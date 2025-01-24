package org.example.readnigstore.Service;

import org.example.readnigstore.Helper.DBConnector;
import org.example.readnigstore.Model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    // Method to update the stock of a book
    public void updateBookStock(int bookId, int quantity) {
        String query = "UPDATE books SET stock = stock - ? WHERE book_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating book stock: " + e.getMessage());
        }
    }

    //update book sold copies
    public void updateBookSoldCopies(int bookId, int quantity) {
        String query = "UPDATE books SET sold_copies = sold_copies + ? WHERE book_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating book sold copies: " + e.getMessage());
        }
    }
    //add book to db
    public void addBook(Book book) {
        String query = "INSERT INTO books (title, author, stock, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getStock());
            statement.setDouble(4, book.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    // Method to directly set a new stock level for a book
    public void updateBookStockById(int bookId, int newStock) {
        String query = "UPDATE books SET stock = ? WHERE book_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newStock);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating book stock: " + e.getMessage());
        }
    }
    public List<Book> loadTop5Books() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books ORDER BY sold_copies DESC LIMIT 5";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setStock(resultSet.getInt("stock"));
                book.setPrice(resultSet.getDouble("price"));
                book.setSoldCopies(resultSet.getInt("sold_copies"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading top 5 books: " + e.getMessage());
        }
        return books;
    }

    // Method to get a book by its ID
    public Book getBookById(int bookId){
        Book book = null;
        String query = "SELECT * FROM books WHERE book_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = new Book();
                book.setBookId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setStock(resultSet.getInt("stock"));
                book.setPrice(resultSet.getDouble("price"));
                book.setSoldCopies(resultSet.getInt("sold_copies"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading book by ID: " + e.getMessage());
        }

        return book;
    }

    //get all books from database
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setStock(resultSet.getInt("stock"));
                book.setPrice(resultSet.getDouble("price"));
                book.setSoldCopies(resultSet.getInt("sold_copies"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading all books: " + e.getMessage());
        }
        return books;
    }
}
