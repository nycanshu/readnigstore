package org.example.readnigstore.Controller.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;

import java.io.IOException;

public class SignupPageViewController {
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField confirmPasswordField;
    public Button loginPageBack;

    public void backToLogin() {
        //back to login page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/main-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)  loginPageBack.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to Login Page", "An error occurred while trying to load login page.");
        }
    }
}
