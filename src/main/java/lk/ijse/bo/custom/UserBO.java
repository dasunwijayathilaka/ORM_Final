package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.UserDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface UserBO extends SuperBO {

    String generateNewID() throws SQLException, ClassNotFoundException, IOException;

    public boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException, IOException;

    public ArrayList<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException, IOException;

    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException, IOException;

    boolean deleteUser(int id) throws SQLException, ClassNotFoundException, IOException;
}
