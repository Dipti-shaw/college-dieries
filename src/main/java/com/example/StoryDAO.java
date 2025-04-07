package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StoryDAO {

    public void addStory(Story story) {
        String query = "INSERT INTO Story_Section (User_id, User_type, Posts, Like_count, Dislike_count, Timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, story.getUserId());
            stmt.setString(2, story.getUserType());
            stmt.setString(3, story.getPosts());
            stmt.setInt(4, story.getLikeCount());
            stmt.setInt(5, story.getDislikeCount());
            stmt.setTimestamp(6, Timestamp.valueOf(story.getTimestamp()));
            stmt.executeUpdate();
            System.err.println("querry executed : "+query);
            System.out.println("Story added to database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StoryWithUser> getAllStoriesWithUser() {
        List<StoryWithUser> stories = new ArrayList<>();
        String query = "SELECT u.User_name, s.Posts, s.Like_count, s.Dislike_count, s.User_id, s.User_type, s.Timestamp FROM Story_Section s JOIN User u ON s.User_id = u.User_id AND s.User_type = u.User_type ORDER BY s.Timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()
             ) {
            System.err.println("querry executed : "+query);
            while (rs.next()) {
                String userName = rs.getString("User_name");
                String posts = rs.getString("Posts");
                int likeCount = rs.getInt("Like_count");
                int dislikeCount = rs.getInt("Dislike_count");
                int userId = rs.getInt("User_id");
                String userType = rs.getString("User_type");
                String timestamp = rs.getTimestamp("Timestamp").toString();
                stories.add(new StoryWithUser(userName, posts, likeCount, dislikeCount, userId, userType, timestamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stories;
    }

    public void updateLikeCount(int userId, String userType, String timestamp, boolean increment) {
        String query = "UPDATE Story_Section SET Like_count = Like_count + ? WHERE User_id = ? AND User_type = ? AND Timestamp = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            System.err.println("querry executed : "+query);
            stmt.setInt(1, increment ? 1 : -1);
            stmt.setInt(2, userId);
            stmt.setString(3, userType);
            stmt.setTimestamp(4, Timestamp.valueOf(timestamp));
            int rows = stmt.executeUpdate();
            System.out.println("Like count updated, rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDislikeCount(int userId, String userType, String timestamp, boolean increment) {
        String query = "UPDATE Story_Section SET Dislike_count = Dislike_count + ? WHERE User_id = ? AND User_type = ? AND Timestamp = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            System.err.println("querry executed : "+query);
            stmt.setInt(1, increment ? 1 : -1);
            stmt.setInt(2, userId);
            stmt.setString(3, userType);
            stmt.setTimestamp(4, Timestamp.valueOf(timestamp));
            int rows = stmt.executeUpdate();
            System.out.println("Dislike count updated, rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}