package com.example;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDAO userDAO = new UserDAO();
    private final StoryDAO storyDAO = new StoryDAO();
    private final BazaarDAO bazaarDAO = new BazaarDAO();

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

    @GetMapping("/stories")
    public ResponseEntity<List<StoryWithUser>> getAllStories() {
        try {
            List<StoryWithUser> stories = storyDAO.getAllStoriesWithUser();
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/stories/like")
    public ResponseEntity<String> likeStory(@RequestBody StoryAction action) {
        try {
            storyDAO.updateLikeCount(action.getUserId(), action.getUserType(), action.getTimestamp(), true);
            return ResponseEntity.ok("Liked successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to like story: " + e.getMessage());
        }
    }

    @PostMapping("/stories/dislike")
    public ResponseEntity<String> dislikeStory(@RequestBody StoryAction action) {
        try {
            storyDAO.updateDislikeCount(action.getUserId(), action.getUserType(), action.getTimestamp(), true);
            return ResponseEntity.ok("Disliked successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to dislike story: " + e.getMessage());
        }
    }

    // Bazaar endpoints
    @PostMapping("/bazaar")
    public ResponseEntity<String> addBazaarItem(@RequestBody Bazaar item) {
        try {
            bazaarDAO.addItem(item);
            return ResponseEntity.ok("Item added to bazaar successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add item: " + e.getMessage());
        }
    }

    @GetMapping("/bazaar")
    public ResponseEntity<List<Bazaar>> getAllBazaarItems() {
        try {
            List<Bazaar> items = bazaarDAO.getAllItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/bazaar/buy")
    public ResponseEntity<String> buyItem(@RequestBody BazaarAction action) {
        try {
            bazaarDAO.buyItem(action.getItemId());
            return ResponseEntity.ok("Item purchased successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to buy item: " + e.getMessage());
        }
    }

    @PostMapping("/bazaar/provide")
    public ResponseEntity<String> provideItem(@RequestBody BazaarAction action) {
        try {
            bazaarDAO.fulfillRequest(action.getItemId());
            return ResponseEntity.ok("Request fulfilled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to provide item: " + e.getMessage());
        }
    }

// Updated announcements endpoints
    @PostMapping("/announcements")
    public ResponseEntity<String> addAnnouncement(@RequestBody Announcement announcement) {
        try {
            // Assuming gymkhanaName is derived from userData (e.g., gymkhanaPos)
            // You’ll need to adjust this based on how you populate gymkhanaName
            if (announcement.getGymkhanaName() == null) {
                return ResponseEntity.badRequest().body("Gymkhana name is required");
            }
            AnnouncementDAO.addAnnouncement(announcement); // Fixed instance method call
            return ResponseEntity.ok("Announcement added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to add announcement: " + e.getMessage());
        }
    }

    @GetMapping("/announcements")
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        try {
            List<Announcement> announcements = AnnouncementDAO.getAllAnnouncements(); // Fixed instance method call
            return ResponseEntity.ok(announcements);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Gymkhana endpoints
    @GetMapping("/gymkhanas")
    public ResponseEntity<List<Gymkhana>> getAllGymkhanas() {
        try {
            List<Gymkhana> gymkhanas = GymkhanaDAO.getAllGymkhanas();
            return ResponseEntity.ok(gymkhanas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/gymkhana/assign-faculty")
    public ResponseEntity<String> assignFaculty(@RequestBody GymkhanaAction action) {
        try {
            GymkhanaDAO.assignFaculty(action.getGymkhanaName(), action.getUserId());
            return ResponseEntity.ok("Faculty assigned successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to assign faculty: " + e.getMessage());
        }
    }

    @PostMapping("/gymkhana/set-budget")
    public ResponseEntity<String> setBudget(@RequestBody GymkhanaAction action) {
        try {
            GymkhanaDAO.setBudget(action.getGymkhanaName(), action.getFunds());
            return ResponseEntity.ok("Budget set successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to set budget: " + e.getMessage());
        }
    }

    @PostMapping("/gymkhana/assign-pres")
    public ResponseEntity<String> assignPresident(@RequestBody GymkhanaAction action) {
        try {
            GymkhanaDAO.assignPresident(action.getGymkhanaName(), action.getUserId());
            return ResponseEntity.ok("President assigned successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to assign president: " + e.getMessage());
        }
    }

    @PostMapping("/gymkhana/assign-vice-pres")
    public ResponseEntity<String> assignVicePresident(@RequestBody GymkhanaAction action) {
        try {
            GymkhanaDAO.assignVicePresident(action.getGymkhanaName(), action.getUserId());
            return ResponseEntity.ok("Vice-president assigned successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to assign vice-president: " + e.getMessage());
        }
    }

    @PostMapping("/gymkhana/join")
    public ResponseEntity<String> joinGymkhana(@RequestBody GymkhanaAction action) {
        try {
            GymkhanaDAO.joinGymkhana(action.getGymkhanaName(), action.getUserId());
            return ResponseEntity.ok("Joined gymkhana successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to join gymkhana: " + e.getMessage());
        }
    }
}

// Class to handle like/dislike requests
class StoryAction {

    private int userId;
    private String userType;
    private String timestamp;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

// Class to handle bazaar actions
class BazaarAction {

    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
