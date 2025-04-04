function showSection(sectionId, element) {
    // Handle section visibility
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId).classList.add('active');

    // Handle sidebar active state
    const navItems = document.querySelectorAll('.sidebar ul li');
    navItems.forEach(item => {
        item.classList.remove('active');
    });
    if (element && element.tagName === 'LI') {
        element.classList.add('active');
    }
    
    // Close mobile menu if open
    if (window.innerWidth <= 768) {
        document.getElementById('sidebar').classList.remove('open');
    }
}

// Toggle mobile menu
document.getElementById('menuToggle').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('open');
});

// Show the Home section by default on page load
document.addEventListener('DOMContentLoaded', () => {
    showSection('home', document.getElementById('nav-home'));
    
    // Close menu when clicking outside on mobile
    document.addEventListener('click', function(e) {
        const sidebar = document.getElementById('sidebar');
        const menuToggle = document.getElementById('menuToggle');
        
        if (window.innerWidth <= 768 && 
            !sidebar.contains(e.target) && 
            e.target !== menuToggle && 
            !menuToggle.contains(e.target)) {
            sidebar.classList.remove('open');
        }
    });
});
document.addEventListener('DOMContentLoaded', function() {
// Check if user is logged in
if (!localStorage.getItem('isLoggedIn')) {
    // If not logged in, redirect to login page
    window.location.href = 'index.html';
}

// Get user data if needed
const userData = localStorage.getItem('userData');
if (userData) {
    console.log('User data:', JSON.parse(userData));
    // You can use this data to personalize the dashboard
}

// Logout functionality
document.getElementById('logoutBtn').addEventListener('click', function() {
    // Clear authentication data
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('userData');
    
    // Redirect to login page
    window.location.href = 'index.html';
});
showSection('home', document.getElementById('nav-home'));

// Close menu when clicking outside on mobile
document.addEventListener('click', function(e) {
    const sidebar = document.getElementById('sidebar');
    const menuToggle = document.getElementById('menuToggle');
    
    if (window.innerWidth <= 768 && 
        !sidebar.contains(e.target) && 
        e.target !== menuToggle && 
        !menuToggle.contains(e.target)) {
        sidebar.classList.remove('open');
    }
});
});
const storyForm = document.getElementById('add-story-form');

if (storyForm) {
    storyForm.addEventListener('submit', function (e) {
        e.preventDefault();

        // if (!userData) {
        //     console.error('No userData found for story submission');
        //     alert('User data not found. Please log in again.');
        //     window.location.href = 'index.html';
        //     return;
        // }

        const storyData = {
            userId: userData.userId,
            userType: userData.userType,
            posts: document.getElementById('story-posts').value,
            likeCount: 0,
            dislikeCount: 0,
            timestamp: new Date().toISOString().slice(0, 19).replace('T', ' ')
        };

        console.log('Submitting story:', storyData);

        fetch('http://localhost:8080/api/users/stories', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(storyData)
        })
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            return response.text();
        })
        .then(() => {
            console.log('Story successfully submitted.');
            document.getElementById('story-posts').value = ''; // Clear input field
            loadStories(); // Reload stories dynamically
        })
        .catch(error => {
            console.error('Story upload error:', error);
            alert('Failed to add story: ' + error.message);
        });
    });
}

// Function to load stories and display them with like/dislike buttons
function loadStories() {
    fetch('http://localhost:8080/api/users/stories')
        .then(response => response.json())
        .then(stories => {
            const storiesList = document.getElementById('stories-list');
            storiesList.innerHTML = ''; // Clear previous list

            stories.forEach(story => {
                const storyElement = document.createElement('div');
                storyElement.className = 'card';
                storyElement.style = `
                    padding: 15px;
                    margin-bottom: 10px;
                    border-radius: 8px;
                    background: #fff;
                    box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
                `;

                storyElement.innerHTML = `
                    <h4>${story.userName}</h4>
                    <p>${story.posts}</p>
                    <button class="like-btn" data-id="${story.storyId}" style="background: #28a745; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                        👍 Like (${story.likeCount})
                    </button>
                    <button class="dislike-btn" data-id="${story.storyId}" style="background: #dc3545; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                        👎 Dislike (${story.dislikeCount})
                    </button>
                `;

                storiesList.appendChild(storyElement);
            });

            // Attach event listeners for like and dislike buttons
            document.querySelectorAll('.like-btn').forEach(button => {
                button.addEventListener('click', function () {
                    const storyId = this.getAttribute('data-id');
                    updateReaction(storyId, 'like');
                });
            });

            document.querySelectorAll('.dislike-btn').forEach(button => {
                button.addEventListener('click', function () {
                    const storyId = this.getAttribute('data-id');
                    updateReaction(storyId, 'dislike');
                });
            });
        })
        .catch(error => console.error('Error loading stories:', error));
}

// Function to handle like/dislike
function updateReaction(storyId, action) {
    fetch(`http://localhost:8080/api/users/stories/${storyId}/${action}`, {
        method: 'POST'
    })
    .then(() => loadStories()) // Reload stories after updating
    .catch(error => console.error('Error updating reaction:', error));
}

// Initial load of stories
loadStories();