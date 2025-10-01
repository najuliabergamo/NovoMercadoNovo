package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/mercado"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "admin"; 
    public static Connection conectar() {
        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado ao MySQL!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        return null;
    }
}