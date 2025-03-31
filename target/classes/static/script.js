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
    .then(response => {
        console.log('Login response status:', response.status);
        return response.json();
    })
    .then(data => {
        console.log('Login response data:', data);
        if (data && data.userId) { // Check for valid user data
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userData', JSON.stringify(data)); // Use 'userData'
            console.log('Stored userData after login:', localStorage.getItem('userData'));
            window.location.href = 'dashboard.html';
        } else {
            alert('Login failed: Invalid user data');
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        alert('Login failed');
    });
});

document.getElementById('signup-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const userData = {
        userId: parseInt(document.getElementById('signup-userId').value, 10), // Ensure integer
        userType: document.getElementById('signup-userType').value,
        userName: document.getElementById('signup-userName').value,
        phone: document.getElementById('signup-phone').value,
        email: document.getElementById('signup-email').value,
        department: document.getElementById('signup-department').value,
        batch: document.getElementById('signup-batch').value,
        branch: document.getElementById('signup-branch').value
    };
    
    fetch('http://localhost:8080/api/users/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        console.log('Signup response status:', response.status);
        return response.text();
    })
    .then(data => {
        console.log('Signup response text:', data);
        alert(data);
        // Store userData and log in the user
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('userData', JSON.stringify(userData));
        console.log('Stored userData after signup:', localStorage.getItem('userData'));
        document.getElementById('signup-form').style.display = 'none';
        document.getElementById('login-form').style.display = 'block';
        window.location.href = 'dashboard.html';
    })
    .catch(error => {
        console.error('Signup error:', error);
        alert('Signup failed');
    });
});