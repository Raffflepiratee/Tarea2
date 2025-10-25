const userType = localStorage.getItem('tipoUsuario');
if (userType !== 'bibliotecario') {
    console.log('Usuario no bibliotecario detectado, redirigiendo al login.');
    window.location.assign('/biblioteca-web/login.jsp');
}

document.getElementById('logoutBtn').addEventListener('click', function() {
    localStorage.removeItem('tipoUsuario');
    console.log('Cierre de sesión realizado, redirigiendo al login.');
    window.location.assign('/biblioteca-web/login.jsp');
});

const nombreUsuario = localStorage.getItem('nombre');
document.getElementById('nombre_usuario').innerHTML = nombreUsuario ? nombreUsuario : 'Bibliotecario';