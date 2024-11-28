package lk.ijse.dao.custom.impl;

import lk.ijse.config.SessionFactoryConfiguration;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.QueryDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public ArrayList<Student> getAll() throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();

        try {
            // HQL query to fetch all students
            Query<Student> query = session.createQuery("FROM Student", Student.class);

            // Fetch result list from query
            ArrayList<Student> studentList = (ArrayList<Student>) query.list();

            System.out.println(studentList);
            // Convert List to ArrayList (if needed)
            return studentList;

        } finally {
            session.close(); // Always close the session after use
        }
    }

    @Override
    public boolean save(Student student) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(student);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Student student) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(student);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();

        try {
            // HQL query to fetch the latest student id
            Query<Integer> query = session.createQuery("SELECT s.studentId FROM Student s ORDER BY s.studentId DESC", Integer.class);
            query.setMaxResults(1); // Limit to get only the latest result

            Integer lastId = query.uniqueResult(); // Fetch the last studentId

            // Check if lastId is null, which happens if there are no entries in the database
            if (lastId == null) {
                return String.valueOf(1); // Start from ID 1 if no students exist
            } else {
                return String.valueOf(lastId + 1); // Increment the last ID by 1 if it exists
            }
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException, IOException {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery query = session.createNativeQuery("delete from Students where studentId = ?1");
        query.setParameter(1, id);
        query.executeUpdate();

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Student search(Object... args) throws SQLException, ClassNotFoundException {
        Student student = null;
        Session session = null;
        Transaction transaction = null;

        try {
            session = SessionFactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // Extracting the first argument as studentId
            int studentId = (int) args[0];  // Assuming the first argument is an integer studentId

            System.out.println("Searching for student with ID: " + studentId);

            Query<Student> query = session.createQuery("FROM Student WHERE studentId = :studentId", Student.class);
            query.setParameter("studentId", studentId);

            student = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return student;
    }



    @Override
    public List<Object[]> studentSearchForPayment(int id) throws IOException {
        return queryDAO.studentSearchForPayment(id);
    }

    @Override
    public String getStudentCount() {
        Session session = null;
        String studentCount = null;
        try {
            // Get the current Hibernate session
            session = SessionFactoryConfiguration.getInstance().getSession();

            // Begin transaction
            session.beginTransaction();

            // Use HQL to get the total count of students
            String hql = "SELECT COUNT(s) FROM Student s";
            Query<Long> query = session.createQuery(hql, Long.class);

            // Get the result
            Long count = query.uniqueResult();
            studentCount = count != null ? String.valueOf(count) : null;

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
        return studentCount;
    }



}
