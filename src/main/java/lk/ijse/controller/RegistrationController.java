package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Util.Regex;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.bo.custom.ProgramBO;
import lk.ijse.bo.custom.RegistrationBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;
import lk.ijse.tdm.RegistrationTM;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RegistrationController {

    @FXML
    private JFXComboBox<String> cmbPaymentMethod;

    @FXML
    private JFXComboBox<String> cmbProgramName;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colPaidAmount;

    @FXML
    private TableColumn<?, ?> colProgramId;

    @FXML
    private TableColumn<?, ?> colProgramName;

    @FXML
    private TableColumn<?, ?> colRegId;

    @FXML
    private TableColumn<?, ?> colStudentId;

    @FXML
    private TableColumn<?, ?> colStudentName;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblFee;

    @FXML
    private Label lblProgramDuration;

    @FXML
    private Label lblProgramId;

    @FXML
    private Label lblRegistrationId;

    @FXML
    private Label lblStudentName;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<RegistrationTM> tblRegistration;

    @FXML
    private TextField txtFirstPayment;

    @FXML
    private TextField txtStudentId;




    ProgramBO programBO  = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAM);
    RegistrationBO registrationBO  = (RegistrationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REGISTRATION);
    StudentBO studentBO  = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    PaymentBO paymentBO  = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);

    public void initialize() throws IOException {
        lblDate.setText(LocalDate.now().toString());
        setCmbProgramName();
        generateNewRegID();
        clickEnterButtonMoveCursor();
        setPaymentType();
        loadAllRegistrationDetails();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colRegId.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("proId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("proName"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void loadAllRegistrationDetails() throws IOException {
        // Convert the data to RegistrationDetails objects
        ObservableList<RegistrationTM> registrationDetailsList = FXCollections.observableArrayList();

        List<Object[]> allReg = registrationBO.loadAllRegistrationDetails();

        for (Object[] row : allReg) {
            int regId = (Integer) row[0];
            double paidAmount = (Double) row[1];
            String programId = (String) row[2];
            int studentId = (Integer) row[3];
            String studentName = (String) row[4];
            double programFee = (Double) row[5];
            String programName = (String) row[6];

            RegistrationTM details = new RegistrationTM(regId, studentId, studentName, programId, programName, paidAmount, programFee);
            registrationDetailsList.add(details);
        }

        tblRegistration.setItems(registrationDetailsList);
    }

    private void setPaymentType() {
        ObservableList<String> paymentType = FXCollections.observableArrayList();
        cmbPaymentMethod.setValue("Cash");

        paymentType.add("Cash");
        paymentType.add("Card");

        cmbPaymentMethod.setItems(paymentType);
    }
    
    void clickEnterButtonMoveCursor() {
        txtStudentId.setOnAction(event -> {
            try {
                btnSearchOnAction(new ActionEvent());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void generateNewRegID() {
        try {
            String nextRegId = registrationBO.generateNewID();

            lblRegistrationId.setText(String.valueOf(nextRegId));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCmbProgramName() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            obList.addAll(programBO.getProgramNames());
            cmbProgramName.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }











    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        try {
            // Gather data for the registration table
            int regId = Integer.parseInt(lblRegistrationId.getText());
            double paidAmount = Double.parseDouble(txtFirstPayment.getText());
            Date regDate = Date.valueOf(lblDate.getText());
            String programId = lblProgramId.getText();
            String payMethod = cmbPaymentMethod.getValue();
            int studentId = Integer.parseInt(txtStudentId.getText());

            // Check if any required field is missing
            if (regId < 1 || paidAmount < 1 || regDate == null || programId.isEmpty() || studentId < 1 || payMethod == null) {
                new Alert(Alert.AlertType.ERROR, "Please fill all the fields correctly!").show();
                return;
            }

            Program program = new Program(programId);
            Student student = new Student(studentId);

            // Generate payment ID and create DTOs
            int payId = Integer.parseInt(paymentBO.generateNewID());
            RegistrationDTO registrationDTO = new RegistrationDTO(regId, student, program, regDate, paidAmount);
            PaymentDTO paymentDTO = new PaymentDTO(payId, registrationDTO, paidAmount, regDate, payMethod);

            // Save registration and handle the result
            boolean isSaved = registrationBO.saveRegistration(registrationDTO, paymentDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Registration Successful!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save the registration").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format! Please check your inputs.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Please check all fields entered correctly!").show();
        }
    }


    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = txtStudentId.getText();

        if (!txtStudentId.getText().isEmpty()) {

            if (isSearchIdValied()) {
                Student student = studentBO.studentSearch(Integer.parseInt(id));
                if (student != null) {
                    lblStudentName.setText(student.getName());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Student not found!").show();
                }
            } else {
                validationError();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please enter a Student ID!").show();
        }
    }

    public boolean isSearchIdValied() {
        return Regex.setTextColor(lk.ijse.Util.TextField.INTID, txtStudentId);
    }


    private void validationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Validation Failed");
        alert.setContentText("Please fill in all fields correctly.");
        alert.showAndWait();
    }










    @FXML
    void btnViewRegistrationDetailsOnAction(ActionEvent event) {

    }

    @FXML
    void cmbProgramNameOnAction(ActionEvent event) {
        String name = cmbProgramName.getValue();
        try {
            Program program = programBO.searchByName(name);

            if (program != null) {
                lblProgramId.setText(program.getProgramId());
                lblFee.setText(String.valueOf(program.getFee()));
                lblProgramDuration.setText(program.getDuration());
                txtFirstPayment.requestFocus();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) throws IOException {
        clearFields();
    }

    void clearFields() throws IOException {
        txtStudentId.setText(null);
        txtStudentId.setStyle("");

        lblStudentName.setText(null);
        lblStudentName.setStyle("");

        lblProgramDuration.setText(null);
        lblFee.setText(null);
        lblProgramId.setText(null);
        cmbProgramName.setValue(null);

        txtFirstPayment.setText(null);
        txtFirstPayment.setStyle("");

        tblRegistration.getItems().clear();

        initialize();
    }

    public void idKeyReleaseAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.INTID, txtStudentId);
    }

    public void paymentOnKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.PRICEDOT, txtFirstPayment);
    }
}
