// header.js: controla el dropdown de usuario en el header
document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('.user-dropdown').forEach(function(dropdown) {
    const btn = dropdown.querySelector('.user-btn');
    const menu = dropdown.querySelector('.user-menu');
    if (!btn || !menu) return;

    // evitar que clicks dentro del menú cierren inmediatamente
    menu.addEventListener('click', function(e) {
      e.stopPropagation();
    });

    btn.addEventListener('click', function(e) {
      e.stopPropagation();
      // toggle open
      const isOpen = dropdown.classList.toggle('open');
      btn.setAttribute('aria-expanded', isOpen ? 'true' : 'false');
    });

    // cerrar con Escape cuando el dropdown está abierto
    dropdown.addEventListener('keydown', function(e) {
      if (e.key === 'Escape' || e.key === 'Esc') {
        dropdown.classList.remove('open');
        btn.setAttribute('aria-expanded', 'false');
        btn.focus();
      }
    });
  });

  // cerrar al clicar fuera
  document.addEventListener('click', function() {
    document.querySelectorAll('.user-dropdown.open').forEach(function(d) {
      const btn = d.querySelector('.user-btn');
      d.classList.remove('open');
      if (btn) btn.setAttribute('aria-expanded', 'false');
    });
  });

  // permitir cierre con Escape global
  document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape' || e.key === 'Esc') {
      document.querySelectorAll('.user-dropdown.open').forEach(function(d) {
        const btn = d.querySelector('.user-btn');
        d.classList.remove('open');
        if (btn) btn.setAttribute('aria-expanded', 'false');
      });
    }
  });

  // controlar el boton hamburger en moviles
  const hamburger = document.querySelector('.hamburger');
  const mainNav = document.getElementById('main-nav');
  if (hamburger && mainNav) {
    hamburger.addEventListener('click', function(e) {
      e.stopPropagation();
      const isOpen = hamburger.classList.toggle('open');
      hamburger.setAttribute('aria-expanded', isOpen ? 'true' : 'false');
      if (isOpen) {
        mainNav.classList.add('open');
      } else {
        mainNav.classList.remove('open');
      }
    });

    // cerrar el nav si se hace click en un enlace del nav en modo móvil
    mainNav.addEventListener('click', function(e) {
      const target = e.target;
      if (target.tagName === 'A' && window.innerWidth <= 800) {
        hamburger.classList.remove('open');
        hamburger.setAttribute('aria-expanded', 'false');
        mainNav.classList.remove('open');
      }
    });
  }

  // cerrar hamburguer y nav al clicar fuera (ya tenemos evento global de click que cierra dropdowns)
  document.addEventListener('click', function() {
    if (hamburger && mainNav && mainNav.classList.contains('open')) {
      hamburger.classList.remove('open');
      hamburger.setAttribute('aria-expanded', 'false');
      mainNav.classList.remove('open');
    }
  });
});
