package com.example;
public class User {
    // Attributes
    private int userId;
    private String userType;
    private String userName;
    private String phone;
    private String email;
    private String department;
    private String batch;
    private String branch;
    private String field;
    private String gymkhanaPos;

    // Constructor
    public User(int userId, String userType, String userName, String phone, String email, 
                String department, String batch, String branch, String field, String gymkhanaPos) {
        this.userId = userId;
        this.userType = userType;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.batch = batch;
        this.branch = branch;
        this.field = field;
        this.gymkhanaPos = gymkhanaPos;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getBatch() {
        return batch;
    }

    public String getBranch() {
        return branch;
    }

    public String getField() {
        return field;
    }

    public String getGymkhanaPos() {
        return gymkhanaPos;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setGymkhanaPos(String gymkhanaPos) {
        this.gymkhanaPos = gymkhanaPos;
    }
}