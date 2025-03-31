package com.example;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserDAO userDAO = new UserDAO();
    private final StoryDAO storyDAO = new StoryDAO();

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            userDAO.addUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam int userId, @RequestParam String userType) {
        User user = userDAO.getUser(userId, userType);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/stories")
    public ResponseEntity<String> addStory(@RequestBody Story story) {
        try {
            storyDAO.addStory(story);
            return ResponseEntity.ok("Story added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add story: " + e.getMessage());
        }
    }

    // New endpoint to fetch all stories with usernames
    @GetMapping("/stories")
    public ResponseEntity<List<StoryWithUser>> getAllStories() {
        try {
            List<StoryWithUser> stories = storyDAO.getAllStoriesWithUser();
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

// New class to hold story and username
class StoryWithUser {
    private String userName;
    private String posts;

    public StoryWithUser(String userName, String posts) {
        this.userName = userName;
        this.posts = posts;
    }

    // Getters and setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPosts() { return posts; }
    public void setPosts(String posts) { this.posts = posts; }
}