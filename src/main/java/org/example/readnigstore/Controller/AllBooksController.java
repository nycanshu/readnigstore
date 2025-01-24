package org.example.readnigstore.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;
import org.example.readnigstore.Model.Book;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Service.BookService;
import org.example.readnigstore.Service.ShoppingCartService;

import java.io.IOException;
import java.util.List;

public class AllBooksController {
    public Button home;
    public Button myprofile;
    public Button mycart;
    public Label userNameLabel;
    public TilePane popularBooksList;

    User user;

    BookService bookService = new BookService();
    ShoppingCartService shoppingCartService = new ShoppingCartService();

    public void init(User user){
        this.user = user;
        userNameLabel.setText("Welcome, " + user.getFirstName());
        pupulateAllBooks();
    }


    private void pupulateAllBooks() {
        List<Book> popularBooks = bookService.getAllBooks(); // Retrieve the top 5 popular books

        // Clear existing children in case this method is called multiple times
        popularBooksList.getChildren().clear();

        for (Book book : popularBooks) {
            VBox bookUI = createBookUI(book);
            popularBooksList.getChildren().add(bookUI); // Add book UI to the popular books list
        }
    }

    private VBox createBookUI(Book book) {
        VBox bookBox = new VBox();
        bookBox.setPrefWidth(200);
        bookBox.setPrefHeight(250);
        bookBox.setSpacing(10); // Space between elements
        bookBox.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;"); // Add background color and border

        // Create image view for the book cover
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/open-book.png")));
        imageView.setFitWidth(200);
        imageView.setFitHeight(250); // Set height for the image
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        imageView.setSmooth(true); // Smooth the image rendering

        // Create labels for the title and author
        Label titleLabel = new Label(book.getTitle()); // Assuming Book model has a getTitle() method
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-wrap-text: true;"); // Style for the title
        titleLabel.setWrapText(true); // Allow wrapping if the title is long

        Label authorLabel = new Label("by " + book.getAuthor()); // Assuming Book model has a getAuthor() method
        authorLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #555;"); // Style for the author

        // Create "Add to Cart" button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e); -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 100;"); // Gradient color
        addToCartButton.setOnAction(event -> addToCart(book)); // Handle add to cart action


        // Create a HBox to hold the button
        HBox hBox = new HBox();
        hBox.setSpacing(5); // Space between elements in HBox
        hBox.getChildren().addAll(addToCartButton); // Add only the button here for alignment

        // Add all elements to the VBox
        bookBox.getChildren().addAll(imageView, titleLabel, authorLabel, hBox);
        return bookBox;
    }

    private void addToCart(Book book) {
        // Implement add to cart logic here
        shoppingCartService.addToCart(user, book);
        AlertBoxes.showInformationAlert("Book Added", book.getTitle() + " added to cart.");
        System.out.println(book.getTitle() + " added to cart."); // Placeholder for actual logic
    }



    // Implement navigation to home


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

            Stage stage = (Stage)  home.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Profile");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load Profile Page", "An error occurred while trying to load the Profile dashboard.");
        }
    }

    public void goToCart(ActionEvent actionEvent) {
        // Implement navigation to cart
        sendToCartPage(user);
    }

    private void sendToCartPage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/readnigstore/user-cart-page.fxml"));
            Parent root = loader.load();

            UserCartController controller = loader.getController();
            controller.init(user);
            Stage stage = (Stage)  home.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Your cart");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            AlertBoxes.showErrorAlert("Failed to load your cart", "An error occurred while trying to loading your cart.");
        }
    }


    public void handleLogout() {
        System.exit(0);
    }
}
