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