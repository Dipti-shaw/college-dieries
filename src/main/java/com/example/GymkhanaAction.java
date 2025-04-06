package com.example;

public class GymkhanaAction {
    private String gymkhanaName;
    private Integer userId;
    private Integer funds;

    // Default constructor (required for Jackson deserialization)
    public GymkhanaAction() {}

    // Parameterized constructor
    public GymkhanaAction(String gymkhanaName, Integer userId, Integer funds) {
        this.gymkhanaName = gymkhanaName;
        this.userId = userId;
        this.funds = funds;
    }

    // Getters and setters
    public String getGymkhanaName() { return gymkhanaName; }
    public void setGymkhanaName(String gymkhanaName) { this.gymkhanaName = gymkhanaName; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getFunds() { return funds; }
    public void setFunds(Integer funds) { this.funds = funds; }
}