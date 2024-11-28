package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Registration;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface RegistrationDAO extends CrudDAO<Registration> {

    List<Object[]> loadAllRegistrationDetails() throws IOException;

    boolean saveRegistration(Registration registration, Session session) throws SQLException, ClassNotFoundException, IOException;

    boolean updateAmount(Registration registration, Session session);

    String getRegistrationCount();
}
