package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.UserBO;
import lk.ijse.dto.UserDTO;
import lk.ijse.tdm.UserTm;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserFormController {

    @FXML
    private TableColumn<UserTm, Integer> colId;

    @FXML
    private TableColumn<UserTm, String> colUserName;

    @FXML
    private TableColumn<UserTm, String> colRole;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<UserTm> tblUser;

    @FXML
    private Label lblUserId;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtUserPassword;

    @FXML
    private JFXComboBox<String> cmbUserRole;

    @FXML
    private JFXButton btnUserSave;

    @FXML
    private JFXButton btnUserUpdate;

    @FXML
    private JFXButton btnUserDelete;


    UserBO userBO  = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);



    public void initialize() {
        generateNewUserID();
        setValuesForRoleCmb();
        setCellValueFactory();
        loadAllUsers();
        showSelectedUserDetails();
        buttonsDisableAndEnable();
        clickEnterButtonMoveCursor();
    }



    void clickEnterButtonMoveCursor() {
        // username text field eka type krl enter karahama cursor eka password field ekata yanawa
        txtUserName.setOnAction(event -> txtUserPassword.requestFocus());
    }

    void buttonsDisableAndEnable() {
        btnUserUpdate.setDisable(true);
        btnUserSave.setDisable(false);
        btnUserDelete.setDisable(true);
    }

    private void showSelectedUserDetails() {
        UserTm selectedUser = tblUser.getSelectionModel().getSelectedItem();
        tblUser.setOnMouseClicked(event -> showSelectedUserDetails());
        if (selectedUser != null) {
            lblUserId.setText(String.valueOf(selectedUser.getId()));
            txtUserName.setText(selectedUser.getUserName());
            cmbUserRole.setValue(selectedUser.getRole());

            btnUserUpdate.setDisable(false);
            btnUserSave.setDisable(true);

            btnUserDelete.setDisable(false);
        }
    }

    private void loadAllUsers() {
        tblUser.getItems().clear();
        try {
            /*Get all Users*/
            ArrayList<UserDTO> allUsers = userBO.getAllUsers();

            for (UserDTO userDTO : allUsers) {
                tblUser.getItems().add(new UserTm(userDTO.getUserId(), userDTO.getUsername(), userDTO.getRole()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    void setValuesForRoleCmb() {
        ObservableList<String> role = FXCollections.observableArrayList();

        role.add("admin");
        role.add("admissions coordinator");

        cmbUserRole.setItems(role);
    }

    private void generateNewUserID() {
        try {
            String nextUserId = userBO.generateNewID();

            lblUserId.setText(String.valueOf(nextUserId));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }
    void clearFields() {
        txtUserName.setText(null);
        txtUserPassword.setText(null);
        cmbUserRole.setValue(null);
        lblUserId.setText(null);
        generateNewUserID();
        initialize();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        int id = Integer.parseInt(lblUserId.getText());

        try {
            boolean isDeleted = userBO.deleteUser(id);
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
    void btnSaveOnAction(ActionEvent event) {
        int id = Integer.parseInt(lblUserId.getText());
        String userName = txtUserName.getText();
        String password = txtUserPassword.getText();
        String role = cmbUserRole.getValue();

        // Hash the password with bcrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a UserDTO with the hashed password
        UserDTO userDTO = new UserDTO(id, userName, hashedPassword, role);

        if (userName.isEmpty() || password.isEmpty() || hashedPassword.isEmpty() || cmbUserRole.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        } else {
            try {
                boolean isSaved = userBO.saveUser(userDTO);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User saved!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException | ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        int id = Integer.parseInt(lblUserId.getText());
        String userName = txtUserName.getText();
        String password = txtUserPassword.getText();
        String role = cmbUserRole.getValue();

        // Create a UserDTO with the hashed password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a UserDTO with the hashed password
        UserDTO userDTO = new UserDTO(id, userName, hashedPassword, role);

        if (userName.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty!").show();
        } else {
            try {
                boolean isUpdated = userBO.updateUser(userDTO);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User updated!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
