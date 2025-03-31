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
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, story.getUserId());
            stmt.setString(2, story.getUserType());
            stmt.setString(3, story.getPosts());
            stmt.setInt(4, story.getLikeCount());
            stmt.setInt(5, story.getDislikeCount());
            stmt.setTimestamp(6, Timestamp.valueOf(story.getTimestamp()));
            stmt.executeUpdate();
            System.err.println("story added to databasse");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StoryWithUser> getAllStoriesWithUser() {
        List<StoryWithUser> stories = new ArrayList<>();
        String query = "SELECT u.User_name, s.Posts FROM Story_Section s JOIN User u ON s.User_id = u.User_id AND s.User_type = u.User_type";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userName = rs.getString("User_name");
                String posts = rs.getString("Posts");
                stories.add(new StoryWithUser(userName, posts));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stories;
    }
}
