package org.example.readnigstore.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;
import org.example.readnigstore.Helper.Validators;
import org.example.readnigstore.Model.*;
import org.example.readnigstore.Service.OrderService;
import org.example.readnigstore.Service.ShoppingCartService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserCartController {
    @FXML
    public Label userNameLabel;
    @FXML
    public VBox cartItemsList;
    @FXML
    public Button checkoutButton;
    public Button home;
    public Button profile;
    public Button allbook;


    private User user;
    private ShoppingCartService shoppingCartService;
    OrderService orderService = new OrderService();

    public void init(User user) {
        this.user = user;
        this.shoppingCartService = new ShoppingCartService();
        userNameLabel.setText(user.getFirstName() + "'s Cart");
        populateCartItems(); // Load cart items when initializing
    }

    private void populateCartItems() {
        List<ShoppingCart> cartItems = shoppingCartService.getCartItems(user); // Retrieve user's cart items

        cartItemsList.getChildren().clear(); // Clear previous items

        for (ShoppingCart cartItem : cartItems) {
            HBox bookUI = createCartItemUI(cartItem);
            cartItemsList.getChildren().add(bookUI); // Add book UI to the cart items list
        }
    }

    private HBox createCartItemUI(ShoppingCart cartItem) {
        // Main container for the cart item
        HBox bookBox = new HBox(10);
        bookBox.setPrefWidth(400); // Set preferred width to accommodate elements
        bookBox.setPrefHeight(80); // Adjust height for cart items
        bookBox.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        // VBox for title and author
        VBox textBox = new VBox();
        textBox.setSpacing(5); // Space between title and author

        // Create labels for the book title and author
        Label titleLabel = new Label(cartItem.getBook().getTitle());
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        titleLabel.setPrefWidth(200); // Fixed width for title

        Label authorLabel = new Label(cartItem.getBook().getAuthor()); // Assuming Book model has a getAuthor method
        authorLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;"); // Style for the author
        authorLabel.setPrefWidth(200); // Fixed width for author

        // Add title and author labels to the VBox
        textBox.getChildren().addAll(titleLabel, authorLabel);

        // HBox for quantity controls
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER_RIGHT); // Aligns quantity controls to the right
        quantityBox.setSpacing(10); // Space between buttons and quantity label

        Button decreaseButton = new Button("-");
        Button increaseButton = new Button("+");
        Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
        quantityLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;"); // Style for quantity label

        // Set fixed widths for quantity controls
        decreaseButton.setPrefWidth(30);
        increaseButton.setPrefWidth(30);
        quantityLabel.setPrefWidth(30); // Fixed width for quantity label

        // Handle quantity change
        decreaseButton.setOnAction(event -> updateQuantity(cartItem, cartItem.getQuantity() - 1));
        increaseButton.setOnAction(event -> updateQuantity(cartItem, cartItem.getQuantity() + 1));

        // Add quantity controls to the HBox
        quantityBox.getChildren().addAll(decreaseButton, quantityLabel, increaseButton);

        // Create a remove button
        Button removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold;");
        removeButton.setPrefWidth(80); // Fixed width for remove button
        removeButton.setOnAction(event -> removeFromCart(cartItem)); // Handle remove action

        // Add all elements to the main HBox
        // Create a separate HBox for actions
        HBox actionBox = new HBox(10); // Spacing between action elements
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.getChildren().addAll(quantityBox, removeButton);

        // Add textBox and actionBox to the main bookBox
        bookBox.getChildren().addAll(textBox, actionBox);
        return bookBox;
    }


    private void updateQuantity(ShoppingCart cartItem, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(cartItem);
        } else {
            shoppingCartService.updateCartItemQuantity(cartItem.getCartId(), newQuantity); // Update quantity in backend
            cartItem.setQuantity(newQuantity); // Update local cart item quantity
            populateCartItems(); // Refresh displayed items
        }
    }

    private void removeFromCart(ShoppingCart cartItem) {
        AlertBoxes.showInformationAlert("Are you sure?", "Do you want to remove this item from cart?");

        shoppingCartService.removeFromCart(cartItem,user); // Call the service to remove the item

        populateCartItems(); // Refresh the cart items display
        System.out.println(cartItem.getBook().getTitle() + " removed from cart.");
    }

    @FXML
    private void handleCheckout(ActionEvent actionEvent) {
        Stage checkoutStage = new Stage();
        checkoutStage.setTitle("Checkout");
        checkoutStage.initModality(Modality.APPLICATION_MODAL);
        checkoutStage.setWidth(500);
        checkoutStage.setHeight(300);

        HBox checkoutLayout = new HBox(20);
        checkoutLayout.setPadding(new javafx.geometry.Insets(20));

        VBox summaryBox = createOrderSummaryBox();
        VBox cardDetailsBox = createCardDetailsBox();

        checkoutLayout.getChildren().addAll(summaryBox, cardDetailsBox);
        Scene checkoutScene = new Scene(checkoutLayout);
        checkoutStage.setScene(checkoutScene);
        checkoutStage.show();
    }

    private VBox createOrderSummaryBox() {
        VBox summaryBox = new VBox(10);
        Label orderSummaryLabel = new Label("Order Summary");
        ListView<String> orderItemsList = new ListView<>();

        // Add order items to the list
        for (ShoppingCart cartItem :shoppingCartService.getCartItems(user)) {
            String itemDetail = cartItem.getBook().getTitle() + " - Qty: " + cartItem.getQuantity() + " - Price: $" + (cartItem.getBook().getPrice() * cartItem.getQuantity());
            orderItemsList.getItems().add(itemDetail);
        }

        double totalPrice = shoppingCartService.calculateTotalPrice(user);
        Label totalPriceLabel = new Label("Total Price: $" + totalPrice);
        summaryBox.getChildren().addAll(orderSummaryLabel, orderItemsList, totalPriceLabel);

        return summaryBox;
    }
    private VBox createCardDetailsBox() {
        VBox cardDetailsBox = new VBox(10);
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card Number (16 digits)");
        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("Expiry Date (MM/YY)");
        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV (3 digits)");
        Button confirmButton = new Button("Confirm Order");

        confirmButton.setOnAction(e -> {
            if (Validators.validateCardDetails(cardNumberField.getText(), expiryDateField.getText(), cvvField.getText())) {
               processOrder(cardNumberField.getText(), expiryDateField.getText(), cvvField.getText(), shoppingCartService.calculateTotalPrice(user));
                ((Stage) cardDetailsBox.getScene().getWindow()).close(); // Close the checkout window
            } else {
                AlertBoxes.showErrorAlert("Invalid card details." ,"Please check and try again.");
            }
        });

        cardDetailsBox.getChildren().addAll(cardNumberField, expiryDateField, cvvField, confirmButton);
        return cardDetailsBox;
    }
    private void processOrder(String cardNumber, String expiryDate, String cvv, double totalPrice) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new java.util.Date());
        order.setTotalPrice(totalPrice);
        order.setPaymentCardNumber(cardNumber);
        order.setPaymentExpiryDate(expiryDate);
        order.setPaymentCVV(cvv);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cartItem : shoppingCartService.getCartItems(user)) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(cartItem.getBook());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getBook().getPrice());
            orderDetails.add(orderDetail);
        }

        // Use OrderService to place the order
        OrderService orderService = new OrderService();
        orderService.placeOrder(order, orderDetails);

        shoppingCartService.clearCart(user); // Optionally clear the cart after placing the order
        populateCartItems(); // Refresh the cart items display
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

    public void goToBooks(ActionEvent actionEvent) {
        // Implement navigation to books
        sendToAllBooksPage(user);
    }

    private void sendToAllBooksPage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/all-books-view.fxml"));
            Parent root = loader.load();

            AllBooksController controller = loader.getController();
            controller.init(user);

            Stage stage = (Stage)  allbook.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("All Books In Store");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load Books Page", "An error occurred while trying to load the Books dashboard.");
        }

    }

    public void goToProfile(ActionEvent actionEvent) {
        // Implement navigation to profile
        sendToProfilePage(user);
    }

    private void sendToProfilePage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/user-profile-page.fxml"));
            Parent root = loader.load();

            UserProfileController controller = loader.getController();
            controller.init(user);

            Stage stage = (Stage)  profile.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Profile");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load Profile Page", "An error occurred while trying to load the Profile dashboard.");
        }
    }


    public void handleLogout(ActionEvent actionEvent) {
        // Implement logout logic
        System.exit(0);
    }
}
