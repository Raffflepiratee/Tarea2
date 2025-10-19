// Endpoint relativo al servlet de login. Ajusta si tu contexto de aplicación difiere.
const LOGIN_ENDPOINT = 'loginServlet';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
    if (!form) return;

    console.log("registrando submit");
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

        // Esperamos { role: 'lector'|'bibliotecario', redirect?: '/ruta', token?: '...' }
        if (!data || !data.role) {
            showMessage('Respuesta inválida del servidor.', true);
            return;
        }

        // Guardar token si viene (opcional)
        if (data.token) {
            sessionStorage.setItem('authToken', data.token);
        }

    // Preferimos la URL de redirección que devuelva el servidor
    // El servlet ya construye y devuelve `redirect` (incluye `contextPath`),
    // así que confiamos en esa URL en lugar de mapas locales duplicados.
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
        document.getElementById('loginForm')?.appendChild(el);
    }
    el.textContent = text;
    el.style.color = isError ? 'crimson' : 'green';
}

function clearMessage() {
    const el = document.getElementById('loginMessage');
    if (el) el.textContent = '';
}