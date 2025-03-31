package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static String url = "jdbc:mysql://localhost:3306/college_diaries";
    static String user = "mohit";
    static String password = "Mohit@123";

    public static Connection getConnection() {
        try {
            System.err.println("databasse connected succesfully");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}