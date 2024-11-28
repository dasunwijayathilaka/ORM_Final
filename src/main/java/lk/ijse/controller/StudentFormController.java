package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Util.Regex;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.config.SessionFactoryConfiguration;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;
import lk.ijse.entity.UserSession;
import lk.ijse.tdm.StudentTm;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPhoneNumber;

    @FXML
    private TableColumn<?, ?> colRegisterDate;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblId;

    @FXML
    private Label lblUserId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<StudentTm> tblStudent;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private JFXButton btnStudentDelete;

    @FXML
    private JFXButton btnStudentSave;

    @FXML
    private JFXButton btnStudentUpdate;

    // system ekata log wechcha kenage userID eka
    int userID = UserSession.getInstance().getUserId();

    StudentBO studentBO  = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);



    public void initialize() {
        lblDate.setText(LocalDate.now().toString());
        lblUserId.setText(String.valueOf(userID));
        generateNewStudentID();
        buttonsDisableAndEnable();
        loadAllUsers();
        setCellValueFactory();
        showSelectedUserDetails();
    }

    private void showSelectedUserDetails() {
        StudentTm selectedUser = tblStudent.getSelectionModel().getSelectedItem();
        tblStudent.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            lblId.setText(String.valueOf(selectedUser.getId()));
            txtName.setText(selectedUser.getName());
            lblUserId.setText(String.valueOf(selectedUser.getUserId()));
            txtPhoneNumber.setText(selectedUser.getPhoneNumber());
            txtAddress.setText(selectedUser.getAddress());

            btnStudentUpdate.setDisable(false);
            btnStudentSave.setDisable(true);

            btnStudentDelete.setDisable(false);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registerDate"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    private void loadAllUsers() {
        tblStudent.getItems().clear();
        try {
            /*Get all Students*/
            ArrayList<StudentDTO> allStudents = studentBO.getAllStudents();

            for (StudentDTO studentDTO : allStudents) {
                tblStudent.getItems().add(new StudentTm(
                        studentDTO.getStudentId(),
                        studentDTO.getName(),
                        studentDTO.getAddress(),
                        studentDTO.getPhone(),
                        studentDTO.getRegDate(),
                        studentDTO.getUser()
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void buttonsDisableAndEnable() {
        btnStudentUpdate.setDisable(true);
        btnStudentSave.setDisable(false);
        btnStudentDelete.setDisable(true);
    }

    private void generateNewStudentID() {
        try {
            String nextStudentId = studentBO.generateNewID();

            lblId.setText(String.valueOf(nextStudentId));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }
    void clearFields() {
        txtName.setText(null);
        txtName.setStyle("");

        txtAddress.setText(null);
        txtAddress.setStyle("");

        txtPhoneNumber.setText(null);
        txtPhoneNumber.setStyle("");

        txtID.setText(null);
        txtID.setStyle("");

        generateNewStudentID();
        initialize();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        int id = Integer.parseInt(lblId.getText());

        try {
            boolean isDeleted = studentBO.deleteStudent(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Student deleted!").show();
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
    void btnSaveOnAction(ActionEvent event) {

        int id = Integer.parseInt(lblId.getText());
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtPhoneNumber.getText();
        Date date = Date.valueOf(lblDate.getText());

        if (!name.isEmpty() || !address.isEmpty() || !tel.isEmpty()) {

            if (isValied()) {
                try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
                    Transaction transaction = session.beginTransaction();
                    // Retrieve the User object from the database
                    int userID = UserSession.getInstance().getUserId();
                    User user = session.get(User.class, userID);
                    System.out.println(userID);

                    // Check if user was found in the database
                    if (user == null) {
                        new Alert(Alert.AlertType.ERROR, "User not found in the database").show();
                        return;
                    }

                    // Create a new StudentDTO with the fetched User object
                    StudentDTO studentDTO = new StudentDTO(id, name, address, tel, date, user.getUserId());

                    // Save the StudentDTO
                    boolean isSaved = studentBO.saveStudent(studentDTO);
                    if (isSaved) {
                        transaction.commit();
                        new Alert(Alert.AlertType.CONFIRMATION, "Student saved!").show();
                        clearFields();
                        initialize();
                    } else {
                        transaction.rollback();
                        new Alert(Alert.AlertType.ERROR, "Failed to save the student").show();
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = txtID.getText();

        if (!txtID.getText().isEmpty()) {

            if (isSearchIdValied()) {
                Student student = studentBO.studentSearch(Integer.parseInt(id));
                if (student != null) {
                    lblId.setText(String.valueOf(student.getStudentId()));
                    txtName.setText(student.getName());
                    txtAddress.setText(student.getAddress());
                    txtPhoneNumber.setText(student.getPhone());
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Student not found!").show();
                }
            } else {
                validationError();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please enter a Student ID!").show();
        }
    }

    private void validationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Validation Failed");
        alert.setContentText("Please fill in all fields correctly.");
        alert.showAndWait();
    }

    public boolean isSearchIdValied(){
        return Regex.setTextColor(lk.ijse.Util.TextField.INTID, txtID);
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        int id = Integer.parseInt(lblId.getText());
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtPhoneNumber.getText();
        Date date = Date.valueOf(lblDate.getText());

        StudentDTO studentDTO = new StudentDTO(id, name, address, tel, date, userID);

        if (!name.isEmpty() || !address.isEmpty() || !tel.isEmpty()) {

            if (isValied()) {
                try {
                    boolean isUpdated = studentBO.updateStudent(studentDTO);
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Student updated!").show();
                        clearFields();
                        initialize();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter valid details!").show();
        }
    }

    public boolean isValied(){
        boolean address = Regex.setTextColor(lk.ijse.Util.TextField.ADDRESS, txtAddress);
        boolean name = Regex.setTextColor(lk.ijse.Util.TextField.NAME, txtName);
        boolean tel = Regex.setTextColor(lk.ijse.Util.TextField.TEL, txtPhoneNumber);

        return address && name && tel;
    }

    public void addressKeyRelaseAction(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.ADDRESS, txtAddress);
    }

    public void nameKeyRelaseAction(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.NAME, txtName);
    }

    public void telKeyRelaseAction(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.TEL, txtPhoneNumber);
    }

    public void idKeyReleaseAction(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.Util.TextField.INTID, txtID);
    }
}
