// Declare userData in global scope
let userData;

function showSection(sectionId, element) {
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => section.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');

    const navItems = document.querySelectorAll('.sidebar ul li');
    navItems.forEach(item => item.classList.remove('active'));
    if (element && element.tagName === 'LI') element.classList.add('active');
    
    if (window.innerWidth <= 768) document.getElementById('sidebar').classList.remove('open');
}

document.addEventListener('DOMContentLoaded', function() {
    if (!localStorage.getItem('isLoggedIn')) {
        window.location.href = 'index.html';
        return;
    }

    const userDataRaw = localStorage.getItem('user');
    console.log('Raw userData from localStorage:', userDataRaw);

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

    // Fetch and display stories
    function loadStories() {
        fetch('http://localhost:8080/api/users/stories', { method: 'GET' })
        .then(response => { if (!response.ok) throw new Error(`Failed to fetch stories: ${response.status}`); return response.json(); })
        .then(stories => {
            const storiesList = document.getElementById('stories-list');
            if (!storiesList) { console.error('stories-list element not found'); return; }
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
            document.querySelectorAll('.like-btn').forEach(btn => btn.addEventListener('click', () => updateStoryAction(btn.getAttribute('data-userid'), btn.getAttribute('data-usertype'), btn.getAttribute('data-timestamp'), 'like')));
            document.querySelectorAll('.dislike-btn').forEach(btn => btn.addEventListener('click', () => updateStoryAction(btn.getAttribute('data-userid'), btn.getAttribute('data-usertype'), btn.getAttribute('data-timestamp'), 'dislike')));
        })
        .catch(error => console.error('Error fetching stories:', error));
    }

    function updateStoryAction(userId, userType, timestamp, action) {
        const endpoint = action === 'like' ? '/api/users/stories/like' : '/api/users/stories/dislike';
        fetch(`http://localhost:8080${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: parseInt(userId), userType, timestamp })
        })
        .then(response => { if (!response.ok) throw new Error(`Failed to ${action} story: ${response.status}`); return response.text(); })
        .then(data => { console.log(`${action} response:`, data); loadStories(); })
        .catch(error => { console.error(`Error ${action}ing story:`, error); alert(`Failed to ${action} story: ${error.message}`); });
    }

    loadStories();

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
            if (!storyData.posts) { alert('Story cannot be empty.'); return; }
            fetch('http://localhost:8080/api/users/stories', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(storyData)
            })
            .then(response => { if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`); return response.text(); })
            .then(data => { console.log('Story response:', data); document.getElementById('story-posts').value = ''; loadStories(); })
            .catch(error => { console.error('Story upload error:', error); alert('Failed to add story: ' + error.message); });
        });
    } else { console.error('add-story-form not found'); }
});
// MAKE NO CHANGES ABOVE THIS COMMENT PLEASE :)
// do not change area___________________________________________________________________________________________________________________________________________________

// Bazaar functionality
function loadBazaarItems() {
    fetch('http://localhost:8080/api/users/bazaar', { method: 'GET' })
    .then(response => { console.log('Fetch response status:', response.status); if (!response.ok) throw new Error(`Failed to fetch bazaar items: ${response.status}`); return response.json(); })
    .then(items => {
        console.log('Fetched bazaar items:', items);
        const bazaarItems = document.getElementById('bazaar-items');
        if (!bazaarItems) { console.error('bazaar-items element not found'); return; }
        bazaarItems.innerHTML = '';
        if (items.length === 0) { bazaarItems.innerHTML = '<p>No items available.</p>'; return; }
        items.forEach(item => {
            const itemElement = document.createElement('div');
            itemElement.className = 'card';
            itemElement.style.padding = '20px';
            itemElement.style.background = '#f9f9f9';
            itemElement.style.borderRadius = '10px';
            itemElement.style.boxShadow = '0px 4px 10px rgba(0, 0, 0, 0.1)';
            itemElement.innerHTML = `
                <h4>${item.itemName}</h4>
                <p>Quantity: ${item.count}</p>
                <p>Price: ₹${item.price}</p>
                <p>Type: ${item.itemType}</p>
                <div class="bazaar-actions">
                    ${item.itemType === 'sell' ? `<button class="buy-btn" data-itemid="${item.itemId}">Buy</button>` : `<button class="provide-btn" data-itemid="${item.itemId}">Provide</button>`}
                </div>
            `;
            bazaarItems.appendChild(itemElement);
        });
        document.querySelectorAll('.buy-btn').forEach(btn => btn.addEventListener('click', () => handleBazaarAction(btn.getAttribute('data-itemid'), 'buy')));
        document.querySelectorAll('.provide-btn').forEach(btn => btn.addEventListener('click', () => handleBazaarAction(btn.getAttribute('data-itemid'), 'provide')));
    })
    .catch(error => { console.error('Error fetching bazaar items:', error); bazaarItems.innerHTML = '<p>Error loading items. Check console.</p>'; });
}

function handleBazaarAction(itemId, action) {
    const endpoint = action === 'buy' ? '/api/users/bazaar/buy' : '/api/users/bazaar/provide';
    fetch(`http://localhost:8080${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ itemId: parseInt(itemId) })
    })
    .then(response => { if (!response.ok) throw new Error(`Failed to ${action} item: ${response.status}`); return response.text(); })
    .then(data => { console.log(`${action} response:`, data); loadBazaarItems(); })
    .catch(error => { console.error(`Error ${action}ing item:`, error); alert(`Failed to ${action} item: ${error.message}`); });
}

const bazaarForm = document.getElementById('add-bazaar-form');
console.log('Bazaar form element:', bazaarForm);
if (bazaarForm) {
    bazaarForm.addEventListener('submit', function(e) {
        e.preventDefault();
        console.log('Form submitted with action:', e.submitter.value);

        if (!userData || !userData.userId) {
            console.error('No userData found for bazaar submission:', userData);
            alert('User data not found. Please log in again.');
            window.location.href = 'index.html';
            return;
        }

        const itemData = {
            userId: userData.userId,
            itemName: document.getElementById('item-name').value.trim(),
            count: parseInt(document.getElementById('item-count').value),
            price: parseInt(document.getElementById('item-price').value),
            itemType: e.submitter.value
        };

        console.log('Submitting bazaar item:', itemData);

        if (!itemData.itemName || itemData.count < 1 || itemData.price < 0) {
            alert('Please provide valid item details. Item name cannot be empty, quantity must be at least 1, and price cannot be negative.');
            return;
        }

        fetch('http://localhost:8080/api/users/bazaar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(itemData)
        })
        .then(response => {
            console.log('Fetch response status:', response.status);
            if (!response.ok) {
                return response.text().then(text => { throw new Error(`HTTP error! Status: ${response.status}, Response: ${text}`); });
            }
            return response.text();
        })
        .then(data => {
            console.log('Bazaar response:', data);
            document.getElementById('item-name').value = '';
            document.getElementById('item-count').value = '1';
            document.getElementById('item-price').value = '';
            loadBazaarItems();
        })
        .catch(error => {
            console.error('Bazaar upload error:', error);
            alert('Failed to add item: ' + error.message);
        });
    });
} else {
    console.error('add-bazaar-form not found');
}

// Load bazaar items when the bazaar section is shown
document.getElementById('nav-bazaar').addEventListener('click', loadBazaarItems);

// Load items on page load as fallback
loadBazaarItems();