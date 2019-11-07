package pattern.dao;

import java.sql.SQLException;
import java.util.List;

public interface DaoPattern {

    public void saveOrUpdate(Object object) throws SQLException;

    public void delete(Object object) throws SQLException;

    public List<?> list();

    public List<?> list(String criteria);

    public Object findById(Object id);

    public void commit() throws SQLException;

    public void rollback() throws SQLException;

    public void createStatement() throws SQLException;

    public void destroyStatement() throws SQLException;

    public void createPreparedStatement(String sql) throws SQLException;

    public void destroyPreparedStatement() throws SQLException;

    public Integer nextVal();

}
