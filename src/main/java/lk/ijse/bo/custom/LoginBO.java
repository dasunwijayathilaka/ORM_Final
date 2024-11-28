package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.LoginDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface LoginBO extends SuperBO {
    boolean checkCredential(LoginDTO login) throws SQLException, IOException, ClassNotFoundException;
}
