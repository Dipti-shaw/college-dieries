signupForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const user = {
        userId: parseInt(document.getElementById('signup-userId').value),
        userType: document.getElementById('signup-userType').value,
        userName: document.getElementById('signup-userName').value,
        phone: parseInt(document.getElementById('signup-phone').value),
        email: document.getElementById('signup-email').value,
        department: document.getElementById('signup-department').value,
        batch: document.getElementById('signup-batch').value,
        branch: document.getElementById('signup-branch').value,
        field: null,
        gymkhanaPos: null
    };

    console.log('Sending user data:', JSON.stringify(user)); // Debug log

    fetch('http://localhost:8080/api/users/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Signup failed with status: ' + response.status);
        }
        return response.text();
    })
    .then(data => {
        alert(data);
        signupForm.style.display = 'none';
        loginForm.style.display = 'block';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Signup failed: ' + error.message);
    });
});