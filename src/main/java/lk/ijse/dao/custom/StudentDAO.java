package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Student;

import java.io.IOException;
import java.util.List;

public interface StudentDAO extends CrudDAO<Student> {
    List<Object[]> studentSearchForPayment(int id) throws IOException;


    String getStudentCount();

}
