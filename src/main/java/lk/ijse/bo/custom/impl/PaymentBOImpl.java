package lk.ijse.bo.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.bo.custom.PaymentBO;
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
import java.util.ArrayList;

public class PaymentBOImpl implements PaymentBO {

    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    RegistrationDAO registrationDAO = (RegistrationDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.REGISTRATION);

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        return paymentDAO.generateNewID();
    }

    @Override
    public ArrayList<PaymentDTO> getPaymentDetails(int regId) throws SQLException, ClassNotFoundException, IOException {
        ArrayList<Payment> payments = paymentDAO.getDataUsingRegId(regId);
        ArrayList<PaymentDTO> paymentDTOList = new ArrayList<>();

        // Convert each Payment to PaymentDTO
        for (Payment payment : payments) {
            PaymentDTO paymentDTO = new PaymentDTO(
                    payment.getPaymentId(),
                    new RegistrationDTO(payment.getRegistration().getRegId()),
                    payment.getAmount(),
                    payment.getPaymentDate(),
                    payment.getPaymentMethod()
            );
            paymentDTOList.add(paymentDTO);
        }
        return paymentDTOList;
    }





    @Override
    public boolean savePayment(PaymentDTO paymentDTO, RegistrationDTO registrationDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

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

            if (!isSavedPaymentTable) {
                transaction.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save the payment table!").show();
                return false;
            }

            // Save payment
            boolean isRegistrationTableUpdate = registrationDAO.updateAmount(new Registration(registrationDTO.getRegId(), registrationDTO.getPaidAmount()), session);

            if (isRegistrationTableUpdate) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to update paid amount!").show();
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
    public String getSumOfTransactionsAmount() {
        return paymentDAO.getSumOfTransactionsAmount();
    }

}
