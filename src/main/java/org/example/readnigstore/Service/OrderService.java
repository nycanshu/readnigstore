package org.example.readnigstore.Service;

import org.example.readnigstore.Model.Order;
import org.example.readnigstore.Model.OrderDetail;
import org.example.readnigstore.Model.Book;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Helper.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    // Method to place an order and save it to the database
    public void placeOrder(Order order, List<OrderDetail> orderDetails) {
        int orderId = insertOrder(order);
        if (orderId > 0) {
            for (OrderDetail detail : orderDetails) {
                insertOrderDetail(orderId, detail);
                // Call the book service to update stock
                BookService bookService = new BookService();
                bookService.updateBookStock(detail.getBook().getBookId(), detail.getQuantity());
                bookService.updateBookSoldCopies(detail.getBook().getBookId(), detail.getQuantity());
            }
        }
    }

    // Insert order into the database
    private int insertOrder(Order order) {
        String query = "INSERT INTO orders (user_id, order_date, total_price, payment_card_number, payment_expiry_date, payment_cvv) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1; // To hold the generated order ID

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUser().getUserId());
            statement.setTimestamp(2, new java.sql.Timestamp(order.getOrderDate().getTime()));
            statement.setDouble(3, order.getTotalPrice());
            statement.setString(4, order.getPaymentCardNumber());
            statement.setString(5, order.getPaymentExpiryDate()); // Change from setDate to setString
            statement.setString(6, order.getPaymentCVV());
            statement.executeUpdate();

            // Retrieve the generated order ID
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                generatedId = keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return generatedId;
    }

    // Insert order detail into the database
    private void insertOrderDetail(int orderId, OrderDetail detail) {
        String query = "INSERT INTO orderdetails (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            statement.setInt(2, detail.getBook().getBookId());
            statement.setInt(3, detail.getQuantity());
            statement.setDouble(4, detail.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    // Method to get user's orders
    public List<Order> getUserOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.order_id, o.order_date, o.total_price, o.payment_card_number, o.payment_expiry_date, o.payment_cvv " +
                "FROM orders o WHERE o.user_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setOrderDate(resultSet.getTimestamp("order_date"));
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setPaymentCardNumber(resultSet.getString("payment_card_number"));
                order.setPaymentExpiryDate(resultSet.getString("payment_expiry_date")); // Keep as string
                order.setPaymentCVV(resultSet.getString("payment_cvv"));

                // Fetch order details
                List<OrderDetail> orderDetails = getOrderDetails(order.getOrderId());
                order.setOrderDetails(orderDetails);

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return orders;
    }

    // Method to get order details for a specific order
    private List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String query = "SELECT od.order_details_id, od.book_id, od.quantity, od.price, b.title, b.author " +
                "FROM orderdetails od JOIN books b ON od.book_id = b.book_id WHERE od.order_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(resultSet.getInt("order_details_id"));
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title")); // Set title
                book.setAuthor(resultSet.getString("author")); // Set author
                detail.setBook(book);
                detail.setQuantity(resultSet.getInt("quantity"));
                detail.setPrice(resultSet.getDouble("price"));
                orderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return orderDetails;
    }

    // Method to get user's order details with book info and order timestamp
    public List<OrderDetail> getUserOrderDetailsWithBookInfo(int userId) {
        List<OrderDetail> userOrderDetails = new ArrayList<>();
        String query = "SELECT o.order_date, od.order_details_id, od.quantity, od.price, b.title, b.author " +
                "FROM orders o " +
                "JOIN orderdetails od ON o.order_id = od.order_id " +
                "JOIN books b ON od.book_id = b.book_id " +
                "WHERE o.user_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setQuantity(resultSet.getInt("quantity"));
                detail.setPrice(resultSet.getDouble("price"));

                // Setting Book details
                Book book = new Book();
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                detail.setBook(book);

                // Setting Order date and time
                detail.setOrderDate(resultSet.getTimestamp("order_date"));

                userOrderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return userOrderDetails;
    }

}
