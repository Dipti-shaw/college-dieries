package com.example;

public class Announcement {
    private int announcementId;
    private String gymkhanaName;
    private String announcementMessage;
    private String timestamp;

    public Announcement(int announcementId, String gymkhanaName, String announcementMessage, String timestamp) {
        this.announcementId = announcementId;
        this.gymkhanaName = gymkhanaName;
        this.announcementMessage = announcementMessage;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getAnnouncementId() { return announcementId; }
    public void setAnnouncementId(int announcementId) { this.announcementId = announcementId; }
    public String getGymkhanaName() { return gymkhanaName; }
    public void setGymkhanaName(String gymkhanaName) { this.gymkhanaName = gymkhanaName; }
    public String getAnnouncementMessage() { return announcementMessage; }
    public void setAnnouncementMessage(String announcementMessage) { this.announcementMessage = announcementMessage; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}