package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.ProgramBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ProgramDAO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.entity.Program;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramBOImpl implements ProgramBO {

    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PROGRAM);


    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException, IOException {
        return (String) programDAO.generateNewID();
    }

    @Override
    public boolean saveProgram(ProgramDTO dto) throws SQLException, ClassNotFoundException, IOException {
        return programDAO.save(new Program(dto.getProgramId(), dto.getProgramName(), dto.getDuration(), dto.getFee()));
    }

    @Override
    public ArrayList<ProgramDTO> getAllPrograms() throws SQLException, ClassNotFoundException, IOException {
        ArrayList<ProgramDTO> allPrograms = new ArrayList<>();

        ArrayList<Program> all = programDAO.getAll();
        for (Program program : all) {
            allPrograms.add(new ProgramDTO(program.getProgramId(), program.getProgramName(), program.getDuration(), program.getFee()));
        }
        return allPrograms;
    }

    @Override
    public boolean deleteProgram(String id) throws SQLException, ClassNotFoundException, IOException {
        return programDAO.delete(id);
    }

    @Override
    public boolean updateProgram(ProgramDTO dto) throws SQLException, ClassNotFoundException, IOException {
        return programDAO.update(new Program(dto.getProgramId(), dto.getProgramName(), dto.getDuration(), dto.getFee()));
    }

    @Override
    public List<String> getProgramNames() throws SQLException, ClassNotFoundException {
        return programDAO.getProgramNames();
    }

    @Override
    public Program searchByName(String name) throws SQLException, ClassNotFoundException {
        return programDAO.searchByName(name);
    }

    @Override
    public String getProgramCount() {
        return programDAO.getProgramCount();
    }
}
