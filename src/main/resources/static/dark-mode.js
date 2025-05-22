function toggleDarkMode() {
    const body = document.body;
    const isDarkMode = body.classList.toggle('dark-mode');
    const themeToggleButton = document.getElementById('themeToggleButton');
    if (themeToggleButton) {
        themeToggleButton.innerHTML = isDarkMode
            ? '<i class="fas fa-sun"></i> Light Mode'
            : '<i class="fas fa-moon"></i> Dark Mode';
    }
    localStorage.setItem('darkMode', isDarkMode ? 'enabled' : 'disabled');
}

function loadTheme() {
    const darkMode = localStorage.getItem('darkMode');
    const body = document.body;
    const themeToggleButton = document.getElementById('themeToggleButton');
    if (darkMode === 'enabled') {
        body.classList.add('dark-mode');
        if (themeToggleButton) {
            themeToggleButton.innerHTML = '<i class="fas fa-sun"></i> Light Mode';
        }
    } else {
        if (themeToggleButton) {
            themeToggleButton.innerHTML = '<i class="fas fa-moon"></i> Dark Mode';
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadTheme();
    const themeToggleButton = document.getElementById('themeToggleButton');
    if (themeToggleButton) {
        themeToggleButton.addEventListener('click', toggleDarkMode);
    }
});