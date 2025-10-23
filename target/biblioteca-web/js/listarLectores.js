// const userType = localStorage.getItem('tipoUsuario');
// if (userType !== 'bibliotecario') {
//     console.log('Usuario no bibliotecario detectado, redirigiendo al login.');
//     window.location.assign('/biblioteca-web/login.jsp');
// }

const logoutBtnEl = document.getElementById('logoutBtn');
if (logoutBtnEl) {
    logoutBtnEl.addEventListener('click', () => {
        localStorage.removeItem('tipoUsuario');
        window.location.assign('/biblioteca-web/login.jsp');
    });
}

// Cargar usuarios al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    cargarUsuarios();
});

let correoUsuario = '';

function cargarUsuarios() {
    showLoading(true);
    hideError();
    
    fetch('/biblioteca-web/listarLectores?action=list')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al conectar con el servidor');
            }
            return response.json();
        })
        .then(data => {
            showLoading(false);
            mostrarUsuarios(data);
            console.log('Usuarios cargados:', data);
        })
        .catch(error => {
            showLoading(false);
            showError('Error al cargar usuarios: ' + error.message);
            console.error('Error:', error);
        });
}

function mostrarUsuarios(usuarios) {
    const tbody = document.getElementById('usuariosTableBody');
    tbody.innerHTML = '';
    
    if (!usuarios || usuarios.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay usuarios registrados</td></tr>';
        return;
    }
    
    usuarios.forEach(usuario => {
        const row = document.createElement('tr');
        // Escapar comillas en zona/estado para insertar en onclick
        const zonaVal = usuario.zona ? usuario.zona.replace(/'/g, "\\'") : '';
        const estadoVal = usuario.estado ? usuario.estado.replace(/'/g, "\\'") : '';

        row.innerHTML = `
            <td>${usuario.nombre || 'N/A'}</td>
            <td>${usuario.correo || 'N/A'}</td>
            <td>${usuario.tipo || 'N/A'}</td>
            <td>${usuario.detalles || 'N/A'}</td>
            <td><button type="button" class="btn btn-primary" onclick="asignarCorreo('${usuario.correo}','${zonaVal}','${estadoVal}')">Modificar</button></td>
        `;
        tbody.appendChild(row);
    });
    
    console.log('Total usuarios mostrados:', usuarios.length);
}

function showLoading(show) {
    const loadingEl = document.querySelector('.loading');
    if (loadingEl) loadingEl.style.display = show ? 'block' : 'none';
}

function showError(message) {
    const errorSpan = document.getElementById('errorMessage');
    const errorContainer = document.querySelector('.error');
    if (errorSpan && errorContainer) {
        errorSpan.textContent = message;
        errorContainer.classList.remove('d-none');
    } else {
        console.warn('showError fallback - elements not found, message:', message);
        try { alert(message); } catch (e) { /* ignore */ }
    }
}

function hideError() {
    const errorContainer = document.querySelector('.error');
    if (errorContainer) {
        errorContainer.classList.add('d-none');
    }
}

document.getElementById('modificarUsuarioForm').addEventListener('click', function(event) {
    event.preventDefault();
    const zona = document.getElementById('zona').value;
    const estadoRaw = document.getElementById('estadoU').value;

    // Validar que se haya seleccionado un estado
    if (!estadoRaw) {
        showError('Seleccione un estado para el usuario antes de guardar.');
        return;
    }

    // Mapear el valor del select al enum esperado por el servidor (mayúsculas)
    const estado = estadoRaw.toUpperCase();

    // console.log('Zona ingresada:', zona);
    // console.log('Estado seleccionado (raw):', estadoRaw);
    // console.log('Estado enviado (enum):', estado);

    const body =  new URLSearchParams({ correo: correoUsuario, zona: zona, estado: estado });
    // console.log('Preparing to send update with correoUsuario=', correoUsuario);
    console.log('Request body:', body.toString());

    fetch('/biblioteca-web/listarLectores?action=update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body:body.toString()
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error('Server error: ' + text); });
        }
        return response.json();
    })
    .then(data => {
        console.log('Usuario actualizado:', data);
        bootstrap.Modal.getInstance(document.getElementById('exampleModal')).hide();
        cargarUsuarios();
    })
    .catch(error => {
        console.error('Error al actualizar:', error);
        showError('No se pudo actualizar el usuario');
    });
});

function asignarCorreo(correo, zona, estado) {
    correoUsuario = correo;
    console.log('Correo asignado para modificación:', correoUsuario);

    // Preseleccionar la zona si está disponible
    if (zona) {
        const zonaSelect = document.getElementById('zona');
        zonaSelect.value = zona;
    }

    // Preseleccionar el estado si está disponible
    if (estado) {
        const estadoSelect = document.getElementById('estadoU');
        // estado ya viene en mayúsculas desde el servidor (ACTIVO/SUSPENDIDO)
        estadoSelect.value = estado;
    }

    // Abrir modal (en caso que el llamado no venga del data-bs-toggle)
    const modalEl = document.getElementById('exampleModal');
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();
}

