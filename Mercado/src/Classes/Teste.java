package Classes;

import java.sql.Connection;

public class Teste {
	public static void main(String[] args) {
	    Connection teste = Conexao.conectar();
	    if(teste != null){
	        System.out.println("Conexão OK!");
	    } else {
	        System.out.println("Falha na conexão");
	    }
	}
}

