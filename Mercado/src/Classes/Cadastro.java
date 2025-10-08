package Classes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

public class Cadastro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField CdNome;
    private JTextField CdCpf;
    private JPasswordField CdCrie;
    private JPasswordField CdConfirme;

    public Cadastro() {
        setTitle("CADASTRO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(191, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Faça seu cadastro!");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel.setBounds(128, 24, 193, 19);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Preencha todos os campos");
        lblNewLabel_1.setBounds(149, 41, 158, 14);
        contentPane.add(lblNewLabel_1);

        CdNome = new JTextField();
        CdNome.setBounds(30, 76, 158, 20);
        contentPane.add(CdNome);
        CdNome.setColumns(10);

        CdCpf = new JTextField();
        CdCpf.setBounds(30, 119, 158, 20);
        contentPane.add(CdCpf);
        CdCpf.setColumns(10);

        // só deixa números no CPF
        CdCpf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });

        CdCrie = new JPasswordField();
        CdCrie.setBounds(231, 76, 158, 20);
        contentPane.add(CdCrie);

        CdConfirme = new JPasswordField();
        CdConfirme.setBounds(231, 119, 158, 20);
        contentPane.add(CdConfirme);

        JRadioButton RadioAdm = new JRadioButton("Administrador");
        RadioAdm.setFont(new Font("Tahoma", Font.PLAIN, 14));
        RadioAdm.setBackground(new Color(191, 255, 255));
        RadioAdm.setBounds(212, 162, 120, 23);
        contentPane.add(RadioAdm);

        JRadioButton RadioCliente = new JRadioButton("Cliente");
        RadioCliente.setFont(new Font("Tahoma", Font.PLAIN, 14));
        RadioCliente.setBackground(new Color(191, 255, 255));
        RadioCliente.setBounds(128, 162, 80, 23);
        contentPane.add(RadioCliente);

        // agrupa os radios (só pode selecionar um)
        ButtonGroup grupoRadio = new ButtonGroup();
        grupoRadio.add(RadioAdm);
        grupoRadio.add(RadioCliente);

        JLabel lblNewLabel_2 = new JLabel("Sou");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel_2.setBounds(50, 164, 46, 19);
        contentPane.add(lblNewLabel_2);

        JButton ButtonCadastrar = new JButton("CADASTRAR");
        ButtonCadastrar.setBackground(new Color(187, 255, 119));
        ButtonCadastrar.setBounds(128, 192, 183, 23);
        contentPane.add(ButtonCadastrar);

        // AÇÃO DO BOTÃO CADASTRAR
        ButtonCadastrar.addActionListener(e -> {
            String nome = CdNome.getText().trim();
            String cpf = CdCpf.getText().trim();
            String senha = new String(CdCrie.getPassword());
            String confirmaSenha = new String(CdConfirme.getPassword());
            String tipo = RadioAdm.isSelected() ? "Administrador" : "Cliente";

            // 1️⃣ Validação do nome
            if (nome.isEmpty() || !nome.matches("[\\p{L} ]+")) {
                JOptionPane.showMessageDialog(this, "Nome inválido! Não pode conter números ou símbolos.");
                CdNome.requestFocus();
                return;
            }

            // 2️⃣ Validação do CPF
            if (!cpf.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, "CPF inválido! Deve conter exatamente 11 números.");
                CdCpf.requestFocus();
                return;
            }

            // 3️⃣ Validação da senha
            if (senha.length() < 6) {
                JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 6 caracteres!");
                CdCrie.requestFocus();
                return;
            }
            if (!senha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!");
                CdConfirme.requestFocus();
                return;
            }

            // 4️⃣ Tipo do usuário
            if (!RadioAdm.isSelected() && !RadioCliente.isSelected()) {
                JOptionPane.showMessageDialog(this, "Selecione se é Administrador ou Cliente!");
                return;
            }

            // 5️⃣ Salvar no banco
            try {
                java.sql.Connection conn = Conexao.conectar();
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco!");
                    return;
                }

                String sql = "INSERT INTO usuarios (nome, cpf, senha, tipo) VALUES (?, ?, ?, ?)";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nome);
                ps.setString(2, cpf);
                ps.setString(3, senha);
                ps.setString(4, tipo);

                int linhas = ps.executeUpdate();
                conn.close();

                if (linhas > 0) {
                    JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
                    this.dispose();          // fecha a tela de cadastro
                    new Login().setVisible(true); // abre tela de login
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: cadastro não realizado!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
            }
        });

        JButton ButtonJaTenho = new JButton("JÁ TENHO CONTA");
        ButtonJaTenho.setBackground(new Color(187, 255, 119));
        ButtonJaTenho.setBounds(128, 227, 180, 23);
        contentPane.add(ButtonJaTenho);
        
        ButtonJaTenho.addActionListener(e -> {
         
            Login telaLogin = new Login();
            telaLogin.setVisible(true);

       
            this.dispose();
        });

        JLabel lblNewLabel_3 = new JLabel("Nome:");
        lblNewLabel_3.setBounds(30, 57, 46, 14);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("CPF:");
        lblNewLabel_4.setBounds(30, 106, 46, 14);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Crie sua senha:");
        lblNewLabel_5.setBounds(231, 57, 90, 14);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Confirme sua senha:");
        lblNewLabel_6.setBounds(231, 107, 120, 14);
        contentPane.add(lblNewLabel_6);
    }
}
