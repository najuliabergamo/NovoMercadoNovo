package Classes;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Cupom extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Construtor que recebe o usuário e o total da compra
     */
    public Cupom(Usuario usuario, double total) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(208, 251, 253));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Compra finalizada com sucesso!");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(88, 62, 275, 20);
        contentPane.add(lblNewLabel);

        JButton btnNotaFiscal = new JButton("Emitir nota fiscal");
        btnNotaFiscal.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnNotaFiscal.setBackground(new Color(128, 255, 128));
        btnNotaFiscal.setBounds(88, 114, 260, 23);
        contentPane.add(btnNotaFiscal);

        
        btnNotaFiscal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    Cupom.this,
                    "Nota Fiscal:\nNome: " + usuario.getNome() +
                    "\nCPF: " + usuario.getCpf() +
                    "\nValor Total: R$ " + String.format("%.2f", total)
                );
            }
        });

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnSair.setBackground(new Color(128, 255, 128));
        btnSair.setBounds(88, 148, 260, 23);
        contentPane.add(btnSair);
        
        JButton voltarCupom = new JButton("Voltar");
        voltarCupom.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        voltarCupom.addActionListener(e -> {
            
            Compras telaCompras = new Compras();
            telaCompras.setVisible(true);

       
            this.dispose();
        });
        voltarCupom.setBackground(new Color(255, 149, 149));
        voltarCupom.setBounds(88, 182, 260, 23);
        contentPane.add(voltarCupom);
        
        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(176, 28, 89, 23);
        contentPane.add(btnNewButton);

        btnSair.addActionListener(e -> System.exit(0));
    }

   
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
               
                Usuario teste = new Usuario("Ana Júlia", "12345678901");
                Cupom frame = new Cupom(teste, 50.00);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
