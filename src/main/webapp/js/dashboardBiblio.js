document.addEventListener("DOMContentLoaded", () => {

    const userType = localStorage.getItem('tipoUsuario');
    if (userType !== 'bibliotecario') {
        window.location.assign('/biblioteca-web/login.jsp');
    }

    const nombreUsuario = localStorage.getItem('nombre');
    const nombreEl = document.getElementById('nombre_usuario');
    if (nombreEl) {
        nombreEl.textContent = nombreUsuario ? nombreUsuario : "Bibliotecario";
    }

    const logout1 = document.getElementById('logoutBtn');
    if (logout1) {
        logout1.addEventListener('click', () => {
            localStorage.clear();
            window.location.assign('/biblioteca-web/login.jsp');
        });
    }

    const logout2 = document.getElementById('logoutBtn2');
    if (logout2) {
        logout2.addEventListener('click', () => {
            localStorage.clear();
            window.location.assign('/biblioteca-web/login.jsp');
        });
    }

});
