// This file contains the JavaScript code for the web application.
// It handles interactivity, DOM manipulation, and any client-side logic required for the application.

// document.addEventListener('DOMContentLoaded', () => {
//     const greetingElement = document.createElement('h1');
//     greetingElement.textContent = 'Welcome to the Simple Web App!';
//     document.body.appendChild(greetingElement);
// });

function toggleSidebar() {
    // let sidebar = document.querySelector('.sidebar');
    // if (sidebar.style.display === 'none' || sidebar.style.display === '') {
    //     sidebar.style.display = 'block';
    // } else {
    //     sidebar.style.display = 'none';
    // }
}

function showSection(section) {
    document.querySelectorAll('.content-section').forEach(el => el.style.display = 'none');
    document.getElementById(section).style.display = 'block';
}
// showSection('projects');


