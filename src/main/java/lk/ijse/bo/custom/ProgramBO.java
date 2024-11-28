package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.entity.Program;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ProgramBO extends SuperBO {

    String generateNewID() throws SQLException, ClassNotFoundException, IOException;

    public boolean saveProgram(ProgramDTO dto) throws SQLException, ClassNotFoundException, IOException;

    public ArrayList<ProgramDTO> getAllPrograms() throws SQLException, ClassNotFoundException, IOException;

    public boolean deleteProgram(String id) throws SQLException, ClassNotFoundException, IOException;

    public boolean updateProgram(ProgramDTO dto) throws SQLException, ClassNotFoundException, IOException;

    // this method for registration UI
    public List<String> getProgramNames() throws SQLException, ClassNotFoundException;

    // this method for registration UI
    Program searchByName(String name) throws SQLException, ClassNotFoundException;

    String getProgramCount();
}
