package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BazaarDAO {
    public void addItem(Bazaar item) {
        String query = "INSERT INTO Bazaar (User_id, Item_name, Count, Price, Item_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, item.getUserId());
            stmt.setString(2, item.getItemName());
            stmt.setInt(3, item.getCount());
            stmt.setInt(4, item.getPrice());
            stmt.setString(5, item.getItemType());
            stmt.executeUpdate();
            System.out.println("Item added to bazaar");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add item: " + e.getMessage());
        }
    }

    public List<Bazaar> getAllItems() {
        List<Bazaar> items = new ArrayList<>();
        String query = "SELECT * FROM Bazaar";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(new Bazaar(
                    rs.getInt("Item_id"),
                    rs.getInt("User_id"),
                    rs.getString("Item_name"),
                    rs.getInt("Count"),
                    rs.getInt("Price"),
                    rs.getString("Item_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void fulfillRequest(int itemId) {
        String query = "DELETE FROM Bazaar WHERE Item_id = ? AND Item_type = 'request'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No request found with Item_id: " + itemId);
            System.out.println("Request fulfilled and removed");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fulfill request: " + e.getMessage());
        }
    }

    public void buyItem(int itemId) {
        String query = "DELETE FROM Bazaar WHERE Item_id = ? AND Item_type = 'sell'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No sell item found with Item_id: " + itemId);
            System.out.println("Item bought and removed");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to buy item: " + e.getMessage());
        }
    }
}