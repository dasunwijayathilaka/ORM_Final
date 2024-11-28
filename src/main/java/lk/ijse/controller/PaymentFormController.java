package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.Util.Regex;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.tdm.PaymentRegTM;
import lk.ijse.tdm.PaymentTM;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentFormController {

    @FXML
    private JFXComboBox<String> cmbPaymentMethod;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colPaidAmount;

    @FXML
    private TableColumn<?, ?> colPaymentDate;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableColumn<?, ?> colPaymentMethod;

    @FXML
    private TableColumn<?, ?> colProgramId;

    @FXML
    private TableColumn<?, ?> colProgramName;

    @FXML
    private TableColumn<?, ?> colRegId;

    @FXML
    private TableColumn<?, ?> colRegistrationId;

    @FXML
    private TableColumn<?, ?> colStudentId;

    @FXML
    private TableColumn<?, ?> colStudentName;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblPaidAmount;

    @FXML
    private Label lblPaymentDate;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblProgramFee;

    @FXML
    private Label lblProgramName;

    @FXML
    private Label lblRegId;

    @FXML
    private Label lblStudentId;

    @FXML
    private TableView<PaymentTM> tblPayment;

    @FXML
    private TableView<PaymentRegTM> tblRegistration;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtStudentSearch;

    PaymentBO paymentBO  = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);
    StudentBO studentBO  = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);


    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        lblPaymentDate.setText(LocalDate.now().toString());
        generateNewPaymentId();
        setPaymentType();
        setCellValueFactory();
        clickEnterButtonMoveCursor();
        showSelectedUserDetails();

        // Ensures the cursor goes to txtStudentSearch when the payment UI is opened
        Platform.runLater(() -> txtStudentSearch.requestFocus());

    }



    private void showSelectedUserDetails() throws SQLException, IOException, ClassNotFoundException {
        PaymentRegTM selectedUser = tblRegistration.getSelectionModel().getSelectedItem();
        tblRegistration.setOnMouseClicked(event -> {
            try {
                showSelectedUserDetails();
            } catch (SQLException | IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        if (selectedUser != null) {
            lblRegId.setText(String.valueOf(selectedUser.getRegistrationId()));
            lblStudentId.setText(String.valueOf(selectedUser.getStudentId()));
            lblProgramFee.setText(String.valueOf(selectedUser.getFee()));
            lblProgramName.setText(selectedUser.getProName());
            lblPaidAmount.setText(String.valueOf(selectedUser.getPaidAmount()));

            double balance = selectedUser.getFee() - selectedUser.getPaidAmount();
            lblBalance.setText(String.format("%.2f", balance));

            txtAmount.requestFocus();

            loadPaymentDetails();

        }
    }

    void loadPaymentDetails() {
        try {

            tblPayment.getItems().clear();
            // Fetch payment details using the registration ID
            ArrayList<PaymentDTO> paymentList = paymentBO.getPaymentDetails(Integer.parseInt(lblRegId.getText()));

            for (PaymentDTO paymentDTO : paymentList) {
                tblPayment.getItems().add(new PaymentTM(
                        paymentDTO.getPaymentId(),
                        paymentDTO.getRegistration().getRegId(),
                        paymentDTO.getAmount(),
                        paymentDTO.getPaymentDate(),
                        paymentDTO.getPaymentMethod()
                ));
            }

        } catch (SQLException | ClassNotFoundException | IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    void clickEnterButtonMoveCursor() {
//        txtUserName.setOnAction(event -> txtPassword.requestFocus());
        txtStudentSearch.setOnAction(event -> {
            try {
                try {
                    btnSearchOnAction(new ActionEvent());
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setCellValueFactory() {
        colRegId.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("proId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("proName"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));


        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colRegistrationId.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

    }

    private void generateNewPaymentId() {
        try {
            String nextStudentId = paymentBO.generateNewID();

            lblPaymentId.setText(nextStudentId);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPaymentType() {
        ObservableList<String> paymentType = FXCollections.observableArrayList();
        cmbPaymentMethod.setValue("Cash");

        paymentType.add("Cash");
        paymentType.add("Card");

        cmbPaymentMethod.setItems(paymentType);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        ObservableList<PaymentRegTM> registrationDetailsList = FXCollections.observableArrayList();

        String id = txtStudentSearch.getText();

        if (!txtStudentSearch.getText().isEmpty()) {

            if (isSearchIdValied()) {
                List<Object[]> regDetails = studentBO.studentSearchForPayment(Integer.parseInt(id));
                if (regDetails != null) {

                    for (Object[] row : regDetails) {
                        int regId = (Integer) row[0];
                        double paidAmount = (Double) row[1];
                        String programId = (String) row[2];
                        int studentId = (Integer) row[3];
                        String studentName = (String) row[4];
                        double programFee = (Double) row[5];
                        String programName = (String) row[6];

                        PaymentRegTM details = new PaymentRegTM(regId, studentId, studentName, programId, programName, paidAmount, programFee);
                        registrationDetailsList.add(details);
                    }

                    tblRegistration.getItems().clear();
                    tblRegistration.getItems().addAll(registrationDetailsList);

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

    @FXML
    void idKeyReleaseAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.Util.TextField.INTID, txtStudentSearch);
    }

    @FXML
    void amountKeyReleaseOnAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.Util.TextField.PRICEDOT, txtAmount);
    }

    public boolean isSearchIdValied() {
        return Regex.setTextColor(lk.ijse.Util.TextField.INTID, txtStudentSearch);
    }

    @FXML
    public void btnClearOnAction(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        clearFields();
    }

    void clearFields() throws SQLException, IOException, ClassNotFoundException {
        txtStudentSearch.setText(null);
        txtStudentSearch.setStyle("");

        txtAmount.setText(null);
        txtAmount.setStyle("");

        tblRegistration.getItems().clear();

        lblRegId.setText(null);
        lblStudentId.setText(null);
        lblProgramName.setText(null);
        lblProgramFee.setText(null);
        lblPaidAmount.setText(null);
        lblBalance.setText(null);

        tblPayment.getItems().clear();

        initialize();
    }

    void refreshPageMethod(double amount) throws SQLException, IOException, ClassNotFoundException {
        loadPaymentDetails();
        btnSearchOnAction(new ActionEvent());

        lblPaidAmount.setText(String.valueOf(amount));

        double fee = Double.parseDouble(lblProgramFee.getText());
        double paid = Double.parseDouble(lblPaidAmount.getText());
        double balance = fee - paid;

        lblBalance.setText(String.valueOf(balance));;

        txtAmount.setText(null);
        txtAmount.setStyle(null);
    }

    @FXML
    public void btnPayOnAction(ActionEvent actionEvent) {
        try {

            int payId = Integer.parseInt(lblPaymentId.getText());
            double paidAmount = Double.parseDouble(txtAmount.getText());
            Date payDate = Date.valueOf(lblPaymentDate.getText());
            String payMethod = cmbPaymentMethod.getValue();
            int regId = Integer.parseInt(lblRegId.getText());

            // Check if any required field is missing
            if (regId < 1 || paidAmount < 1) {
                new Alert(Alert.AlertType.ERROR, "Please fill all the fields correctly!").show();
                return;
            }

            double a = Double.parseDouble(lblPaidAmount.getText());

            // registration table eke thiyena paidamount ekata aluthin pay karan amount eka ekathu kranawa
            double amount = paidAmount + a;

            RegistrationDTO registrationDTO = new RegistrationDTO(regId, amount);
            PaymentDTO paymentDTO = new PaymentDTO(payId, new RegistrationDTO(regId), paidAmount, payDate, payMethod);

            // Save registration and handle the result
            boolean isSaved = paymentBO.savePayment(paymentDTO, registrationDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Successful!").show();
                refreshPageMethod(amount);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save the payment").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format! Please check your inputs.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Please check all fields entered correctly!").show();
        }

    }
}
