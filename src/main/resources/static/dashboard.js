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

    const userData = JSON.parse(localStorage.getItem('userData'));
    console.log('Raw userData from localStorage:', localStorage.getItem('userData'));
    console.log('Parsed userData:', userData);
    if (userData) {
        document.getElementById('user-id').textContent = userData.userId || 'Not provided';
        document.getElementById('user-type').textContent = userData.userType || 'Not provided';
        document.getElementById('user-name').textContent = userData.userName || 'Not provided';
        document.getElementById('user-phone').textContent = userData.phone || 'Not provided';
        document.getElementById('user-email').textContent = userData.email || 'Not provided';
        document.getElementById('user-department').textContent = userData.department || 'Not provided';
        document.getElementById('user-batch').textContent = userData.batch || 'Not provided';
        document.getElementById('user-branch').textContent = userData.branch || 'Not provided';
        document.getElementById('user-field').textContent = userData.field || 'Not provided';
        document.getElementById('user-gymkhana-pos').textContent = userData.announcementsPos || 'Not provided';
    } else {
        console.error('No userData found in localStorage');
    }

    document.getElementById('logoutBtn').addEventListener('click', function() {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('userData');
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

    // Fetch and display stories
    fetch('http://localhost:8080/api/users/stories', {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to fetch stories');
        return response.json();
    })
    .then(stories => {
        const storiesList = document.getElementById('stories-list');
        storiesList.innerHTML = ''; // Clear existing content
        stories.forEach(story => {
            const storyElement = document.createElement('div');
            storyElement.className = 'card';
            storyElement.innerHTML = `
                <h4>${story.userName}</h4>
                <p>${story.posts}</p>
            `;
            storiesList.appendChild(storyElement);
        });
    })
    .catch(error => {
        console.error('Error fetching stories:', error);
    });

    // Story submission
    const storyForm = document.getElementById('add-story-form');
    if (storyForm) {
        storyForm.addEventListener('submit', function(e) {
            e.preventDefault();
            if (!userData) {
                console.error('No userData found for story submission');
                alert('User data not found. Please log in again.');
                window.location.href = 'index.html';
                return;
            }

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
            .then(data => {
                console.log('Story response:', data);
                alert(data);
                document.getElementById('story-posts').value = '';
                // Refresh stories after adding
                fetch('http://localhost:8080/api/users/stories')
                    .then(response => response.json())
                    .then(stories => {
                        const storiesList = document.getElementById('stories-list');
                        storiesList.innerHTML = '';
                        stories.forEach(story => {
                            const storyElement = document.createElement('div');
                            storyElement.className = 'card';
                            storyElement.innerHTML = `
                                <h4>${story.userName}</h4>
                                <p>${story.posts}</p>
                            `;
                            storiesList.appendChild(storyElement);
                        });
                    });
            })
            .catch(error => {
                console.error('Story upload error:', error);
                alert('Failed to add story: ' + error.message);
            });
        });
    }
});