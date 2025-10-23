document.addEventListener('DOMContentLoaded', function () {
  const toggle = document.getElementById('menuToggle');
  const sidebar = document.querySelector('.sidebar');
  const overlay = document.querySelector('.sidebar-overlay');
  const body = document.body;

  if (!toggle || !sidebar || !body) return;

  function openClose() {
    const isOpen = body.classList.toggle('sidebar-open');
    // reflect state
    sidebar.classList.toggle('open', isOpen);
    if (isOpen) {
      toggle.setAttribute('aria-expanded', 'true');
      sidebar.setAttribute('aria-hidden', 'false');
    } else {
      toggle.setAttribute('aria-expanded', 'false');
      sidebar.setAttribute('aria-hidden', 'true');
    }
  }

  toggle.addEventListener('click', openClose);

  if (overlay) {
    overlay.addEventListener('click', () => {
      body.classList.remove('sidebar-open');
      sidebar.classList.remove('open');
      toggle.setAttribute('aria-expanded', 'false');
      sidebar.setAttribute('aria-hidden', 'true');
    });
  }

  // close on ESC
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && body.classList.contains('sidebar-open')) {
      body.classList.remove('sidebar-open');
      sidebar.classList.remove('open');
      toggle.setAttribute('aria-expanded', 'false');
      sidebar.setAttribute('aria-hidden', 'true');
    }
  });
});
