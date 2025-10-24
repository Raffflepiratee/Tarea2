document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form#prestamosForm') || document.querySelector('form');
    const buscarBtn = document.getElementById('buscarPrestamos');

    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            const correo = document.getElementById('biblioEmail')?.value?.trim() || '';
            if (!correo) { showError('Ingrese el correo del bibliotecario'); return; }
            cargarPrestamosPorBibliotecario(correo);
        });
        return;
    }

    if (buscarBtn) {
        buscarBtn.addEventListener('click', function (e) {
            e.preventDefault();
            const correo = document.getElementById('biblioEmail')?.value?.trim() || '';
            if (!correo) { showError('Ingrese el correo del bibliotecario'); return; }
            cargarPrestamosPorBibliotecario(correo);
        });
    }

    // allow enter on input when no form
    const input = document.getElementById('biblioEmail');
    if (input) {
        input.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const correo = input.value.trim();
                if (!correo) { showError('Ingrese el correo del bibliotecario'); return; }
                cargarPrestamosPorBibliotecario(correo);
            }
        });
    }
});

function cargarPrestamosPorBibliotecario(correo) {
    showLoading(true);
    hideError();
    const tbody = document.getElementById('prestamosTableBody');
    if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando préstamos...</td></tr>';

    const body = new URLSearchParams();
    body.append('bibliotecario', correo);

    fetch(window.location.pathname.replace(/\/[^/]*$/, '') + '/prestamosPorBibliotecario', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
        body: body.toString()
    })
    .then(async response => {
        const text = await response.text();
        let data = null;
        try {
            data = text ? JSON.parse(text) : null;
        } catch (e) {
            if (!response.ok) throw new Error(text || response.statusText || 'Error del servidor');
            throw new Error('Respuesta inválida del servidor');
        }

        if (!response.ok) {
            const msg = (data && data.error) ? data.error : (text || response.statusText || 'Error del servidor');
            throw new Error(msg);
        }

        return data;
    })
    .then(data => {
        showLoading(false);

        // If server returned an object (not an array) treat as message/error container
        if (data && typeof data === 'object' && !Array.isArray(data)) {
            if (data.error) {
                showError(data.error);
                const tbody = document.getElementById('prestamosTableBody');
                if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-warning">' + escapeHtml(data.error) + '</td></tr>';
                return;
            }
            if (data.message) {
                // Informational message (e.g. "Bibliotecario no encontrado")
                showError(data.message);
                const tbody = document.getElementById('prestamosTableBody');
                if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-warning">' + escapeHtml(data.message) + '</td></tr>';
                return;
            }
        }

        // If server returned an array (expected)
        if (Array.isArray(data)) {
            if (data.length === 0) {
                const tbody = document.getElementById('prestamosTableBody');
                if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">El bibliotecario no tiene préstamos</td></tr>';
                return;
            }
            renderPrestamosPorBibliotecario(data);
            return;
        }

        // If data is null (empty body), treat as no loans
        if (data == null) {
            const tbody = document.getElementById('prestamosTableBody');
            if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">El bibliotecario no tiene préstamos</td></tr>';
            return;
        }

        // Unexpected payload
        showError('Respuesta inesperada del servidor');
        const tbodyErr = document.getElementById('prestamosTableBody');
        if (tbodyErr) tbodyErr.innerHTML = '<tr><td colspan="6" class="text-center text-danger">Respuesta inesperada</td></tr>';
    })
    .catch(err => {
        showLoading(false);
        console.error('Fetch error:', err);
        const tbody = document.getElementById('prestamosTableBody');
        if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">' + escapeHtml(err.message || 'Error') + '</td></tr>';
        showError(err && err.message ? err.message : 'Error al cargar préstamos');
    });
}

function renderPrestamosPorBibliotecario(prestamos) {
    const tbody = document.getElementById('prestamosTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';
    if (!prestamos || prestamos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay préstamos para este bibliotecario</td></tr>';
        return;
    }

    let rowsHtml = '';
    prestamos.forEach(p => {
        const fechaS = formatDate(p.fechaSoli);
        const fechaD = formatDate(p.fechaDev);
        const id = escapeHtml(p.id || '');
        const estado = escapeHtml(p.estadoP || '');
        const idMaterial = escapeHtml(p.idMaterial || '');
        const correoL = escapeHtml(p.correoL || '');
        rowsHtml += `<tr><td>${id}</td><td>${escapeHtml(fechaS)}</td><td>${escapeHtml(fechaD)}</td><td>${estado}</td><td>${idMaterial}</td><td>${correoL}</td></tr>`;
    });
    tbody.innerHTML = rowsHtml;
}

// Helpers (same as prestamosActivosLector)
function formatDate(value) {
    if (!value) return '';
    const m = String(value).match(/(\d{2})\/(\d{2})\/(\d{4})/);
    if (m) return value;
    const d = new Date(value);
    if (isNaN(d.getTime())) return value;
    const dd = String(d.getDate()).padStart(2,'0');
    const mm = String(d.getMonth()+1).padStart(2,'0');
    const yyyy = d.getFullYear();
    return dd + '/' + mm + '/' + yyyy;
}

function escapeHtml(s) {
    if (s == null) return '';
    return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
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
