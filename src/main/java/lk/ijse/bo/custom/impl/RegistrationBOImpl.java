package lk.ijse.bo.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.bo.custom.RegistrationBO;
import lk.ijse.config.SessionFactoryConfiguration;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dao.custom.RegistrationDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Registration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegistrationBOImpl implements RegistrationBO {

    RegistrationDAO registrationDAO = (RegistrationDAO)DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.REGISTRATION);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        return registrationDAO.generateNewID();
    }

    @Override
    public boolean saveRegistration(RegistrationDTO registrationDTO, PaymentDTO paymentDTO) throws IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Save registration
            boolean isSavedRegistrationTable = registrationDAO.saveRegistration(
                    new Registration(
                            registrationDTO.getRegId(),
                            registrationDTO.getStudent(),
                            registrationDTO.getProgram(),
                            registrationDTO.getRegistrationDate(),
                            registrationDTO.getPaidAmount()
                    ),
                    session
            );

            if (!isSavedRegistrationTable) {
                transaction.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save the registration").show();
                return false;
            }

            // Save payment
            boolean isSavedPaymentTable = paymentDAO.savePayment(
                    new Payment(
                            paymentDTO.getPaymentId(),
                            new Registration(paymentDTO.getRegistration().getRegId()),
                            paymentDTO.getAmount(),
                            paymentDTO.getPaymentDate(),
                            paymentDTO.getPaymentMethod()
                    ),
                    session
            );

            if (isSavedPaymentTable) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save the payment").show();
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving the transaction").show();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Object[]> loadAllRegistrationDetails() throws IOException {
        return registrationDAO.loadAllRegistrationDetails();
    }

    @Override
    public String getRegistrationCount() {
        return registrationDAO.getRegistrationCount();
    }

}
