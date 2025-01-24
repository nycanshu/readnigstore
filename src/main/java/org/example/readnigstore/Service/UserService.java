package org.example.readnigstore.Service;

import org.example.readnigstore.Helper.DBConnector;
import org.example.readnigstore.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    // Method to add a new user to the database
    public boolean addUser(User user) {
        String query = "INSERT INTO users (username, password, first_name, last_name, is_admin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setBoolean(5, user.isAdmin());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve a user by username
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"), // Corrected field name
                        rs.getString("username"),
                        rs.getString("first_name"), // Corrected field name
                        rs.getString("last_name"), // Corrected field name
                        rs.getString("password"),
                        rs.getBoolean("is_admin") // Corrected field name
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to update a user's profile (except username)
    public User updateUserProfile(User user) {
        String query = "UPDATE users SET first_name = ?, last_name = ? WHERE user_id = ?"; // Corrected field names
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setInt(3, user.getUserId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Login validation method
    public User validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"), // Corrected field name
                        rs.getString("username"),
                        rs.getString("first_name"), // Corrected field name
                        rs.getString("last_name"), // Corrected field name
                        rs.getString("password"),
                        rs.getBoolean("is_admin") // Corrected field name
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
