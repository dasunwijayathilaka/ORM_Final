package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.RegistrationDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentBO extends SuperBO {

    String generateNewID() throws SQLException, ClassNotFoundException, IOException;

    public ArrayList<PaymentDTO> getPaymentDetails(int regId) throws SQLException, ClassNotFoundException, IOException;

    boolean savePayment(PaymentDTO paymentDTO, RegistrationDTO registrationDTO) throws SQLException, ClassNotFoundException, IOException;

    String getSumOfTransactionsAmount();
}
