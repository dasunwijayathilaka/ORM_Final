package lk.ijse.dao.custom.impl;

import lk.ijse.config.SessionFactoryConfiguration;
import lk.ijse.dao.custom.RegistrationDAO;
import lk.ijse.entity.Registration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAOImpl implements RegistrationDAO {


    @Override
    public List<Object[]> loadAllRegistrationDetails() throws IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        List<Object[]> results = new ArrayList<>();

        try {
            transaction = session.beginTransaction();

            // HQL query to fetch required details from Registrations, Students, and Programs
            String hql = "SELECT r.regId, r.paidAmount, r.program.programId, r.student.studentId, r.student.name, r.program.fee, r.program.programName " +
                    "FROM Registration r " +
                    "JOIN r.student s " +
                    "JOIN r.program p";

            Query<Object[]> query = session.createQuery(hql, Object[].class);

            // Execute the query and get the result list
            results = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception closeException) {
                closeException.printStackTrace();
            }
        }
        return results;
    }


    @Override
    public ArrayList<Registration> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean saveRegistration(Registration registration, Session session) throws SQLException, ClassNotFoundException, IOException {

        session.save(registration);

        return true;
    }

    @Override
    public boolean updateAmount(Registration registration, Session session) {
        try {
            // HQL update query to set paidAmount where regId matches
            String hql = "UPDATE Registration r SET r.paidAmount = :paidAmount WHERE r.regId = :regId";
            Query query = session.createQuery(hql);
            query.setParameter("paidAmount", registration.getPaidAmount());
            query.setParameter("regId", registration.getRegId());

            // Execute update and check the number of affected rows
            int affectedRows = query.executeUpdate();
            return affectedRows > 0;  // Returns true if at least one row was updated

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getRegistrationCount() {
        Session session = null;
        String regCount = null;
        try {
            // Get the current Hibernate session
            session = SessionFactoryConfiguration.getInstance().getSession();

            // Begin transaction
            session.beginTransaction();

            // Use HQL to get the total count of registrations
            String hql = "SELECT COUNT(r) FROM Registration r";
            Query<Long> query = session.createQuery(hql, Long.class);

            // Get the result
            Long count = query.uniqueResult();
            regCount = count != null ? String.valueOf(count) : null;

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
        return regCount;
    }


    @Override
    public boolean update(Registration entity) throws SQLException, ClassNotFoundException {
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
            // HQL query to fetch the latest registration id
            Query<Integer> query = session.createQuery("SELECT r.regId FROM Registration r ORDER BY r.regId DESC", Integer.class);
            query.setMaxResults(1); // Limit to get only the latest result

            Integer lastId = query.uniqueResult(); // Fetch the last registration

            // Check if lastId is null, which happens if there are no entries in the database
            if (lastId == null) {
                return String.valueOf(1); // Start from ID 1 if no registration exist
            } else {
                return String.valueOf(lastId + 1); // Increment the last ID by 1 if it exists
            }
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Registration search(Object... args) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Registration entity) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }
}
