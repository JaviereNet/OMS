document.addEventListener("DOMContentLoaded", () => {
    const loginContainer = document.getElementById('login');
    const registroContainer = document.getElementById('registro');
    const links = document.querySelectorAll('.link a');

    links.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const target = link.getAttribute('href').substring(1);

            if (target === 'registro') {
                loginContainer.style.display = 'none';
                registroContainer.style.display = 'block';
            } else {
                loginContainer.style.display = 'block';
                registroContainer.style.display = 'none';
            }
        });
    });

    const urlParams = new URLSearchParams(window.location.search);
    const action = urlParams.get('action');
    if (action === 'registro') {
        loginContainer.style.display = 'none';
        registroContainer.style.display = 'block';
    } else {
        loginContainer.style.display = 'block';
        registroContainer.style.display = 'none';
    }
});

