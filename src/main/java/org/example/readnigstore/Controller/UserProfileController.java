package org.example.readnigstore.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;
import org.example.readnigstore.Model.OrderDetail;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Service.UserService; // Ensure you have a UserService to handle user-related operations
import org.example.readnigstore.Service.OrderService; // Ensure you have an OrderService to handle orders
import org.example.readnigstore.Model.Order; // Your order model
import javafx.scene.layout.HBox; // For order history entries

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class UserProfileController {

    public Label userNameLabel;
    public Button home;
    public Label usernameLabel;
    public TextField firstNameField;
    public TextField lastNameField;
    public VBox orderHistoryList; // For displaying order history
    public ScrollPane orderHistoryScrollPane;
    public Button exportToCsv;
    public Button saveButton;
    User user;

    private UserService userService; // Service for user operations
    private OrderService orderService; // Service for order operations

    public UserProfileController() {
        userService = new UserService(); // Initialize user service
        orderService = new OrderService(); // Initialize order service
    }

    public void init(User user) {
        this.user = user;
        userNameLabel.setText("Welcome, " + user.getFirstName());
        usernameLabel.setText(user.getUsername());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        loadOrderHistory(); // Load the user's order history
    }

    public void handleSave(ActionEvent actionEvent) {
        // Update user details
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());

        // Call the service to update the user and get the updated user
        User updatedUser = userService.updateUserProfile(user);
        AlertBoxes.showInformationAlert("Profile Updated", "Your profile has been updated successfully.");
        // Refresh the profile with updated user information
        refreshUserProfile(updatedUser);
    }

    private void refreshUserProfile(User updatedUser) {
        this.user = updatedUser;
        userNameLabel.setText("Welcome, " + updatedUser.getFirstName());
        usernameLabel.setText(updatedUser.getUsername());
        firstNameField.setText(updatedUser.getFirstName());
        lastNameField.setText(updatedUser.getLastName());
        loadOrderHistory(); // Refresh order history if needed
    }

    public void handleLogout() {
        System.exit(0);
    }

    public void goToHome() {
        // Implement navigation to home
        sendToUserDashboard(user);
    }
    public void sendToUserDashboard(User user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/user-home-page.fxml"));
            Parent root = loader.load();


            UserHomePageController controller = loader.getController();
            controller.init(user);


            Stage stage = (Stage)  home.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load dashboard", "An error occurred while trying to load the user dashboard.");
        }
    }

    private void loadOrderHistory() {
        orderHistoryList.getChildren().clear(); // Clear any previous entries

        // Retrieve user's orders
        List<Order> orders = orderService.getUserOrders(user.getUserId());

        for (Order order : orders) {
            HBox orderEntry = new HBox(10); // Create a horizontal box for each order entry
            orderEntry.setSpacing(10);
            orderEntry.setStyle("-fx-padding: 10; -fx-background-color: #e6e6e6; -fx-border-color: #999; -fx-border-radius: 5; -fx-background-radius: 5;");

            Label orderLabel = new Label("Order ID: " + order.getOrderId() + ",\nPrice: $" + order.getTotalPrice());
            orderLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;"); // Style for order ID
            orderEntry.getChildren().add(orderLabel);

            // Create a VBox to hold all order items
            VBox orderItemsBox = new VBox(5); // Vertical spacing between items

            // Add details for each book in the order
            for (OrderDetail detail : order.getOrderDetails()) {
                orderItemsBox.getChildren().add(createOrderItemUI(detail));
            }

            // Add order items box to the order entry
            orderEntry.getChildren().add(orderItemsBox);
            orderHistoryList.getChildren().add(orderEntry); // Add order entry to the list
        }
    }

    private HBox createOrderItemUI(OrderDetail orderDetail) {
        // Main container for the order item
        HBox bookBox = new HBox(10);
        bookBox.setPrefWidth(400); // Set preferred width to accommodate elements
        bookBox.setPrefHeight(80); // Adjust height for order items
        bookBox.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        // VBox for title and author
        VBox textBox = new VBox();
        textBox.setSpacing(5); // Space between title and author

        // Create labels for the book title and author
        Label titleLabel = new Label(orderDetail.getBook().getTitle());
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        titleLabel.setPrefWidth(200); // Fixed width for title

        Label authorLabel = new Label(orderDetail.getBook().getAuthor()); // Assuming Book model has a getAuthor method
        authorLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;"); // Style for the author
        authorLabel.setPrefWidth(200); // Fixed width for author

        // Add title and author labels to the VBox
        textBox.getChildren().addAll(titleLabel, authorLabel);

        // Label for quantity
        Label quantityLabel = new Label("Quantity: " + orderDetail.getQuantity());
        quantityLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        // Add title, author, and quantity to the main box
        bookBox.getChildren().addAll(textBox, quantityLabel);

        return bookBox;
    }

    public void exportToCsv() {
        // Step 1: Prompt user for the file name and path using FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Order History");

        // Set extension filter for CSV files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(exportToCsv.getScene().getWindow());

        if (file != null) {
            // Step 2: Generate CSV content from order history
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Order ID,Order Date,Total Price,Book Title,Author,Quantity\n");

            // Get all orders and append them to CSV content
            List<Order> orders = orderService.getUserOrders(user.getUserId());
            for (Order order : orders) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    csvContent.append(order.getOrderId()).append(",")
                            .append(order.getOrderDate()).append(",") // Added order date
                            .append(order.getTotalPrice()).append(",")
                            .append(detail.getBook().getTitle()).append(",")
                            .append(detail.getBook().getAuthor()).append(",")
                            .append(detail.getQuantity()).append("\n");
                }
            }

            // Step 3: Write CSV content to the selected file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(csvContent.toString());
                AlertBoxes.showInformationAlert("Export Successful", "Order history has been exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                AlertBoxes.showErrorAlert("Export Failed", "An error occurred while exporting the order history to CSV.");
            }
        }
    }

}
