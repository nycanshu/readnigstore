package com.example.readningstore;

import org.example.readnigstore.Model.Book;
import org.example.readnigstore.Service.BookService;
import org.example.readnigstore.Helper.DBConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookService bookService;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        bookService = new BookService(); // Initialize BookService
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock the static method of DBConnector
        Mockito.mockStatic(DBConnector.class);
        when(DBConnector.getConnection()).thenReturn(mockConnection);

        // Mock the prepared statement creation
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
    }

    @Test
    public void testAddBook() throws SQLException {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setStock(10);
        book.setPrice(29.99);

        // Call the addBook method
        bookService.addBook(book);

        // Verify interactions
        verify(mockConnection).prepareStatement("INSERT INTO books (title, author, stock, price) VALUES (?, ?, ?, ?)");
        verify(mockStatement).setString(1, "Test Title");
        verify(mockStatement).setString(2, "Test Author");
        verify(mockStatement).setInt(3, 10);
        verify(mockStatement).setDouble(4, 29.99);
        verify(mockStatement).executeUpdate();

        // Print result
        System.out.println("testAddBook passed.");
    }

    @Test
    public void testUpdateBookStock() throws SQLException {
        int bookId = 1;
        int quantity = 5;

        // Call the updateBookStock method
        bookService.updateBookStock(bookId, quantity);

        // Verify interactions
        verify(mockConnection).prepareStatement("UPDATE books SET stock = stock - ? WHERE book_id = ?");
        verify(mockStatement).setInt(1, quantity);
        verify(mockStatement).setInt(2, bookId);
        verify(mockStatement).executeUpdate();

        // Print result
        System.out.println("testUpdateBookStock passed.");
    }

    @Test
    public void testUpdateBookSoldCopies() throws SQLException {
        int bookId = 1;
        int quantity = 3;

        // Call the updateBookSoldCopies method
        bookService.updateBookSoldCopies(bookId, quantity);

        // Verify interactions
        verify(mockConnection).prepareStatement("UPDATE books SET sold_copies = sold_copies + ? WHERE book_id = ?");
        verify(mockStatement).setInt(1, quantity);
        verify(mockStatement).setInt(2, bookId);
        verify(mockStatement).executeUpdate();

        // Print result
        System.out.println("testUpdateBookSoldCopies passed.");
    }

    @Test
    public void testGetBookById() throws SQLException {
        int bookId = 1;

        // Set up mock result set
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement("SELECT * FROM books WHERE book_id = ?")).thenReturn(mockStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("book_id")).thenReturn(bookId);
        when(mockResultSet.getString("title")).thenReturn("Test Title");
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getInt("stock")).thenReturn(10);
        when(mockResultSet.getDouble("price")).thenReturn(29.99);
        when(mockResultSet.getInt("sold_copies")).thenReturn(5);

        // Call the getBookById method
        Book book = bookService.getBookById(bookId);

        // Verify interactions
        verify(mockConnection).prepareStatement("SELECT * FROM books WHERE book_id = ?");
        verify(mockStatement).setInt(1, bookId);
        verify(mockStatement).executeQuery();

        // Print results
        if (book != null) {
            System.out.println("testGetBookById passed. Retrieved book: " + book.getTitle());
        } else {
            System.out.println("testGetBookById failed. No book found.");
        }
    }

    @Test
    public void testLoadTop5Books() throws SQLException {
        // Set up mock result set
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement("SELECT * FROM books ORDER BY sold_copies DESC LIMIT 5")).thenReturn(mockStatement);
        when(mockResultSet.next()).thenReturn(true, true, true, true, true, false); // Simulate 5 books
        when(mockResultSet.getInt("book_id")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("Test Title");
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getInt("stock")).thenReturn(10);
        when(mockResultSet.getDouble("price")).thenReturn(29.99);
        when(mockResultSet.getInt("sold_copies")).thenReturn(5);

        // Call the loadTop5Books method
        List<Book> books = bookService.loadTop5Books();

        // Verify interactions
        verify(mockConnection).prepareStatement("SELECT * FROM books ORDER BY sold_copies DESC LIMIT 5");
        verify(mockStatement).executeQuery();

        // Print results
        System.out.println("testLoadTop5Books passed. Retrieved " + books.size() + " books.");
    }
}
