package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Registration_Controller {

    @FXML
    private JFXComboBox<?> cmbProgramName;

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
    private TableView<?> tblRegistration;

    @FXML
    private TextField txtFirstPayment;

    @FXML
    private TextField txtStudentId;

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnViewRegistrationDetails(ActionEvent event) {

    }

    @FXML
    void cmbProgramNameOnAction(ActionEvent event) {

    }

}
