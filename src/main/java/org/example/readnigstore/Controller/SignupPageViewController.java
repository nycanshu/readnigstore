package org.example.readnigstore.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Service.UserService;

import java.io.IOException;

public class SignupPageViewController {
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField confirmPasswordField;
    public Button loginPageBack;



    UserService userService = new UserService();

    public void createAnAccount() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertBoxes.showErrorAlert("Empty Fields", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertBoxes.showErrorAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        User user = new User(firstName, lastName, username, password,false);

        if (userService.addUser(user)) {
            AlertBoxes.showInformationAlert("Account Created", "Account created successfully. Plase login to continue.");
            backToLogin();
        } else {
            AlertBoxes.showErrorAlert("Account Creation Failed", "An error occurred while trying to create account.");
        }

    }

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
