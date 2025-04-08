package com.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO User (User_id, User_type, User_name, Phone, Email_id, Department, Batch, Branch, Field, Announcements_pos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getUserType());
            stmt.setString(3, user.getUserName());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getDepartment());
            stmt.setString(7, user.getBatch());
            stmt.setString(8, user.getBranch());
            stmt.setString(9, user.getField());
            stmt.setString(10, user.getGymkhanaPos());

            int rowsAffected = stmt.executeUpdate();
            System.err.println("Query executed: " + query);
            if (rowsAffected > 0) {
                System.err.println("User added to database");
            } else {
                throw new SQLException("Failed to add user: No rows affected");
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw e; // Re-throw the exception to be handled by the controller
        }
    }

    public User getUser(int userId, String userType) throws SQLException {
        User user = null;
        String query = "SELECT * FROM User WHERE User_id = ? AND User_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, userType);
            ResultSet rs = stmt.executeQuery();
            System.err.println("Query executed: " + query);

            if (rs.next()) {
                user = new User(rs.getInt("User_id"), rs.getString("User_type"), rs.getString("User_name"),
                        rs.getString("Phone"), rs.getString("Email_id"), rs.getString("Department"),
                        rs.getString("Batch"), rs.getString("Branch"), rs.getString("Field"),
                        rs.getString("Announcements_pos"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            throw e; // Re-throw the exception
        }
        return user;
    }
}