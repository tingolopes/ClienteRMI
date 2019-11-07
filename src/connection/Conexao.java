/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author santos
 */
public class Conexao {

    private static Conexao instancia;
    private Connection conexao = null;
    private String url = "jdbc:mysql://localhost/sd";
    private String user = "root";
    private String password = "";

    private Conexao() throws ClassNotFoundException, SQLException {
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
