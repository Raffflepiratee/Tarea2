document.addEventListener('DOMContentLoaded', function() {
    initListarPrestamos();
});

let cachedPrestamos = [];

function initListarPrestamos() {
    const btnListar = document.getElementById('btnListarPrestamos');
    if (btnListar) {
        btnListar.addEventListener('click', function () {
            const zonaInput = document.getElementById('filtroZona');
            const zona = zonaInput ? zonaInput.value.trim() : '';
            if (!zona) {
                showError('Debe ingresar un zona válido');
                return;
            }
            cargarPrestamosPorZona(zona);
        });
    }

    const filtro = document.getElementById('filtroEstado');
    if (filtro) {
        filtro.addEventListener('change', function () {
            applyFiltroEstado(this.value);
        });
    }
}

function cargarPrestamosPorZona(zona) {
    if (!zona || zona.trim() === '') {
        showLoading(false);
        showError('Zona no puede estar sin seleccionar');
        return;
    }

    showLoading(true);
    hideError();

    const body = new URLSearchParams();
    body.append('zona', zona);

    fetch('/biblioteca-web/listarPrestamosPorZona', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: body.toString()
    }).then(response => {
            if (!response.ok) throw new Error('Error al conectar con el servidor');
            return response.json();
        })
        .then(data => {
            showLoading(false);
            cachedPrestamos = data || [];

            const contador = document.getElementById('contadorPrestamos');
            if (contador) {
                const prestamosValidos = cachedPrestamos.filter(p =>
                    p.idPrestamo && p.idPrestamo !== "0" &&
                    p.fechaSoli && p.estadoP
                );
                contador.textContent = `Total: ${prestamosValidos.length}`;
            }

            applyFiltroEstado(document.getElementById('filtroEstado').value);
            console.log('Prestamos por zona cargados:', cachedPrestamos.length);
        })
        .catch(err => {
            showLoading(false);
            showError('Error al cargar préstamos por zona: ' + (err && err.message ? err.message : err));
            console.error(err);
        });
}

function mostrarPrestamos(prestamos) {
    const tbody = document.getElementById('prestamosTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';

    if (!prestamos || prestamos.length === 0 || prestamoVacios(prestamos)) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">No hay préstamos registrados</td></tr>';
        return;
    }

    prestamos.forEach(p => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${p.idPrestamo || 'N/A'}</td>
            <td>${p.fechaSoli || 'N/A'}</td>
            <td>${p.fechaDev || 'N/A'}</td>
            <td>${p.estadoP || 'N/A'}</td>
            <td>${p.lector || 'N/A'}</td>
            <td>${p.material || 'N/A'}</td>
            <td>${p.bibliotecario || 'N/A'}</td>
        `;
        tbody.appendChild(row);
    });
}

function applyFiltroEstado(estado) {
    if (!cachedPrestamos || cachedPrestamos.length === 0) return;

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

function prestamoVacios(prestamos) {
    return prestamos.every(p =>
        (!p.idPrestamo || p.idPrestamo === "0") &&
        (!p.fechaSoli || p.fechaSoli.trim() === "") &&
        (!p.fechaDev || p.fechaDev.trim() === "") &&
        (!p.estadoP || p.estadoP.trim() === "") &&
        (!p.lector || p.lector.trim() === "") &&
        (!p.material || p.material === "0") &&
        (!p.bibliotecario || p.bibliotecario.trim() === "")
    );
}