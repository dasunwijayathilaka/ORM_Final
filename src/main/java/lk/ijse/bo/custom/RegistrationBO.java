package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.RegistrationDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface RegistrationBO extends SuperBO {

    String generateNewID() throws SQLException, ClassNotFoundException, IOException;

    boolean saveRegistration(RegistrationDTO registrationDTO, PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException, IOException;

    List<Object[]> loadAllRegistrationDetails() throws IOException;

    String getRegistrationCount();
}
