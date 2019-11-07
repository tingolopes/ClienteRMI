/*
 
 
 
 */
package pattern.dao;

import connection.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author santos
 */
public abstract class AbstractDao implements DaoPattern {

    protected Conexao conexao;
    protected Statement stm;
    protected PreparedStatement preparedStm;
    protected ResultSet rs;

    public AbstractDao(Conexao conexao) {
        this.conexao = conexao;
    }

    @Override
    public void createStatement() throws SQLException {
        stm = conexao.getConexao().createStatement();
    }

    @Override
    public void destroyStatement() throws SQLException {
        if (rs != null) {
            rs.close();
            rs = null;
        }
        stm.close();
        stm = null;
    }

    @Override
    public void createPreparedStatement(String sql) throws SQLException {
        preparedStm = conexao.getConexao().prepareStatement(sql);
    }

    @Override
    public void destroyPreparedStatement() throws SQLException {
        preparedStm.close();
        preparedStm = null;
    }

    @Override
    public void commit() throws SQLException {
        this.conexao.getConexao().commit();
    }

    @Override
    public void rollback() throws SQLException {
        this.conexao.getConexao().rollback();
    }

    @Override
    public Integer nextVal() {
        return null;
    }
}
