package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Util.Regex;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.ProgramBO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.tdm.ProgramTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramsFormController {

    @FXML
    private JFXButton btnProgramDelete;

    @FXML
    private JFXButton btnProgramSave;

    @FXML
    private JFXButton btnProgramUpdate;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private Label lblProgramId;

    @FXML
    private TableView<ProgramTM> tblProgram;

    @FXML
    private TextField txtProgramDuration;

    @FXML
    private TextField txtProgramFee;

    @FXML
    private TextField txtProgramName;


    ProgramBO programBO  = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAM);


    public void initialize() {
        generateNewProgramID();
        setCellValueFactory();
        loadAllPrograms();
        showSelectedUserDetails();
        buttonsDisableAndEnable();
        clickEnterButtonMoveCursor();
    }


    private void showSelectedUserDetails() {
        ProgramTM selectedUser = tblProgram.getSelectionModel().getSelectedItem();
        tblProgram.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            lblProgramId.setText(String.valueOf(selectedUser.getId()));
            txtProgramName.setText(selectedUser.getName());
            txtProgramDuration.setText(selectedUser.getDuration());
            txtProgramFee.setText(String.valueOf(selectedUser.getFee()));

            btnProgramUpdate.setDisable(false);
            btnProgramSave.setDisable(true);

            btnProgramDelete.setDisable(false);
        }
    }

    void clickEnterButtonMoveCursor() {
        txtProgramName.setOnAction(event -> txtProgramDuration.requestFocus());
        txtProgramDuration.setOnAction(event -> txtProgramFee.requestFocus());
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void loadAllPrograms() {
        tblProgram.getItems().clear();
        try {

            ArrayList<ProgramDTO> allPrograms = programBO.getAllPrograms();

            for (ProgramDTO programDTO : allPrograms) {
                tblProgram.getItems().add(new ProgramTM(programDTO.getProgramId(), programDTO.getProgramName(), programDTO.getDuration(), programDTO.getFee()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void buttonsDisableAndEnable() {
        btnProgramUpdate.setDisable(true);
        btnProgramSave.setDisable(false);
        btnProgramDelete.setDisable(true);
    }

    private void generateNewProgramID() {
        try {
            String nextProgramId = programBO.generateNewID();

            lblProgramId.setText(nextProgramId);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void clearFields() {
        lblProgramId.setText(null);
        txtProgramDuration.setText(null);
        txtProgramDuration.setStyle("");

        txtProgramFee.setText(null);
        txtProgramFee.setStyle("");

        txtProgramName.setText(null);
        txtProgramName.setStyle("");
        initialize();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblProgramId.getText();

        try {
            boolean isDeleted = programBO.deleteProgram(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "User deleted!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblProgramId.getText();
        String name = txtProgramName.getText();
        String fee = (txtProgramFee.getText());
        String duration = txtProgramDuration.getText();

        if (!id.isEmpty() || !name.isEmpty() || !fee.isEmpty() || !duration.isEmpty()) {
            if (isValied()) {

                ProgramDTO programDTO = new ProgramDTO(id, name, duration, fee);

                try {
                    boolean isSaved = programBO.updateProgram(programDTO);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "User saved!").show();
                        clearFields();
                        initialize();
                    }
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        String id = lblProgramId.getText();
        String name = txtProgramName.getText();
        String fee = (txtProgramFee.getText());
        String duration = txtProgramDuration.getText();

        if (!id.isEmpty() || !name.isEmpty() || !fee.isEmpty() || !duration.isEmpty()) {
            if (isValied()) {

                ProgramDTO programDTO = new ProgramDTO(id, name, duration, fee);

                try {
                    boolean isSaved = programBO.saveProgram(programDTO);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "User saved!").show();
                        clearFields();
                        initialize();
                    }
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }

    }



    @FXML
    public void feeKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.PRICEDOT, txtProgramFee);
    }

    @FXML
    public void durationKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.DURATION, txtProgramDuration);
    }

    @FXML
    public void nameKeyRelaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.NAME, txtProgramName);
    }

    public boolean isValied(){
        boolean nameValid = Regex.setTextColor(lk.ijse.Util.TextField.NAME, txtProgramName);
        boolean duration = Regex.setTextColor(lk.ijse.Util.TextField.DURATION, txtProgramDuration);
        boolean fee = Regex.setTextColor(lk.ijse.Util.TextField.PRICEDOT, txtProgramFee);

        return nameValid && duration && fee;
    }

}
