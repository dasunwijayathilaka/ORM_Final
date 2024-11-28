package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.config.SessionFactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Session session;
        try {
            session = SessionFactoryConfiguration.getInstance().getSession();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Transaction transaction = session.beginTransaction();
        transaction.commit();
        session.close();


        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"))));
        stage.setTitle("The Culinary Academy");
        stage.centerOnScreen();
        stage.show();
    }
}
