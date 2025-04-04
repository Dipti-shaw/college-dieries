// StoryWithUser.java
package com.example;

public class StoryWithUser {
    private String userName;
    private String posts;
    private int likeCount;
    private int dislikeCount;
    private int userId;
    private String userType;
    private String timestamp;

    public StoryWithUser(String userName, String posts, int likeCount, int dislikeCount, int userId, String userType, String timestamp) {
        this.userName = userName;
        this.posts = posts;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.userId = userId;
        this.userType = userType;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUserName() { return userName; }
    public String getPosts() { return posts; }
    public int getLikeCount() { return likeCount; }
    public int getDislikeCount() { return dislikeCount; }
    public int getUserId() { return userId; }
    public String getUserType() { return userType; }
    public String getTimestamp() { return timestamp; }

    // Setters
    public void setUserName(String userName) { this.userName = userName; }
    public void setPosts(String posts) { this.posts = posts; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public void setDislikeCount(int dislikeCount) { this.dislikeCount = dislikeCount; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setUserType(String userType) { this.userType = userType; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}