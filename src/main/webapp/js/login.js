// Endpoint relativo al servlet de login. Ajusta si tu contexto de aplicación difiere.
const LOGIN_ENDPOINT = 'loginServlet';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
    if (!form) return;

    form.addEventListener('submit', handleSubmit);
});

async function handleSubmit(e) {
    e.preventDefault();
    clearMessage();

    const email = (document.getElementById('email') || {}).value?.trim() || '';
    const password = (document.getElementById('password') || {}).value || '';

    if (!email || !password) {
        showMessage('Ingrese email y contraseña.', true);
        return;
    }

    showMessage('Iniciando sesión...', false);

    try {
        const res = await fetch(LOGIN_ENDPOINT, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) {
            // manejar errores comunes del servidor
            if (res.status === 401) {
                showMessage('Credenciales incorrectas.', true);
                return;
            }
            showMessage('Error del servidor. Intente nuevamente.', true);
            return;
        }

        const data = await res.json();

        if (!data || !data.role) {
            showMessage('Respuesta inválida del servidor.', true);
            return;
        }

        // Guardar rol en localStorage
        localStorage.setItem('tipoUsuario', data.role);
        localStorage.setItem('nombre', data.nombre);
        
    const redirectTo = data.redirect;
        if (redirectTo) {
            window.location.assign(redirectTo);
            return;
        }

        showMessage('Tipo de usuario desconocido.', true);
    } catch (err) {
        console.error(err);
        showMessage('No se pudo conectar al servidor.', true);
    }
}

/* Helpers para mostrar mensajes — adaptar HTML/CSS según tu página */
function showMessage(text, isError = false) {
    let el = document.getElementById('loginMessage');
    if (!el) {
        el = document.createElement('div');
        el.id = 'loginMessage';
        el.style.marginTop = '0.5rem';
        el.style.textAlign = 'center';
        document.getElementById('loginForm')?.appendChild(el);
    }
    el.textContent = text;
    el.style.color = isError ? 'crimson' : 'green';
}

function clearMessage() {
    const el = document.getElementById('loginMessage');
    if (el) el.textContent = '';
}