package Classes;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField NomeF;
    private JTextField CPFF;
    private JPasswordField senhaLog;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login() {
        setBackground(new Color(208, 251, 253));
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Master\\Downloads\\pngtree-red-3d-heart-png-image_2569975.png"));
        setTitle("Mercadinho");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(215, 245, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblNewLabel.setBounds(177, 11, 118, 27);
        contentPane.add(lblNewLabel);

        JLabel lblInsiraSeusDados = new JLabel("Insira seus dados:");
        lblInsiraSeusDados.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblInsiraSeusDados.setBounds(159, 32, 118, 27);
        contentPane.add(lblInsiraSeusDados);

        NomeF = new JTextField();
        NomeF.setBounds(116, 80, 190, 20);
        contentPane.add(NomeF);
        NomeF.setColumns(10);

        CPFF = new JTextField();
        CPFF.setColumns(10);
        CPFF.setBounds(116, 123, 190, 20);
        contentPane.add(CPFF);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNome.setBounds(116, 54, 118, 27);
        contentPane.add(lblNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCpf.setBounds(116, 101, 118, 27);
        contentPane.add(lblCpf);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblSenha.setBounds(116, 139, 118, 27);
        contentPane.add(lblSenha);

        senhaLog = new JPasswordField();
        senhaLog.setBounds(116, 165, 190, 20);
        contentPane.add(senhaLog);

        JLabel lblVoceE = new JLabel("Sou");
        lblVoceE.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblVoceE.setBounds(71, 189, 32, 27);
        contentPane.add(lblVoceE);

        JRadioButton AdmR = new JRadioButton("Administrador");
        AdmR.setBackground(new Color(215, 245, 255));
        AdmR.setFont(new Font("Tahoma", Font.PLAIN, 13));
        AdmR.setBounds(109, 192, 109, 23);
        contentPane.add(AdmR);

        JRadioButton UsuarioR = new JRadioButton("Cliente");
        UsuarioR.setFont(new Font("Tahoma", Font.PLAIN, 13));
        UsuarioR.setBackground(new Color(215, 245, 255));
        UsuarioR.setBounds(244, 192, 109, 23);
        contentPane.add(UsuarioR);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(AdmR);
        grupo.add(UsuarioR);

        JButton EntrarButton = new JButton("Entrar");
        EntrarButton.addActionListener(e -> {
            String nome = NomeF.getText().trim();
            String CPF = CPFF.getText().trim();
            String senhaDigitada = new String(senhaLog.getPassword());
            String tipo = AdmR.isSelected() ? "Administrador" : UsuarioR.isSelected() ? "Cliente" : "";

            if(nome.isEmpty() || CPF.isEmpty() || senhaDigitada.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                return;
            }

            if(CPF.length() != 11) {
                JOptionPane.showMessageDialog(null, "O CPF deve conter exatamente 11 dígitos!");
                return;
            }

            if(tipo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione se você é Administrador ou Cliente!");
                return;
            }

            // ✅ Conectar ao banco e verificar usuário + senha
            try (Connection conn = Conexao.conectar()) {
                if(conn == null) {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco!");
                    return;
                }

                String sql = "SELECT * FROM usuarios WHERE nome=? AND cpf=? AND tipo=? AND senha=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nome);
                ps.setString(2, CPF);
                ps.setString(3, tipo);
                ps.setString(4, senhaDigitada);

                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    Usuario usuario = new Usuario(nome, CPF);
                    JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");

                    if(tipo.equals("Administrador")) {
                        new Visualizar().setVisible(true);
                    } else {
                        new Compras(usuario).setVisible(true);
                    }

                    dispose(); // fecha a tela de login
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário não encontrado ou dados incorretos!");
                }

            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao logar: " + ex.getMessage());
            }

        });

        EntrarButton.setBackground(new Color(136, 236, 125));
        EntrarButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        EntrarButton.setBounds(109, 222, 89, 23);
        contentPane.add(EntrarButton);
        
        JButton voltarLogin = new JButton("Voltar");
        voltarLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        voltarLogin.addActionListener(e -> {
            
            Cadastro telaCadastro = new Cadastro();
            telaCadastro.setVisible(true);

       
            this.dispose();
        });
        voltarLogin.setBackground(new Color(255, 149, 149));
        voltarLogin.setBounds(217, 222, 89, 23);
        contentPane.add(voltarLogin);
    }
}
