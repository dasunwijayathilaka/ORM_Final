package lk.ijse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.bo.custom.ProgramBO;
import lk.ijse.bo.custom.RegistrationBO;
import lk.ijse.bo.custom.StudentBO;

import java.sql.SQLException;

public class AnalyticsDashboard {

    @FXML
    private Label lblProgramCount;

    @FXML
    private Label lblRegCount;

    @FXML
    private Label lblStudentCount;

    @FXML
    private Label lblStudentCount21;

    @FXML
    private Label lblTransactionsCount;

    @FXML
    private AnchorPane rootNode;


    StudentBO studentBO  = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    ProgramBO programBO  = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAM);
    RegistrationBO registrationBO  = (RegistrationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REGISTRATION);
    PaymentBO paymentBO  = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);



    public void initialize() throws SQLException, ClassNotFoundException {
        loadStudentCount();
        loadProgramCount();
        loadRegistrationCount();
        loadSumOfTransactionsAmount();
    }





    private void loadStudentCount() throws SQLException, ClassNotFoundException {
        lblStudentCount.setText(studentBO.getStudentCount());
    }

    private void loadProgramCount() throws SQLException, ClassNotFoundException {
        lblProgramCount.setText(programBO.getProgramCount());
    }

    private void loadRegistrationCount() throws SQLException, ClassNotFoundException {
        lblRegCount.setText(registrationBO.getRegistrationCount());
    }

    private void loadSumOfTransactionsAmount() throws SQLException, ClassNotFoundException {
        lblTransactionsCount.setText(paymentBO.getSumOfTransactionsAmount());
    }

}
