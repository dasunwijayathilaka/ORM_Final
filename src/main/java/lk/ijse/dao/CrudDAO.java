package lk.ijse.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{
    public ArrayList<T> getAll() throws SQLException, ClassNotFoundException, IOException;
    public boolean save(T entity) throws SQLException, ClassNotFoundException, IOException;
    public boolean update(T entity) throws SQLException, ClassNotFoundException, IOException;
    public boolean exist(String id) throws SQLException, ClassNotFoundException;
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException;
    public boolean delete(String id) throws SQLException, ClassNotFoundException, IOException;
    public T search(Object... args) throws SQLException, ClassNotFoundException;
}
