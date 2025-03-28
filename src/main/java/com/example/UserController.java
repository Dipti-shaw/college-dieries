package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDAO userDAO = new UserDAO();

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        System.out.println("Received signup request: " + user);
        try {
            if (user.getUserId() <= 0 || user.getUserType() == null || user.getUserName() == null) {
                return ResponseEntity.badRequest().body("Invalid user data: userId, userType, and userName are required");
            }
            userDAO.addUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            System.err.println("Signup error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam int userId, @RequestParam String userType) {
        System.out.println("Login attempt for userId: " + userId + ", userType: " + userType);
        User user = userDAO.getUser(userId, userType);
        if (user != null) {
            System.out.println("Login successful for userId: " + userId);
            return ResponseEntity.ok(user);
        }
        System.out.println("Login failed: No user found for userId: " + userId + ", userType: " + userType);
        return ResponseEntity.status(401).body(null); // Should return 401, not 400
    }
}
