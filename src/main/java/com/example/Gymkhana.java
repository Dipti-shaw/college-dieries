package com.example;

public class Gymkhana {
    private String gymkhanaName;
    private Integer presId;
    private String presName; // Optional, for display
    private Integer facultyId;
    private String facultyName; // Optional, for display
    private Integer vicePresId;
    private String vicePresName; // Optional, for display
    private int memberCount;
    private int funds;

    public Gymkhana(String gymkhanaName, Integer presId, String presName, Integer facultyId, String facultyName, Integer vicePresId, String vicePresName, int memberCount, int funds) {
        this.gymkhanaName = gymkhanaName;
        this.presId = presId;
        this.presName = presName;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.vicePresId = vicePresId;
        this.vicePresName = vicePresName;
        this.memberCount = memberCount;
        this.funds = funds;
    }

    // Getters and setters
    public String getGymkhanaName() { return gymkhanaName; }
    public void setGymkhanaName(String gymkhanaName) { this.gymkhanaName = gymkhanaName; }
    public Integer getPresId() { return presId; }
    public void setPresId(Integer presId) { this.presId = presId; }
    public String getPresName() { return presName; }
    public void setPresName(String presName) { this.presName = presName; }
    public Integer getFacultyId() { return facultyId; }
    public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }
    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
    public Integer getVicePresId() { return vicePresId; }
    public void setVicePresId(Integer vicePresId) { this.vicePresId = vicePresId; }
    public String getVicePresName() { return vicePresName; }
    public void setVicePresName(String vicePresName) { this.vicePresName = vicePresName; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public int getFunds() { return funds; }
    public void setFunds(int funds) { this.funds = funds; }
}