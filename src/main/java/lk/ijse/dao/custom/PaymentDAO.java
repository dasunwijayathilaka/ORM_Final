package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Payment;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentDAO extends CrudDAO<Payment> {

    public ArrayList<Payment>  getDataUsingRegId(int regId) throws SQLException, ClassNotFoundException, IOException;

    public boolean savePayment(Payment payment, Session session) throws SQLException, ClassNotFoundException, IOException;

    String getSumOfTransactionsAmount();
}
