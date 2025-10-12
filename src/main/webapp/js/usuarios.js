// Cargar usuarios al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    cargarUsuarios();
});

let correoUsuario = '';

function cargarUsuarios() {
    showLoading(true);
    hideError();
    
    fetch('/biblioteca-web/usuarios?action=list')
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
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">No hay usuarios registrados</td></tr>';
        return;
    }
    
    usuarios.forEach(usuario => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${usuario.nombre || 'N/A'}</td>
            <td>${usuario.correo || 'N/A'}</td>
            <td>${usuario.tipo || 'N/A'}</td>
            <td>${usuario.detalles || 'N/A'}</td>
            <td><button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="asignarCorreo('${usuario.correo}')">Modificar</button></td>
        `;
        tbody.appendChild(row);
    });
    
    console.log('Total usuarios mostrados:', usuarios.length);
}

function showLoading(show) {
    document.querySelector('.loading').style.display = show ? 'block' : 'none';
}

function showError(message) {
    document.getElementById('errorMessage').textContent = message;
    document.querySelector('.error').style.display = 'block';
}

function hideError() {
    document.querySelector('.error').style.display = 'none';
}

document.getElementById('modificarUsuarioForm').addEventListener('click', function(event) {
    event.preventDefault();
    const zona = document.getElementById('zona').value;
    const estado = document.getElementById('estadoU').value;

    console.log('Zona ingresada:', zona);
    console.log('Estado seleccionado:', estado);

    fetch('/biblioteca-web/usuarios?action=update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ correo: correoUsuario, zona, estado })
    })
    .then(response => response.json())
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

function asignarCorreo(correo) {
    correoUsuario = correo;
    console.log('Correo asignado para modificación:', correoUsuario);
}

