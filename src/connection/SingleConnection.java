package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnection {

	private static final String banco = "jdbc:postgresql://localhost:5432/loja?autoReconnect=true​";
	private static final String password = "estudo";
	private static final String user = "postgres";
	
	private static Connection connection = null;
	
	static {
		conectar();
	}
	
	public SingleConnection() {
		conectar();
	}
	
	private static void conectar() {
		try {
			if (connection == null) {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(banco, user, password);
				connection.setAutoCommit(false);
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao conectar ao banco de dados");
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
}
