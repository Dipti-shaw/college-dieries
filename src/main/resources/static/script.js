// Toggle between forms
document.getElementById('show-signup').addEventListener('click', function(e) {
    e.preventDefault();
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('signup-form').style.display = 'block';
});

document.getElementById('show-login').addEventListener('click', function(e) {
    e.preventDefault();
    document.getElementById('signup-form').style.display = 'none';
    document.getElementById('login-form').style.display = 'block';
});

// Handle form submissions
document.getElementById('login-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const userId = document.getElementById('login-userId').value;
    const userType = document.getElementById('login-userType').value;
    
    fetch(`http://localhost:8080/api/users/login?userId=${userId}&userType=${userType}`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data) {
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('user', JSON.stringify(data));
            window.location.href = 'dashboard.html';
        } else {
            alert('Login failed');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Login failed');
    });
});

document.getElementById('signup-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const userData = {
        userId: document.getElementById('signup-userId').value,
        userType: document.getElementById('signup-userType').value,
        userName: document.getElementById('signup-userName').value,
        phone: document.getElementById('signup-phone').value,
        email: document.getElementById('signup-email').value,
        department: document.getElementById('signup-department').value,
        batch: document.getElementById('signup-batch').value,
        branch: document.getElementById('signup-branch').value,
        field: document.getElementById('signup-field').value // Map "Position" to field
    };
    
    fetch('http://localhost:8080/api/users/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
    .then(response => response.text())
    .then(data => {
        // alert(data); // Removed this line to eliminate the popup
        document.getElementById('signup-form').style.display = 'none';
        document.getElementById('login-form').style.display = 'block';
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('user', JSON.stringify(userData));
        window.location.href = 'dashboard.html';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Signup failed');
    });
});

// Check if user is already logged in
localStorage.setItem('isLoggedIn', 'true');