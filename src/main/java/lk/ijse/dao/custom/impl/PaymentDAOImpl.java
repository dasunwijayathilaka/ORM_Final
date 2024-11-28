package lk.ijse.dao.custom.impl;

import lk.ijse.config.SessionFactoryConfiguration;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.entity.Payment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public ArrayList<Payment> getDataUsingRegId(int regId) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();

        try {
            // HQL query to fetch Payment records with the specified regId in Registration
            Query<Payment> query = session.createQuery("FROM Payment p WHERE p.registration.regId = :regId", Payment.class);
            query.setParameter("regId", regId);  // Set the regId parameter

            // Fetch result list from query
            List<Payment> paymentList = query.list();

            // Convert List to ArrayList (if needed)
            return new ArrayList<>(paymentList);

        } finally {
            session.close(); // Always close the session after use
        }
    }



    @Override
    public ArrayList<Payment> getAll() throws SQLException, ClassNotFoundException, IOException {
        return null;
    }

    @Override
    public boolean save(Payment entity) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }

    @Override
    public boolean update(Payment entity) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();

        try {
            // HQL query to fetch the latest payment id
            Query<Integer> query = session.createQuery("SELECT p.id FROM Payment p ORDER BY p.id DESC", Integer.class);
            query.setMaxResults(1); // Limit to get only the latest result

            Integer lastId = query.uniqueResult(); // Fetch the last paymentID

            // Check if lastId is null, which happens if there are no entries in the database
            if (lastId == null) {
                return String.valueOf(1); // Start from ID 1 if no payments exist
            } else {
                return String.valueOf(lastId + 1); // Increment the last ID by 1 if it exists
            }
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }

    @Override
    public Payment search(Object... args) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean savePayment(Payment payment, Session session) throws SQLException, ClassNotFoundException, IOException {
        session.save(payment);

        return true;
    }

    @Override
    public String getSumOfTransactionsAmount() {
        Session session = null;
        String totalAmount = null;
        try {
            // Get the current Hibernate session
            session = SessionFactoryConfiguration.getInstance().getSession();

            // Begin transaction
            session.beginTransaction();

            // Use HQL to calculate the sum of the amount column
            String hql = "SELECT SUM(p.amount) FROM Payment p";
            Query<Double> query = session.createQuery(hql, Double.class);

            // Get the result
            Double sum = query.uniqueResult();
            totalAmount = sum != null ? String.valueOf(sum) : "0.0";

            // Commit transaction
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return totalAmount;
    }



}
