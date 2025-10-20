document.addEventListener('DOMContentLoaded', function() {
    cargarPrestamos();
});

let idPrestamoModificar = null;

function cargarPrestamos() {
    showLoading(true);
    hideError();
    
    fetch('/biblioteca-web/listarPrestamos')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al conectar con el servidor');
            }
            return response.json();
        })
        .then(data => {
            showLoading(false);
            mostrarPrestamos(data);
            console.log('Prestamos cargados:', data);
        })
        .catch(error => {
            showLoading(false);
            showError('Error al cargar prestamos: ' + error.message);
            console.error('Error:', error);
        });
}

function mostrarPrestamos(prestamos) {
    const tbody = document.getElementById('prestamosTableBody');
    tbody.innerHTML = '';
    
    if (!prestamos || prestamos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">No hay prestamos registrados</td></tr>';
        return;
    }
    
    prestamos.forEach(prestamos => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${prestamos.id || 'N/A'}</td>
            <td>${prestamos.fechaSoli || 'N/A'}</td>
            <td>${prestamos.fechaDev || 'N/A'}</td>
            <td>${prestamos.estadoP || 'N/A'}</td>
            <td>${prestamos.correoL || 'N/A'}</td>
            <td>${prestamos.idMaterial || 'N/A'}</td>
            <td>${prestamos.correoB || 'N/A'}</td>
            <td><button type="button" class="btn btn-primary" onclick="asignarPrestamo('${prestamos.id}', '${prestamos.estadoP}', '${prestamos.fechaSoli}', '${prestamos.fechaDev}', '${prestamos.correoL}', '${prestamos.idMaterial}', '${prestamos.correoB}')">Modificar</button></td>
        `;
        tbody.appendChild(row);
    });
    
    console.log('Total prestamos mostrados:', prestamos.length);
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

document.getElementById('modificarPrestamoForm').addEventListener('click', function(event) {
    event.preventDefault();
    const estadoP = document.getElementById('estadoP').value;
    const fechaSolicitud = document.getElementById('fechaSoli').value;
    const fechaDevolucion = document.getElementById('fechaDev').value;
    const correoL = document.getElementById('correoL').value;
    const idMaterial = document.getElementById('idMaterial').value;
    const correoB = document.getElementById('correoB').value;

    const body = new URLSearchParams();
    body.append('idPrestamo', idPrestamoSeleccionado);
    body.append('estadoP', estadoP);
    body.append('fechaSoli', fechaSolicitud);
    body.append('fechaDev', fechaDevolucion);
    body.append('correoL', correoL);
    body.append('idMaterial', idMaterial);
    body.append('correoB', correoB);

    console.log('Datos a enviar para modificar prestamo:', body.toString());

    fetch('/biblioteca-web/listarPrestamos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: body.toString(),
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error('Server error: ' + text); });
        }
        return response.json();
    })
    .then(data => {
        console.log('Prestamo actualizado:', data);
        bootstrap.Modal.getInstance(document.getElementById('exampleModal')).hide();
        cargarPrestamos();
    })
    .catch(error => {
        console.error('Error al actualizar:', error);
        showError('No se pudo actualizar el prestamo');
    });
});

function asignarPrestamo(idPrestamo, estadoP, fechaSoli, fechaDev, correoL, idMaterial, correoB) {
    idPrestamoSeleccionado = idPrestamo;
    console.log('ID préstamo asignado:', idPrestamoSeleccionado);

    if (estadoP) document.getElementById('estadoP').value = estadoP;
    if (fechaSoli) document.getElementById('fechaSoli').value = fechaSoli;
    if (fechaDev) document.getElementById('fechaDev').value = fechaDev;
    if (correoL) document.getElementById('correoL').value = correoL;
    if (idMaterial) document.getElementById('idMaterial').value = idMaterial;
    if (correoB) document.getElementById('correoB').value = correoB;

    const modalEl = document.getElementById('exampleModal');
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();
}