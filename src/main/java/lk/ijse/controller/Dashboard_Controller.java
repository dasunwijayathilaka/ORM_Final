package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.entity.UserSession;

import java.io.IOException;

public class Dashboard_Controller {

    @FXML
    private AnchorPane nodePane;

    @FXML
    private AnchorPane rootNode;


    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnUser;

    @FXML
    private JFXButton btnPrograms;

    int userId = UserSession.getInstance().getUserId();
    String role = UserSession.getInstance().getRole();


    // Initialize method to load the main dashboard after a delay
    public void initialize() {
//         Load mainDashboard_form.fxml after a 1-second delay
        PauseTransition delay = new PauseTransition(Duration.seconds(0.0001));
        delay.setOnFinished(event -> loadMainDashboard());
        delay.play();

//        dButton.setStyle("-fx-background-color: #192a51");

        // Set action listeners for each button in the VBox
//        setButtonActions();


        checkLoggedUser();
    }


    void checkLoggedUser() {
        if (role.equals("admissions coordinator")){
            btnUser.setVisible(false);
            btnPrograms.setVisible(false);
        }
    }



    // Method to load the main dashboard
    private void loadMainDashboard() {
        try {
            // Load mainDashboard_form.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/analytics_dashboard.fxml"));
            AnchorPane mainDashboard = loader.load();

            // Replace the children of rootNode with the loaded content
            rootNode.getChildren().setAll(mainDashboard);
            Stage stage = (Stage) rootNode.getScene().getWindow();
            stage.setTitle("Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/view/analytics_dashboard.fxml"));
        Parent productRoot = productLoader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(productRoot);

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("Dashboard");
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) throws IOException {

        // Load the login form
        Parent loginForm = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(loginForm);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("The Culinary Academy");
    }


    @FXML
    void btnProgramsOnAction(ActionEvent event) throws IOException {
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/view/programs_form.fxml"));
        Parent productRoot = productLoader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(productRoot);

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("");
    }

    @FXML
    void btnRegistrationOnAction(ActionEvent event) throws IOException {
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/view/registration_form.fxml"));
        Parent productRoot = productLoader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(productRoot);

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("");
    }

    @FXML
    void btnStudentsOnAction(ActionEvent event) throws IOException {
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/view/student_form.fxml"));
        Parent productRoot = productLoader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(productRoot);

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("");
    }

    @FXML
    void btnUser(ActionEvent event) throws IOException {
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/view/user_form.fxml"));
        Parent productRoot = productLoader.load();
        rootNode.getChildren().clear();
        rootNode.getChildren().add(productRoot);

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setTitle("");
    }

}
