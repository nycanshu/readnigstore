package org.example.readnigstore.Service;

import org.example.readnigstore.Model.ShoppingCart;
import org.example.readnigstore.Model.User;
import org.example.readnigstore.Model.Book;
import org.example.readnigstore.Helper.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartService {
    private final BookService bookService = new BookService();

    // Method to add a book to the user's cart
    public void addToCart(User user, Book book) {
        ShoppingCart existingCartItem = getCartItem(user, book);

        // If the cart item exists, update the quantity
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + 1; // Increase quantity
            updateCartItemQuantity(existingCartItem.getCartId(), newQuantity);
        } else {
            // Otherwise, add a new cart item
            addNewCartItem(user, book);
        }
    }

    // Method to check if the cart item exists
    private ShoppingCart getCartItem(User user, Book book) {
        String query = "SELECT * FROM shoppingcart WHERE user_id = ? AND book_id = ? AND is_checked_out = 0";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.setInt(2, book.getBookId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createCartItemFromResultSet(resultSet, user, book);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return null;
    }

    // Method to update the quantity of an existing cart item
    public void updateCartItemQuantity(int cartId, int newQuantity) {
        String query = "UPDATE shoppingcart SET quantity = ? WHERE cart_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newQuantity);
            statement.setInt(2, cartId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    // Method to add a new cart item
    private void addNewCartItem(User user, Book book) {
        String query = "INSERT INTO shoppingcart (user_id, book_id, quantity, is_checked_out) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.setInt(2, book.getBookId());
            statement.setInt(3, 1); // Set initial quantity to 1
            statement.setBoolean(4, false); // Initially not checked out
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    // Method to retrieve cart items for a user
    public List<ShoppingCart> getCartItems(User user) {
        List<ShoppingCart> cartItems = new ArrayList<>();
        String query = "SELECT * FROM shoppingcart WHERE user_id = ? AND is_checked_out = 0";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ShoppingCart cartItem = createCartItemFromResultSet(resultSet, user, bookService.getBookById(resultSet.getInt("book_id")));
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return cartItems;
    }

    // Create a ShoppingCart object from ResultSet
    private ShoppingCart createCartItemFromResultSet(ResultSet resultSet, User user, Book book) throws SQLException {
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setCartId(resultSet.getInt("cart_id"));
        cartItem.setUser(user);
        cartItem.setQuantity(resultSet.getInt("quantity"));
        cartItem.setBook(book);
        cartItem.setCheckedOut(resultSet.getBoolean("is_checked_out"));
        return cartItem;
    }

    // Method to remove an item from the cart
    public void removeFromCart(ShoppingCart cartItem, User user) {
        String query = "DELETE FROM shoppingcart WHERE cart_id = ? AND user_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cartItem.getCartId());
            statement.setInt(2, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    // Method to calculate the total price of items in the user's cart
    public double calculateTotalPrice(User user) {
        double totalPrice = 0;
        List<ShoppingCart> cartItems = getCartItems(user);
        for (ShoppingCart cartItem : cartItems) {
            totalPrice += cartItem.getBook().getPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    // Method clear the cart after checkout make the is_checked_out to true
    public void clearCart(User user) {
        String query = "UPDATE shoppingcart SET is_checked_out = 1 WHERE user_id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

}
