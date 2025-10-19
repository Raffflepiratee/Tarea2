// /c:/Users/rbogl/OneDrive/Escritorio/ProgramacionAp/Tarea2/src/main/webapp/js/login.js
// GitHub Copilot
// Cliente JS para enviar credenciales al backend y redirigir según tipo de usuario.
// El backend debe aceptar POST JSON en LOGIN_ENDPOINT y devolver JSON:
// { role: "lector" | "bibliotecario", token?: "<jwt-o-session>" }

const LOGIN_ENDPOINT = '/api/loginServlet'; // ajustar al endpoint real del servidor (p. ej. /LoginServlet)
const REDIRECTS = {
    lector: '/lector/dashboard.html',         // ajustar rutas destino
    bibliotecario: '/bibliotecario/dashboard.html'
};

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
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
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

        // Esperamos { role: 'lector'|'bibliotecario', token?: '...' }
        if (!data || !data.role) {
            showMessage('Respuesta inválida del servidor.', true);
            return;
        }

        // Guardar token si viene (opcional)
        if (data.token) {
            sessionStorage.setItem('authToken', data.token);
        }

        const role = data.role.toLowerCase();
        if (role === 'lector') {
            window.location.assign(REDIRECTS.lector);
            return;
        }
        if (role === 'bibliotecario') {
            window.location.assign(REDIRECTS.bibliotecario);
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
        document.getElementById('loginForm')?.appendChild(el);
    }
    el.textContent = text;
    el.style.color = isError ? 'crimson' : 'green';
}

function clearMessage() {
    const el = document.getElementById('loginMessage');
    if (el) el.textContent = '';
}