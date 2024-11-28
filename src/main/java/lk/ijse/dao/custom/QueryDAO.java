package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;

import java.io.IOException;
import java.util.List;

public interface QueryDAO extends SuperDAO {
    List<Object[]> studentSearchForPayment(int id) throws IOException;
}
