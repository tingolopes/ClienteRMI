package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static Conexao instancia;
    private Connection conexao = null;
    //private String url = "jdbc:postgresql://localhost:5432/sd";
    private String url = "jdbc:mysql://localhost:3306/sd";
    //private String user = "postgres";
    private String user = "root";
    //private String password = "postgres";
    private String password = "";

    private Conexao() throws ClassNotFoundException, SQLException {
        //Class.forName("org.postgresql.Driver");
        Class.forName("com.mysql.jdbc.Driver");
        conexao = DriverManager.getConnection(url, user, password);
        conexao.setAutoCommit(false);
        conexao.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    public static synchronized Conexao getInstancia() throws ClassNotFoundException, SQLException {
        if (instancia == null) {
            instancia = new Conexao();
        }
        return instancia;
    }

    public Connection getConexao() {
        return conexao;
    }

    public void fechar() throws SQLException {
        conexao.close();
    }
}
