package org.example.readnigstore.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Service.UserService;

import java.io.IOException;

public class MainController {
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField confirmPasswordField;
    public Button signupOpen;
    public Button login;
    @FXML
    private Label welcomeText;



    UserService userService = new UserService();
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.validateLogin(username, password);
        if(user!=null) {
            if (user.isAdmin()) {
                sendToAdminDashboard(user);
            } else {
                sendToUserDashBoadrd(user);
            }
        }else {
            AlertBoxes.showErrorAlert("Login Failed", "Invalid username or password.");
        }
    }

    public void sendToSignUpPage(ActionEvent actionEvent) {
        //open sign up page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/signup-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)  signupOpen.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load dashboard", "An error occurred while trying to load the user dashboard.");
        }
    }

    public void sendToAdminDashboard(User user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/admin-dashboard-view.fxml"));
            Parent root = loader.load();

            AdminDashboardView controller = loader.getController();
            controller.init(user);


            Stage stage = (Stage)  login.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load dashboard", "An error occurred while trying to load the Admin dashboard.");
        }
    }

    public void sendToUserDashBoadrd(User user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/user-home-page.fxml"));
            Parent root = loader.load();

            //passing login user to next page
            UserHomePageController controller = loader.getController();
            controller.init(user);

            Stage stage = (Stage)  login.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load dashboard", "An error occurred while trying to load the user dashboard.");
        }
    }
}