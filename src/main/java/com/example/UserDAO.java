package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(User user) {
        String query = "INSERT INTO User (User_id, User_type, User_name, Phone, Email, Department, Batch, Branch, Field, Gymkhana_pos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Explicitly manage transaction
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, user.getUserId());
                stmt.setString(2, user.getUserType());
                stmt.setString(3, user.getUserName());
                stmt.setInt(4, user.getPhone());
                stmt.setString(5, user.getEmail());
                stmt.setString(6, user.getDepartment());
                stmt.setString(7, user.getBatch());
                stmt.setString(8, user.getBranch());
                stmt.setString(9, user.getField());
                stmt.setString(10, user.getGymkhanaPos());
                int rowsAffected = stmt.executeUpdate();
                System.out.println("User added, rows affected: " + rowsAffected);
                conn.commit(); // Explicitly commit the transaction
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Failed to add user to database", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    public User getUser(int userId, String userType) {
        // [Unchanged from previous fix]
        String query = "SELECT * FROM User WHERE User_id = ? AND User_type = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, userType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getInt("User_id"), rs.getString("User_type"), rs.getString("User_name"),
                        rs.getInt("Phone"), rs.getString("Email"), rs.getString("Department"),
                        rs.getString("Batch"), rs.getString("Branch"), rs.getString("Field"),
                        rs.getString("Gymkhana_pos"));
                System.out.println("User found: " + user.getUserName());
                return user;
            } else {
                System.out.println("No user found for userId: " + userId + ", userType: " + userType);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
