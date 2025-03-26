document.addEventListener('DOMContentLoaded', () => {
    const sectionButtons = document.querySelectorAll('.section-btn');
    const sections = document.querySelectorAll('.section');
    const profileBtn = document.getElementById('profileBtn');
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');

    // Switch sections
    sectionButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Remove active class from all buttons
            sectionButtons.forEach(btn => btn.classList.remove('active'));
            // Add active class to clicked button
            button.classList.add('active');

            // Hide all sections
            sections.forEach(section => section.classList.remove('active'));
            // Show the selected section
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
            // Add your search logic here
        } else {
            alert('Please enter a search term.');
        }
    });
});