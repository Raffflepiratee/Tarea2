document.addEventListener('DOMContentLoaded', function() {
    initListarPrestamos();
});

let cachedPrestamos = [];
let idPrestamoModificar = null;

function initListarPrestamos() {
    cargarPrestamos();

    const modificarBtn = document.getElementById('modificarPrestamoForm');
    if (modificarBtn) {
        modificarBtn.addEventListener('click', function (ev) {
            ev.preventDefault();
            modificarBtn.disabled = true;
            enviarModificarPrestamo().finally(() => { modificarBtn.disabled = false; });
        });
    }

    const filtro = document.getElementById('filtroEstado');
    if (filtro) {
        filtro.addEventListener('change', function () {
            applyFiltroEstado(this.value);
        });
    }
}

function cargarPrestamos() {
    showLoading(true);
    hideError();

    fetch('/biblioteca-web/listarPrestamos')
        .then(response => {
            if (!response.ok) throw new Error('Error al conectar con el servidor');
            return response.json();
        })
        .then(data => {
            showLoading(false);
            cachedPrestamos = data || [];
            mostrarPrestamos(cachedPrestamos);
            console.log('Prestamos cargados:', cachedPrestamos.length);
        })
        .catch(err => {
            showLoading(false);
            showError('Error al cargar préstamos: ' + (err && err.message ? err.message : err));
            console.error(err);
        });
}

function mostrarPrestamos(prestamos) {
    const tbody = document.getElementById('prestamosTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';

    if (!prestamos || prestamos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">No hay préstamos registrados</td></tr>';
        return;
    }

    prestamos.forEach(p => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${p.id || 'N/A'}</td>
            <td>${p.fechaSoli || 'N/A'}</td>
            <td>${p.fechaDev || 'N/A'}</td>
            <td>${p.estadoP || 'N/A'}</td>
            <td>${p.correoL || 'N/A'}</td>
            <td>${p.idMaterial || 'N/A'}</td>
            <td>${p.correoB || 'N/A'}</td>
            <td><button type="button" class="btn btn-primary" onclick="asignarPrestamo('${p.id}','${p.estadoP}','${p.fechaSoli}','${p.fechaDev}','${p.correoL}','${p.idMaterial}','${p.correoB}')">Modificar</button></td>
        `;
        tbody.appendChild(row);
    });
}

function applyFiltroEstado(estado) {
    if (!estado || estado === 'ALL') {
        mostrarPrestamos(cachedPrestamos);
        return;
    }
    const filtered = cachedPrestamos.filter(p => (p.estadoP || '').toUpperCase() === estado.toUpperCase());
    mostrarPrestamos(filtered);
}

function showLoading(show) {
    const el = document.querySelector('.loading');
    if (el) el.style.display = show ? 'block' : 'none';
}

function showError(message) {
    const msgEl = document.getElementById('errorMessage');
    if (msgEl) msgEl.textContent = message;
    const errBox = document.querySelector('.error');
    if (errBox) errBox.style.display = 'block';
}

function hideError() {
    const errBox = document.querySelector('.error');
    if (errBox) errBox.style.display = 'none';
}

function showSuccess(message) {
    const msgEl = document.getElementById('successMessage');
    if (msgEl) msgEl.textContent = message;
    const box = document.querySelector('.success');
    if (box) box.style.display = 'block';
    setTimeout(() => {
        if (box) box.style.display = 'none';
    }, 4000);
}

function enviarModificarPrestamo() {
    const estadoPEl = document.getElementById('estadoP');
    const fechaSoliEl = document.getElementById('fechaSoli');
    const fechaDevEl = document.getElementById('fechaDev');
    const correoLEl = document.getElementById('correoL');
    const idMaterialEl = document.getElementById('idMaterial');
    const correoBEl = document.getElementById('correoB');

    const estadoP = estadoPEl ? estadoPEl.value : '';
    const fechaSolicitud = fechaSoliEl ? fechaSoliEl.value : '';
    const fechaDevolucion = fechaDevEl ? fechaDevEl.value : '';
    const correoL = correoLEl ? correoLEl.value : '';
    const idMaterial = idMaterialEl ? idMaterialEl.value : '';
    const correoB = correoBEl ? correoBEl.value : '';

    const body = new URLSearchParams();
    body.append('idPrestamo', idPrestamoModificar || '');
    body.append('estadoP', estadoP);
    body.append('fechaSoli', fechaSolicitud);
    body.append('fechaDev', fechaDevolucion);
    body.append('correoL', correoL);
    body.append('idMaterial', idMaterial);
    body.append('correoB', correoB);

    const modalErrorEl = document.getElementById('modalErrorMessage');
    if (modalErrorEl) { 
        modalErrorEl.style.display = 'none'; 
        modalErrorEl.textContent = ''; 
    }

    return fetch('/biblioteca-web/listarPrestamos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: body.toString()
    })
    .then(async response => {
        const text = await response.text();
        let msg = text;

        try {
            const obj = JSON.parse(text);
            msg = obj.message || 'Error del servidor';
        } catch (e) {
        }

        if (!response.ok) {
            limpiarErroresCampos();

            if (msg.includes('estado')) {
                mostrarErrorCampo('EstadoP', msg);
            }
            if (msg.includes('material')) {
                mostrarErrorCampo('IdMaterial', msg);
            }
            if (msg.includes('solicitud')) {
                mostrarErrorCampo('FechaSoli', msg);
            }
            if (msg.includes('devolucion')) {
                mostrarErrorCampo('FechaDev', msg);
            }
            if (msg.includes('lector')) {
                mostrarErrorCampo('CorreoL', msg);
            }
            if (msg.includes('bibliotecario')) {
                mostrarErrorCampo('CorreoB', msg);
            }

            if (modalErrorEl) {
                modalErrorEl.textContent = msg;
                modalErrorEl.style.display = 'block';
            }

            throw new Error(msg);
        }
        return msg;
    })
    .then(data => {
        limpiarErroresCampos();
        showSuccess('Préstamo modificado exitosamente');
        const modalEl = document.getElementById('exampleModal');
        if (modalEl) {
            const modal = bootstrap.Modal.getInstance(modalEl) || bootstrap.Modal.getOrCreateInstance(modalEl);
            modal.hide();
        }
        cargarPrestamos();
    })
    .catch(err => {
        console.error('Error al modificar préstamo:', err);
    });
}

function asignarPrestamo(idPrestamo, estadoP, fechaSoli, fechaDev, correoL, idMaterial, correoB) {
    idPrestamoModificar = idPrestamo;

    const estadoPEl = document.getElementById('estadoP');
    const fechaSoliEl = document.getElementById('fechaSoli');
    const fechaDevEl = document.getElementById('fechaDev');
    const correoLEl = document.getElementById('correoL');
    const idMaterialEl = document.getElementById('idMaterial');
    const correoBEl = document.getElementById('correoB');

    if (estadoPEl && estadoP) estadoPEl.value = estadoP;
    if (fechaSoliEl && fechaSoli) fechaSoliEl.value = fechaSoli;
    if (fechaDevEl && fechaDev) fechaDevEl.value = fechaDev;
    if (correoLEl && correoL) correoLEl.value = correoL;
    if (idMaterialEl && idMaterial) idMaterialEl.value = idMaterial;
    if (correoBEl && correoB) correoBEl.value = correoB;

    limpiarErroresCampos();

    const modalEl = document.getElementById('exampleModal');
    if (modalEl) {
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    }
}

function mostrarErrorCampo(campoId, mensaje) {
    const span = document.getElementById(`error${campoId}`);
    if (span) {
        span.textContent = mensaje;
        span.style.display = 'block';
    }
}

function limpiarErroresCampos() {
    const errores = document.querySelectorAll('span[id^="error"]');
    errores.forEach(e => {
        e.textContent = '';
        e.style.display = 'none';
    });
}