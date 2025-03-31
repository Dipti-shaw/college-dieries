package com.example;

public class Story {
    private int userId;
    private String userType;
    private String posts;
    private int likeCount;
    private int dislikeCount;
    private String timestamp;

    // Constructor
    public Story(int userId, String userType, String posts, int likeCount, int dislikeCount, String timestamp) {
        this.userId = userId;
        this.userType = userType;
        this.posts = posts;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getPosts() { return posts; }
    public void setPosts(String posts) { this.posts = posts; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public int getDislikeCount() { return dislikeCount; }
    public void setDislikeCount(int dislikeCount) { this.dislikeCount = dislikeCount; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}