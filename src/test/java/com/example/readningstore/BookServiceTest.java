package org.example.readnigstore;

import org.example.readnigstore.Helper.DBConnector;
import org.example.readnigstore.Model.Book;
import org.example.readnigstore.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookService bookService;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        bookService = new BookService(); // Initialize the BookService
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock DBConnector to return the mock connection
        Mockito.mockStatic(DBConnector.class);
        when(DBConnector.getConnection()).thenReturn(mockConnection);
    }

    @Test
    public void testAddBook() throws SQLException {
        // Given
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(9.99);
        book.setStock(10);

        // Mock behavior for the statement
        when(mockConnection.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulating generated book ID

        // When
        bookService.addBook(book);

        // Then
        verify(mockStatement).setString(1, book.getTitle());
        verify(mockStatement).setString(2, book.getAuthor());
        verify(mockStatement).setDouble(3, book.getPrice());
        verify(mockStatement).setInt(4, book.getStock());
        verify(mockStatement).executeUpdate();
        System.out.println("testAddBook passed.");
    }

    @Test
    public void testGetBookById() throws SQLException {
        // Given
        int bookId = 1;

        // Mock behavior for the statement and result set
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("book_id")).thenReturn(bookId);
        when(mockResultSet.getString("title")).thenReturn("Test Book");
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getDouble("price")).thenReturn(9.99);
        when(mockResultSet.getInt("stock")).thenReturn(10);
        when(mockResultSet.getInt("sold_copies")).thenReturn(5);

        // When
        Book foundBook = bookService.getBookById(bookId);

        // Then
        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
        assertEquals("Test Author", foundBook.getAuthor());
        assertEquals(9.99, foundBook.getPrice());
        assertEquals(10, foundBook.getStock());
        assertEquals(5, foundBook.getSoldCopies());
        System.out.println("testGetBookById passed.");
    }

    @Test
    public void testUpdateBookStock() throws SQLException {
        // Given
        int bookId = 1;
        int newStock = 5;

        // Mock behavior for the statement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        // When
        bookService.updateBookStock(bookId, newStock);

        // Then
        verify(mockStatement).setInt(1, newStock);
        verify(mockStatement).setInt(2, bookId);
        verify(mockStatement).executeUpdate();
        System.out.println("testUpdateBookStock passed.");
    }

    @Test
    public void testUpdateBookSoldCopies() throws SQLException {
        // Given
        int bookId = 1;
        int newSoldCopies = 5;

        // Mock behavior for the statement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        // When
        bookService.updateBookSoldCopies(bookId, newSoldCopies);

        // Then
        verify(mockStatement).setInt(1, newSoldCopies);
        verify(mockStatement).setInt(2, bookId);
        verify(mockStatement).executeUpdate();
        System.out.println("testUpdateBookSoldCopies passed.");
    }

    @Test
    public void testGetAllBooksEmpty() throws SQLException {
        // Given
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // When
        List<Book> books = bookService.getAllBooks();

        // Then
        assertNotNull(books);
        assertTrue(books.isEmpty());
        verify(mockStatement).executeQuery(any(String.class));
        System.out.println("testGetAllBooksEmpty passed.");
    }

    @Test
    public void testGetAllBooks() throws SQLException {
        // Given
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one book

        // Mock data for the book
        when(mockResultSet.getInt("book_id")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("Test Book");
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getDouble("price")).thenReturn(9.99);
        when(mockResultSet.getInt("stock")).thenReturn(10);
        when(mockResultSet.getInt("sold_copies")).thenReturn(5);

        // When
        List<Book> books = bookService.getAllBooks();

        // Then
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
        System.out.println("testGetAllBooks passed.");
    }

    @Test
    public void testGetAllBooksException() throws SQLException {
        // Given
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any(String.class))).thenThrow(new SQLException());

        // When
        List<Book> books = bookService.getAllBooks();

        // Then
        assertNotNull(books);
        assertTrue(books.isEmpty()); // It should return an empty list in case of exception
        System.out.println("testGetAllBooksException passed.");
    }

    @Test
    public void testGetBookByIdNotFound() throws SQLException {
        // Given
        int bookId = 1;

        // Mock behavior for the statement and result set
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // When
        Book foundBook = bookService.getBookById(bookId);

        // Then
        assertNull(foundBook);
        System.out.println("testGetBookByIdNotFound passed.");
    }

    @Test
    public void testGetTopPopularBooks() throws SQLException {
        // Given
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one popular book

        // Mock data for the book
        when(mockResultSet.getInt("book_id")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("Popular Book");
        when(mockResultSet.getString("author")).thenReturn("Famous Author");
        when(mockResultSet.getDouble("price")).thenReturn(19.99);
        when(mockResultSet.getInt("stock")).thenReturn(5);
        when(mockResultSet.getInt("sold_copies")).thenReturn(10);

        // When
        List<Book> popularBooks = bookService.loadTop5Books();

        // Then
        assertNotNull(popularBooks);
        assertEquals(1, popularBooks.size());
        assertEquals("Popular Book", popularBooks.get(0).getTitle());
        System.out.println("testGetTopPopularBooks passed.");
    }
}
