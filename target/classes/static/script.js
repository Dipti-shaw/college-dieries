document.addEventListener('DOMContentLoaded', () => {
    const loginBtn = document.getElementById('login-btn');
    const signupBtn = document.getElementById('signup-btn');
    const showSignup = document.getElementById('show-signup');
    const showLogin = document.getElementById('show-login');
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');
    const mainContent = document.getElementById('main-content');
    const sectionButtons = document.querySelectorAll('.section-btn');
    const sections = document.querySelectorAll('.section');
    const profileBtn = document.getElementById('profileBtn');
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');

    // Toggle between login and signup
    showSignup.addEventListener('click', (e) => {
        e.preventDefault();
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
    });

    showLogin.addEventListener('click', (e) => {
        e.preventDefault();
        signupForm.style.display = 'none';
        loginForm.style.display = 'block';
    });

    // Login handler
    loginBtn.addEventListener('click', () => {
        const userId = document.getElementById('login-userId').value;
        const userType = document.getElementById('login-userType').value;

        fetch(`http://localhost:8080/api/users/login?userId=${userId}&userType=${userType}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data) {
                localStorage.setItem('user', JSON.stringify(data));
                document.getElementById('auth-container').style.display = 'none';
                mainContent.style.display = 'block';
            } else {
                alert('Login failed');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Login failed');
        });
    });

    // Signup handler
    signupBtn.addEventListener('click', () => {
        const user = {
            userId: document.getElementById('signup-userId').value,
            userType: document.getElementById('signup-userType').value,
            userName: document.getElementById('signup-userName').value,
            phone: document.getElementById('signup-phone').value,
            email: document.getElementById('signup-email').value,
            department: document.getElementById('signup-department').value,
            batch: document.getElementById('signup-batch').value,
            branch: document.getElementById('signup-branch').value,
            field: document.getElementById('signup-field').value,
            gymkhanaPos: document.getElementById('signup-gymkhanaPos').value
        };

        fetch('http://localhost:8080/api/users/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user)
        })
        .then(response => response.text())
        .then(data => {
            alert(data);
            signupForm.style.display = 'none';
            loginForm.style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Signup failed');
        });
    });

    // Check if already logged in
    if (localStorage.getItem('user')) {
        document.getElementById('auth-container').style.display = 'none';
        mainContent.style.display = 'block';
    }

    // Homepage functionality (section switching)
    sectionButtons.forEach(button => {
        button.addEventListener('click', () => {
            sectionButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            sections.forEach(section => section.classList.remove('active'));
            const sectionId = button.getAttribute('data-section');
            document.getElementById(sectionId).classList.add('active');
        });
    });

    // Profile button click
    profileBtn.addEventListener('click', () => {
        alert('Profile section coming soon! Add your profile logic here.');
    });

    // Search button click
    searchBtn.addEventListener('click', () => {
        const query = searchInput.value.trim();
        if (query) {
            alert(`Searching for: ${query}`);
            // Add backend search API call here if needed
        } else {
            alert('Please enter a search term.');
        }
    });
});