package lk.ijse.dao.custom.impl;

import lk.ijse.config.SessionFactoryConfiguration;
import lk.ijse.dao.custom.ProgramDAO;
import lk.ijse.entity.Program;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramDAOImpl implements ProgramDAO {

    @Override
    public ArrayList<Program> getAll() throws SQLException, ClassNotFoundException, IOException {
            Session session = SessionFactoryConfiguration.getInstance().getSession();

            try {
                // HQL query to fetch all users
                Query<Program> query = session.createQuery("FROM Program", Program.class);

                // Fetch result list from query
                List<Program> programList = query.list();

                // Convert List to ArrayList (if needed)
                return new ArrayList<>(programList);

            } finally {
                session.close(); // Always close the session after use
            }

    }

    @Override
    public boolean save(Program program) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(program);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Program program) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(program);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        String newProgramId = null;

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();

            // Query to get the last program ID in descending order
            Query<String> query = session.createQuery(
                    "SELECT p.programId FROM Program p ORDER BY p.programId DESC",
                    String.class
            );
            query.setMaxResults(1); // Only retrieve the last program ID
            String lastProgramId = query.uniqueResult();

            if (lastProgramId != null) {
                // Extract the numeric part from the last program ID
                int lastIdNumber = Integer.parseInt(lastProgramId.substring(2));
                // Increment the numeric part
                int nextIdNumber = lastIdNumber + 1;
                // Format the new ID with leading zeros (e.g., 1001)
                newProgramId = String.format("CA%04d", nextIdNumber);
            } else {
                // If no records found, start with CA1001
                newProgramId = "CA1001";
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exception (log it, show an alert, etc.)
        }

        return newProgramId;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery query = session.createNativeQuery("delete from Programs where program_id = ?1");
        query.setParameter(1, id);
        query.executeUpdate();

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Program search(Object... args) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getProgramNames() {
        List<String> programList = new ArrayList<>();

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            session.beginTransaction();

            // Use a Hibernate HQL query to get distinct program names
            Query<String> query = session.createQuery("SELECT DISTINCT p.programName FROM Program p", String.class);
            programList = query.getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return programList;
    }

    @Override
    public Program searchByName(String name) {
        Program program = null;

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            session.beginTransaction();

            // Use an HQL query to search for the program by name
            Query<Program> query = session.createQuery(
                    "FROM Program p WHERE p.programName = :name", Program.class);
            query.setParameter("name", name);

            // Retrieve a single result if available
            program = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return program;
    }

    @Override
    public String getProgramCount() {
        Session session = null;
        String programCount = null;
        try {
            // Get the current Hibernate session
            session = SessionFactoryConfiguration.getInstance().getSession();

            // Begin transaction
            session.beginTransaction();

            // Use HQL to get the total count of programs
            String hql = "SELECT COUNT(p) FROM Program p";
            Query<Long> query = session.createQuery(hql, Long.class);

            // Get the result
            Long count = query.uniqueResult();
            programCount = count != null ? String.valueOf(count) : null;

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
        return programCount;
    }


}
