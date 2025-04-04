function showSection(sectionId, element) {
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => section.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');

    const navItems = document.querySelectorAll('.sidebar ul li');
    navItems.forEach(item => item.classList.remove('active'));
    if (element && element.tagName === 'LI') element.classList.add('active');
    
    if (window.innerWidth <= 768) document.getElementById('sidebar').classList.remove('open');
}

document.getElementById('menuToggle').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('open');
});

document.addEventListener('DOMContentLoaded', function() {
    if (!localStorage.getItem('isLoggedIn')) {
        window.location.href = 'index.html';
        return;
    }

    const userDataRaw = localStorage.getItem('user');
    console.log('Raw userData from localStorage:', userDataRaw);

    let userData;
    try {
        userData = JSON.parse(userDataRaw);
    } catch (error) {
        console.error('Error parsing userData:', error);
        userData = null;
    }
    console.log('Parsed userData:', userData);

    if (userData) {
        document.getElementById('user-id').textContent = userData.userId || 'Not provided';
        document.getElementById('user-type').textContent = userData.userType || 'Not provided';
        document.getElementById('user-name').textContent = userData.userName || 'Not provided';
        document.getElementById('user-phone').textContent = userData.phone || 'Not provided';
        document.getElementById('user-email').textContent = userData.email || userData.emailId || 'Not provided';
        document.getElementById('user-department').textContent = userData.department || 'Not provided';
        document.getElementById('user-batch').textContent = userData.batch || 'Not provided';
        document.getElementById('user-branch').textContent = userData.branch || 'Not provided';
        document.getElementById('user-field').textContent = userData.field || 'Not provided';
        document.getElementById('user-gymkhana-pos').textContent = userData.announcementsPos || 'Not provided';
    }

    document.getElementById('logoutBtn').addEventListener('click', function() {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('user');
        window.location.href = 'index.html';
    });

    showSection('home', document.getElementById('nav-home'));

    document.addEventListener('click', function(e) {
        const sidebar = document.getElementById('sidebar');
        const menuToggle = document.getElementById('menuToggle');
        if (window.innerWidth <= 768 && !sidebar.contains(e.target) && e.target !== menuToggle && !menuToggle.contains(e.target)) {
            sidebar.classList.remove('open');
        }
    });

//meri ladki ko mt cher___________________________________________________________________________________________________________________________

    // Fetch and display stories
    function loadStories() {
        fetch('http://localhost:8080/api/users/stories', {
            method: 'GET'
        })
        .then(response => {
            if (!response.ok) throw new Error(`Failed to fetch stories: ${response.status}`);
            return response.json();
        })
        .then(stories => {
            const storiesList = document.getElementById('stories-list');
            if (!storiesList) {
                console.error('stories-list element not found');
                return;
            }
            storiesList.innerHTML = '';
            stories.forEach(story => {
                const storyElement = document.createElement('div');
                storyElement.className = 'card';
                storyElement.innerHTML = `
                    <h4>${story.userName}</h4>
                    <p>${story.posts}</p>
                    <div class="story-actions">
                        <button class="like-btn" data-userid="${story.userId}" data-usertype="${story.userType}" data-timestamp="${story.timestamp}">
                            Like (${story.likeCount})
                        </button>
                        <button class="dislike-btn" data-userid="${story.userId}" data-usertype="${story.userType}" data-timestamp="${story.timestamp}">
                            Dislike (${story.dislikeCount})
                        </button>
                    </div>
                `;
                storiesList.appendChild(storyElement);
            });

            // Add event listeners for like/dislike buttons
            document.querySelectorAll('.like-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const userId = this.getAttribute('data-userid');
                    const userType = this.getAttribute('data-usertype');
                    const timestamp = this.getAttribute('data-timestamp');
                    updateStoryAction(userId, userType, timestamp, 'like');
                });
            });

            document.querySelectorAll('.dislike-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const userId = this.getAttribute('data-userid');
                    const userType = this.getAttribute('data-usertype');
                    const timestamp = this.getAttribute('data-timestamp');
                    updateStoryAction(userId, userType, timestamp, 'dislike');
                });
            });
        })
        .catch(error => {
            console.error('Error fetching stories:', error);
        });
    }

    function updateStoryAction(userId, userType, timestamp, action) {
        const endpoint = action === 'like' ? '/api/users/stories/like' : '/api/users/stories/dislike';
        fetch(`http://localhost:8080${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: parseInt(userId), userType, timestamp })
        })
        .then(response => {
            if (!response.ok) throw new Error(`Failed to ${action} story: ${response.status}`);
            return response.text();
        })
        .then(data => {
            console.log(`${action} response:`, data);
            loadStories(); // Refresh stories to update counts
        })
        .catch(error => {
            console.error(`Error ${action}ing story:`, error);
            alert(`Failed to ${action} story: ${error.message}`);
        });
    }

    loadStories(); // Initial load

    // Story submission
    const storyForm = document.getElementById('add-story-form');
    if (storyForm) {
        storyForm.addEventListener('submit', function(e) {
            e.preventDefault();

            if (!userData || !userData.userId) {
                console.error('No userData found for story submission');
                alert('User data not found. Please log in again.');
                window.location.href = 'index.html';
                return;
            }

            const storyData = {
                userId: userData.userId,
                userType: userData.userType,
                posts: document.getElementById('story-posts').value.trim(),
                likeCount: 0,
                dislikeCount: 0,
                timestamp: new Date().toISOString().slice(0, 19).replace('T', ' ')
            };

            if (!storyData.posts) {
                alert('Story cannot be empty.');
                return;
            }

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
            .then(data => {
                console.log('Story response:', data);
                alert(data);
                document.getElementById('story-posts').value = '';
                loadStories(); // Refresh stories
            })
            .catch(error => {
                console.error('Story upload error:', error);
                alert('Failed to add story: ' + error.message);
            });
        });
    } else {
        console.error('add-story-form not found');
    }
});