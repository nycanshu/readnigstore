package org.example.readnigstore.Controller;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.readnigstore.Helper.AlertBoxes;
import org.example.readnigstore.Model.Book;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Service.BookService;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardView {
    public Button logoutButton;
    public Label adminNameLabel;
    public Button addBookButton;
    public ScrollPane booksScrollPane;
    public VBox booksList;

    private User user;
    private List<Book> books = new ArrayList<>();
    private BookService bookService = new BookService();

    // Initialize the admin view
    public void init(User user) {
        this.user = user;
        adminNameLabel.setText("Welcome, " + user.getFirstName());
        getBooks();
        populateBooksList();
    }

    // Get all books
    public void getBooks() {
        books = bookService.getAllBooks();
    }

    // Logout action
    public void handleLogout(ActionEvent actionEvent) {
        System.exit(0);
    }

    // Add book action
    public void handleAddBook(ActionEvent actionEvent) {
        showAddBookDialog();
    }

    // Populate the books list in the admin dashboard
    private void populateBooksList() {
        booksList.getChildren().clear();
        for (Book book : books) {
            HBox bookTile = createBookTileUI(book);
            booksList.getChildren().add(bookTile);
        }
    }

    // Create a book tile UI for each book entry
    private HBox createBookTileUI(Book book) {
        HBox bookBox = new HBox(10);
        bookBox.setPrefWidth(600);
        bookBox.setPrefHeight(100);
        bookBox.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        // VBox for book details (title, author, price, sold copies, remaining stock)
        VBox textBox = new VBox();
        textBox.setSpacing(5);

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Label priceLabel = new Label("Price: $" + book.getPrice());
        priceLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Label soldCopiesLabel = new Label("Sold Copies: " + book.getSoldCopies());
        soldCopiesLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Label stockLabel = new Label("Stock: " + book.getStock());
        stockLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        textBox.getChildren().addAll(titleLabel, authorLabel, priceLabel, soldCopiesLabel, stockLabel);

        // Quantity control buttons
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER_RIGHT);

        Button decreaseButton = new Button("-");
        Label quantityLabel = new Label(String.valueOf(book.getStock()));
        quantityLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Button increaseButton = new Button("+");

        // Update quantity control buttons
        decreaseButton.setOnAction(event -> updateStock(book, book.getStock() - 1, quantityLabel, stockLabel));
        increaseButton.setOnAction(event -> updateStock(book, book.getStock() + 1, quantityLabel, stockLabel));

        // Add quantity controls to quantityBox
        quantityBox.getChildren().addAll(decreaseButton, quantityLabel, increaseButton);

        // Add textBox and quantityBox to bookBox
        bookBox.getChildren().addAll(textBox, quantityBox);
        return bookBox;
    }

    // Update stock for a book and update both quantityLabel and stockLabel in real-time
    private void updateStock(Book book, int newStock, Label quantityLabel, Label stockLabel) {
        if (newStock < 0) return; // Prevent stock from going below 0

        // Update stock in the database
        bookService.updateBookStockById(book.getBookId(), newStock);
        book.setStock(newStock); // Update stock in the model

        // Update UI labels in real-time
        quantityLabel.setText(String.valueOf(newStock));
        stockLabel.setText("Stock: " + newStock);
    }

    // Show a dialog to add a new book
    private void showAddBookDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Book");

        VBox dialogVbox = new VBox(10);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setStyle("-fx-padding: 20;");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField stockField = new TextField();
        stockField.setPromptText("Stock");

        Button addButton = new Button("Add Book");
        addButton.setOnAction(event -> {
            String title = titleField.getText();
            String author = authorField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            Book newBook = new Book(title, author, price, stock);
            bookService.addBook(newBook); // Assuming addBook method in BookService
            books.add(newBook);
            AlertBoxes.showSuccessAlert("Success", "Book added successfully!");
            populateBooksList(); // Refresh the book list UI

            dialog.close(); // Close dialog after adding the book
        });

        dialogVbox.getChildren().addAll(new Label("Enter book details:"), titleField, authorField, priceField, stockField, addButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 250);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
