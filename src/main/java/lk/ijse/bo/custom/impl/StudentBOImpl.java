package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentBOImpl implements StudentBO {

    StudentDAO studentDAO = (StudentDAO)DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STUDENT);

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        return studentDAO.generateNewID();
    }

    @Override
    public boolean saveStudent(StudentDTO dto) throws SQLException, ClassNotFoundException, IOException {
        return studentDAO.save(new Student(dto.getStudentId(), dto.getName(), dto.getAddress(), dto.getPhone(), dto.getRegDate(), new User(dto.getUser())));
    }

    @Override
    public boolean updateStudent(StudentDTO dto) throws SQLException, ClassNotFoundException, IOException {
        return studentDAO.update(new Student(dto.getStudentId(), dto.getName(), dto.getAddress(), dto.getPhone(), dto.getRegDate(), new User(dto.getUser())));
    }

    @Override
    public ArrayList<StudentDTO> getAllStudents() throws SQLException, ClassNotFoundException, IOException {
        ArrayList<StudentDTO> allStudents = new ArrayList<>();

        ArrayList<Student> all = studentDAO.getAll();
        for (Student student : all) {
            allStudents.add(new StudentDTO(student.getStudentId(), student.getName(), student.getAddress(), student.getPhone(), student.getRegDate(), student.getUser().getUserId()));
        }
        return allStudents;
    }

    @Override
    public boolean deleteStudent(int id) throws SQLException, ClassNotFoundException, IOException {
        return studentDAO.delete(String.valueOf(id));
    }

    @Override
    public Student studentSearch(int id) throws SQLException, ClassNotFoundException {
        return studentDAO.search(id);
    }

    @Override
    public List<Object[]> studentSearchForPayment(int id) throws IOException {
        return studentDAO.studentSearchForPayment(id);
    }

    @Override
    public String getStudentCount() throws SQLException, ClassNotFoundException {
        return studentDAO.getStudentCount();
    }
}
