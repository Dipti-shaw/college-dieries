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
        String query = "UPDATE Gymkhana SET Faculty_id = ? WHERE Gymkhana_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, facultyId);
            stmt.setString(2, gymkhanaName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No rows updated - Check if Gymkhana_name exists or facultyId is valid.");
            } else {
                System.out.println("Faculty assigned to gymkhana");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage()); // Log the exact error
            throw new RuntimeException("Failed to assign faculty: " + e.getMessage());
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
        String query = "UPDATE Gymkhana SET Pres_id = ? WHERE Gymkhana_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, presId);
            stmt.setString(2, gymkhanaName);
            stmt.executeUpdate();
            System.out.println("President assigned to gymkhana");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to assign president: " + e.getMessage());
        }
    }

    public static void assignVicePresident(String gymkhanaName, int vicePresId) {
        String query = "UPDATE Gymkhana SET Vice_pres_id = ? WHERE Gymkhana_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vicePresId);
            stmt.setString(2, gymkhanaName);
            stmt.executeUpdate();
            System.out.println("Vice-president assigned to gymkhana");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to assign vice-president: " + e.getMessage());
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