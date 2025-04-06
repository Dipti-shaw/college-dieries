package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {
    public static List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String query = "SELECT * FROM Announcements";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                announcements.add(new Announcement(
                    rs.getInt("Announcement_id"),
                    rs.getString("Gymkhana_name"),
                    rs.getString("Announcement_message"),
                    rs.getString("Timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return announcements;
    }

    public static void addAnnouncement(Announcement announcement) {
        // Validate Gymkhana_name exists in Gymkhana table
        if (announcement.getGymkhanaName() != null) {
            String checkQuery = "SELECT COUNT(*) FROM Gymkhana WHERE Gymkhana_name = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, announcement.getGymkhanaName());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new RuntimeException("Invalid Gymkhana name: " + announcement.getGymkhanaName());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to validate Gymkhana: " + e.getMessage());
            }
        }

        String query = "INSERT INTO Announcements (Gymkhana_name, Announcement_message, Timestamp) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, announcement.getGymkhanaName());
            stmt.setString(2, announcement.getAnnouncementMessage());
            stmt.setString(3, announcement.getTimestamp() != null ? announcement.getTimestamp() : new java.sql.Timestamp(System.currentTimeMillis()).toString());
            stmt.executeUpdate();
            System.out.println("Announcement added");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add announcement: " + e.getMessage());
        }
    }
}