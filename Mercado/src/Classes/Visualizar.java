package Classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class Visualizar extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabela;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Visualizar frame = new Visualizar();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Visualizar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(208, 251, 253));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Lista de Produtos Cadastrados:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel.setBounds(100, 11, 300, 25);
        contentPane.add(lblNewLabel);

        tabela = new JTable();
        tabela.setBounds(10, 50, 460, 180);
        contentPane.add(tabela);

        tabela.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Produto", "Preço", "Resumo", "Quantidade"}
        ));

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBackground(new Color(128, 255, 128));
        btnAdicionar.setBounds(10, 241, 100, 25);
        contentPane.add(btnAdicionar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(128, 255, 128));
        btnEditar.setBounds(133, 241, 100, 25);
        contentPane.add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(128, 255, 128));
        btnExcluir.setBounds(257, 241, 100, 25);
        contentPane.add(btnExcluir);

        JButton sairAdm = new JButton("Sair");
        sairAdm.addActionListener(e -> System.exit(0));
        sairAdm.setBackground(new Color(255, 149, 149));
        sairAdm.setBounds(370, 242, 100, 23);
        contentPane.add(sairAdm);

        atualizarTabela();

        // Botão de Adicionar Produto
        btnAdicionar.addActionListener(e -> {
            try {
                String nome = JOptionPane.showInputDialog("Nome do Produto:");
                if(nome == null || nome.trim().isEmpty()) return;

                String precoStr = JOptionPane.showInputDialog("Preço do Produto:");
                if(precoStr == null || precoStr.trim().isEmpty()) return;
                double preco = Double.parseDouble(precoStr);

                String resumo = JOptionPane.showInputDialog("Resumo do Produto:");
                if(resumo == null) resumo = "";

                String quantidadeStr = JOptionPane.showInputDialog("Quantidade do Produto:");
                if(quantidadeStr == null || quantidadeStr.trim().isEmpty()) return;
                int quantidade = Integer.parseInt(quantidadeStr);

                Connection conn = Conexao.conectar();
                String sql = "INSERT INTO produtos (nome, preco, resumo, quantidade) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nome);
                ps.setDouble(2, preco);
                ps.setString(3, resumo);
                ps.setInt(4, quantidade);
                ps.executeUpdate();
                conn.close();

                atualizarTabela();
                JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso!");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar: " + ex.getMessage());
            }
        });

        // Botão de Editar Produto
        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if(linha < 0) {
                JOptionPane.showMessageDialog(null, "Selecione um produto para editar!");
                return;
            }

            try {
                int id = (int) tabela.getValueAt(linha, 0);
                String nome = JOptionPane.showInputDialog("Novo nome:", tabela.getValueAt(linha, 1));
                if(nome == null || nome.trim().isEmpty()) return;

                String precoStr = JOptionPane.showInputDialog("Novo preço:", tabela.getValueAt(linha, 2));
                if(precoStr == null || precoStr.trim().isEmpty()) return;
                double preco = Double.parseDouble(precoStr);

                String resumo = JOptionPane.showInputDialog("Novo resumo:", tabela.getValueAt(linha, 3));
                if(resumo == null) resumo = "";

                String quantidadeStr = JOptionPane.showInputDialog("Nova quantidade:", tabela.getValueAt(linha, 4));
                if(quantidadeStr == null || quantidadeStr.trim().isEmpty()) return;
                int quantidade = Integer.parseInt(quantidadeStr);

                Connection conn = Conexao.conectar();
                String sql = "UPDATE produtos SET nome=?, preco=?, resumo=?, quantidade=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nome);
                ps.setDouble(2, preco);
                ps.setString(3, resumo);
                ps.setInt(4, quantidade);
                ps.setInt(5, id);
                ps.executeUpdate();
                conn.close();

                atualizarTabela();
                JOptionPane.showMessageDialog(null, "Produto editado com sucesso!");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao editar: " + ex.getMessage());
            }
        });

        // Botão de Excluir Produto
        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if(linha < 0) {
                JOptionPane.showMessageDialog(null, "Selecione um produto para excluir!");
                return;
            }

            try {
                int id = (int) tabela.getValueAt(linha, 0);
                Connection conn = Conexao.conectar();
                String sql = "DELETE FROM produtos WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                conn.close();

                atualizarTabela();
                JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao excluir: " + ex.getMessage());
            }
        });
    }

    private void atualizarTabela() {
        try {
            Connection conn = Conexao.conectar();
            String sql = "SELECT * FROM produtos";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            modelo.setRowCount(0);

            while(rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("nome"));
                row.add(rs.getDouble("preco"));
                row.add(rs.getString("resumo"));
                row.add(rs.getInt("quantidade"));
                modelo.addRow(row);
            }
            conn.close();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar tabela: " + e.getMessage());
        }
    }
}
