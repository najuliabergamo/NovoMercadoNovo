package Classes;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Compras extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Usuario usuario;
    private JLabel lblNewLabel;
    private JButton voltarCompras;

    public Compras() {
        this(null);
    }

    public Compras(Usuario usuario) {
        this.usuario = usuario;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(208, 251, 253));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 50, 460, 180);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // Modelo com a coluna de quantidade
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Selecionar", "ID", "Produto", "Valor", "Resumo", "Quantidade"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class; // checkbox
                if (columnIndex == 3) return Double.class; // preço
                if (columnIndex == 5) return Integer.class; // quantidade (número inteiro)
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Só a checkbox é editável
            }
        });

        JButton Comprar = new JButton("Comprar!");
        Comprar.setBackground(new Color(128, 255, 128));
        Comprar.setBounds(66, 250, 100, 23);
        contentPane.add(Comprar);
        
        lblNewLabel = new JLabel("Boas Compras!");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel.setBounds(166, 11, 173, 28);
        contentPane.add(lblNewLabel);
        
        JButton sairCompras = new JButton("Sair");
        sairCompras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        sairCompras.addActionListener(e -> System.exit(0));
        sairCompras.setBackground(new Color(255, 149, 149));
        sairCompras.setBounds(344, 250, 100, 23);
        contentPane.add(sairCompras);
        
        voltarCompras = new JButton("Voltar");
        voltarCompras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        voltarCompras.addActionListener(e -> {
            
            Login telaLogin = new Login();
            telaLogin.setVisible(true);

       
            this.dispose();
        });
        voltarCompras.setBackground(new Color(255, 149, 149));
        voltarCompras.setBounds(212, 250, 89, 23);
        contentPane.add(voltarCompras);

        
        atualizarTabela();

        Comprar.addActionListener(e -> {
            double total = 0.0;
            boolean algumSelecionado = false;

            for (int i = 0; i < table.getRowCount(); i++) {
                Boolean selecionado = (Boolean) table.getValueAt(i, 0);
                if (selecionado != null && selecionado) {
                    int quantidadeEmEstoque = (int) table.getValueAt(i, 5); // Quantidade em estoque
                    double preco = (double) table.getValueAt(i, 3);
                    total += preco;

                    // Verifica se há estoque suficiente
                    if (quantidadeEmEstoque <= 0) {
                        JOptionPane.showMessageDialog(this, "Produto sem estoque!");
                        return; // Não pode comprar produto sem estoque
                    }

                    // Subtrai 1 da quantidade em estoque
                    try (Connection conn = Conexao.conectar()) {
                        if (conn == null) {
                            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco!");
                            return;
                        }

                        // Atualiza a quantidade de produto no estoque
                        String sql = "UPDATE produtos SET quantidade = quantidade - 1 WHERE id = ?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setInt(1, (int) table.getValueAt(i, 1)); // ID do produto
                        ps.executeUpdate();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + ex.getMessage());
                        ex.printStackTrace();
                    }

                    algumSelecionado = true;
                }
            }

            if (!algumSelecionado) {
                JOptionPane.showMessageDialog(this, "Selecione pelo menos um produto!");
                return;
            }

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja finalizar a compra?\nTotal: R$ " + String.format("%.2f", total),
                    "Confirmação",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (resposta == JOptionPane.OK_OPTION) {
                new Cupom(usuario, total).setVisible(true);
                this.dispose();
            }
        });
    }

    private void atualizarTabela() {
        try (Connection conn = Conexao.conectar()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco!");
                return;
            }

            String sql = "SELECT * FROM produtos";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            modelo.setRowCount(0); // Limpa a tabela antes de adicionar as novas linhas

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(false); // Checkbox para selecionar o produto
                row.add(rs.getInt("id"));
                row.add(rs.getString("nome"));
                row.add(rs.getDouble("preco"));
                row.add(rs.getString("resumo"));
                row.add(rs.getInt("quantidade")); // Exibe a quantidade em estoque
                modelo.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Compras frame = new Compras();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
