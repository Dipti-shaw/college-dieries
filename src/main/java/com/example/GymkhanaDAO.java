package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GymkhanaDAO {
    public static List<Gymkhana> getAllGymkhanas() {
        List<Gymkhana> gymkhanas = new ArrayList<>();
        String query = "SELECT g.*, u1.User_name AS presName, u2.User_name AS facultyName, u3.User_name AS vicePresName " +
                       "FROM Gymkhana g " +
                       "LEFT JOIN User u1 ON g.Pres_id = u1.User_id " +
                       "LEFT JOIN User u2 ON g.Faculty_id = u2.User_id " +
                       "LEFT JOIN User u3 ON g.Vice_pres_id = u3.User_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                gymkhanas.add(new Gymkhana(
                    rs.getString("Gymkhana_name"),
                    rs.getInt("Pres_id"),
                    rs.getString("presName"),
                    rs.getInt("Faculty_id"),
                    rs.getString("facultyName"),
                    rs.getInt("Vice_pres_id"),
                    rs.getString("vicePresName"),
                    rs.getInt("Member_count"),
                    rs.getInt("Funds")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gymkhanas;
    }

    public static void assignFaculty(String gymkhanaName, int facultyId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Checkpoint 1: Verify user exists and is of type "faculty"
            String userCheckQuery = "SELECT User_type FROM User WHERE User_id = ? AND User_type = 'faculty'";
            try (PreparedStatement checkStmt = conn.prepareStatement(userCheckQuery)) {
                checkStmt.setInt(1, facultyId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("User with ID " + facultyId + " not found or is not a faculty member.");
                }
            }

            // Update Gymkhana table
            String gymkhanaQuery = "UPDATE Gymkhana SET Faculty_id = ? WHERE Gymkhana_name = ?";
            try (PreparedStatement gymkhanaStmt = conn.prepareStatement(gymkhanaQuery)) {
                gymkhanaStmt.setInt(1, facultyId);
                gymkhanaStmt.setString(2, gymkhanaName);
                int rowsAffected = gymkhanaStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No gymkhana found with name: " + gymkhanaName);
                }
            }

            // Clear existing faculty role for this user if any
            String clearFacultyQuery = "UPDATE User SET Announcements_pos = NULL WHERE User_id = ? AND Announcements_pos LIKE ?";
            try (PreparedStatement clearFacultyStmt = conn.prepareStatement(clearFacultyQuery)) {
                clearFacultyStmt.setInt(1, facultyId);
                clearFacultyStmt.setString(2, gymkhanaName + "-Faculty%");
                clearFacultyStmt.executeUpdate();
            }

            // Update User table with faculty role
            String userQuery = "UPDATE User SET Announcements_pos = ? WHERE User_id = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                userStmt.setString(1, gymkhanaName + "-Faculty");
                userStmt.setInt(2, facultyId);
                int userRowsAffected = userStmt.executeUpdate();
                if (userRowsAffected == 0) {
                    throw new SQLException("Failed to update User table for ID: " + facultyId);
                }
                // Checkpoint 2: Verify the update by checking the new value
                try (PreparedStatement verifyStmt = conn.prepareStatement("SELECT Announcements_pos FROM User WHERE User_id = ?")) {
                    verifyStmt.setInt(1, facultyId);
                    ResultSet verifyRs = verifyStmt.executeQuery();
                    if (verifyRs.next() && !((gymkhanaName + "-Faculty").equals(verifyRs.getString("Announcements_pos")))) {
                        throw new SQLException("User table update verification failed for ID: " + facultyId);
                    }
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Faculty assigned to gymkhana and user updated");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Failed to assign faculty: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setBudget(String gymkhanaName, int funds) {
        String query = "UPDATE Gymkhana SET Funds = ? WHERE Gymkhana_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, funds);
            stmt.setString(2, gymkhanaName);
            stmt.executeUpdate();
            System.out.println("Budget set for gymkhana");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set budget: " + e.getMessage());
        }
    }

    public static void assignPresident(String gymkhanaName, int presId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Checkpoint 1: Verify user exists and is of type "student"
            String userCheckQuery = "SELECT User_type FROM User WHERE User_id = ? AND User_type = 'student'";
            try (PreparedStatement checkStmt = conn.prepareStatement(userCheckQuery)) {
                checkStmt.setInt(1, presId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("User with ID " + presId + " not found or is not a student.");
                }
            }

            // Update Gymkhana table
            String gymkhanaQuery = "UPDATE Gymkhana SET Pres_id = ? WHERE Gymkhana_name = ?";
            try (PreparedStatement gymkhanaStmt = conn.prepareStatement(gymkhanaQuery)) {
                gymkhanaStmt.setInt(1, presId);
                gymkhanaStmt.setString(2, gymkhanaName);
                int rowsAffected = gymkhanaStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No gymkhana found with name: " + gymkhanaName);
                }
            }

            // Clear existing president role for this user if any
            String clearPresQuery = "UPDATE User SET Announcements_pos = NULL WHERE User_id = ? AND Announcements_pos LIKE ?";
            try (PreparedStatement clearPresStmt = conn.prepareStatement(clearPresQuery)) {
                clearPresStmt.setInt(1, presId);
                clearPresStmt.setString(2, gymkhanaName + "-President%");
                clearPresStmt.executeUpdate();
            }

            // Update User table with president role
            String userQuery = "UPDATE User SET Announcements_pos = ? WHERE User_id = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                userStmt.setString(1, gymkhanaName + "-President");
                userStmt.setInt(2, presId);
                int userRowsAffected = userStmt.executeUpdate();
                if (userRowsAffected == 0) {
                    throw new SQLException("Failed to update User table for ID: " + presId);
                }
                // Checkpoint 2: Verify the update by checking the new value
                try (PreparedStatement verifyStmt = conn.prepareStatement("SELECT Announcements_pos FROM User WHERE User_id = ?")) {
                    verifyStmt.setInt(1, presId);
                    ResultSet verifyRs = verifyStmt.executeQuery();
                    if (verifyRs.next() && !((gymkhanaName + "-President").equals(verifyRs.getString("Announcements_pos")))) {
                        throw new SQLException("User table update verification failed for ID: " + presId);
                    }
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("President assigned to gymkhana and user updated");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Failed to assign president: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void assignVicePresident(String gymkhanaName, int vicePresId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Checkpoint 1: Verify user exists and is of type "student"
            String userCheckQuery = "SELECT User_type FROM User WHERE User_id = ? AND User_type = 'student'";
            try (PreparedStatement checkStmt = conn.prepareStatement(userCheckQuery)) {
                checkStmt.setInt(1, vicePresId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("User with ID " + vicePresId + " not found or is not a student.");
                }
            }

            // Update Gymkhana table
            String gymkhanaQuery = "UPDATE Gymkhana SET Vice_pres_id = ? WHERE Gymkhana_name = ?";
            try (PreparedStatement gymkhanaStmt = conn.prepareStatement(gymkhanaQuery)) {
                gymkhanaStmt.setInt(1, vicePresId);
                gymkhanaStmt.setString(2, gymkhanaName);
                int rowsAffected = gymkhanaStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No gymkhana found with name: " + gymkhanaName);
                }
            }

            // Clear existing vice-president role for this user if any
            String clearVicePresQuery = "UPDATE User SET Announcements_pos = NULL WHERE User_id = ? AND Announcements_pos LIKE ?";
            try (PreparedStatement clearVicePresStmt = conn.prepareStatement(clearVicePresQuery)) {
                clearVicePresStmt.setInt(1, vicePresId);
                clearVicePresStmt.setString(2, gymkhanaName + "-Vice-President%");
                clearVicePresStmt.executeUpdate();
            }

            // Update User table with vice-president role
            String userQuery = "UPDATE User SET Announcements_pos = ? WHERE User_id = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                userStmt.setString(1, gymkhanaName + "-Vice-President");
                userStmt.setInt(2, vicePresId);
                int userRowsAffected = userStmt.executeUpdate();
                if (userRowsAffected == 0) {
                    throw new SQLException("Failed to update User table for ID: " + vicePresId);
                }
                // Checkpoint 2: Verify the update by checking the new value
                try (PreparedStatement verifyStmt = conn.prepareStatement("SELECT Announcements_pos FROM User WHERE User_id = ?")) {
                    verifyStmt.setInt(1, vicePresId);
                    ResultSet verifyRs = verifyStmt.executeQuery();
                    if (verifyRs.next() && !((gymkhanaName + "-Vice-President").equals(verifyRs.getString("Announcements_pos")))) {
                        throw new SQLException("User table update verification failed for ID: " + vicePresId);
                    }
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Vice-president assigned to gymkhana and user updated");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Failed to assign vice-president: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void joinGymkhana(String gymkhanaName, int userId) {
        String query = "UPDATE Gymkhana SET Member_count = Member_count + 1 WHERE Gymkhana_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, gymkhanaName);
            stmt.executeUpdate();
            System.out.println("User joined gymkhana, member count increased");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to join gymkhana: " + e.getMessage());
        }
    }
}