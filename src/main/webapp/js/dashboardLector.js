// const userType = localStorage.getItem('tipoUsuario');
// if (userType !== 'lector') {
//     console.log('Usuario no lector detectado, redirigiendo al login.');
//     window.location.assign('/biblioteca-web/login.jsp');
// }
document.getElementById('logoutBtn').addEventListener('click', function() {
    localStorage.removeItem('tipoUsuario');
    console.log('Cierre de sesión realizado, redirigiendo al login.');
    window.location.assign('/biblioteca-web/login.jsp');
});