document.addEventListener('DOMContentLoaded', function () {
    // Obtener correo del bibliotecario desde localStorage
    const correo = (localStorage.getItem('correo') || '').trim();
    console.log("Correo del bibliotecario cargado desde sesión:", correo || 'no disponible');

    if (!correo) {
        // Mostrar error y un mensaje en la tabla si no hay correo en sesión
        showError('Correo del bibliotecario no disponible en la sesión');
        const tbody = document.getElementById('prestamosTableBody');
        if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-warning">Correo del bibliotecario no disponible. Inicie sesión.</td></tr>';
        return;
    }

    // Cargar automáticamente los préstamos para el bibliotecario en sesión
    cargarPrestamosPorBibliotecario(correo);
});

function cargarPrestamosPorBibliotecario(correo) {
    showLoading(true);
    hideError();
    const tbody = document.getElementById('prestamosTableBody');
    if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando préstamos...</td></tr>';

    const body = new URLSearchParams();
    body.append('bibliotecario', correo);

    fetch('/biblioteca-web/prestamosPorBibliotecario', {
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
            // Log the raw response for debugging: status and body
            console.error('Invalid JSON response while fetching prestamosPorBibliotecario', { status: response.status, statusText: response.statusText, body: text });

            if (!response.ok) throw new Error(text || response.statusText || 'Error del servidor');

            // return response;

            // Otherwise it's a 200 but invalid JSON
            // Include a short excerpt of the body to aid debugging without flooding the UI
            const excerpt = (text || '').slice(0, 1000);
            throw new Error('Respuesta inválida del servidor: ' + (excerpt ? excerpt + (text.length > 1000 ? '... (truncated)' : '') : 'contenido vacío'));
        }

        if (!response.ok) {
            // If the server returned a JSON body with an error field, let the next `.then` handle it
            // by returning the parsed data. If there was no JSON body, create a small object
            // so the downstream error handling shows a friendly message instead of raw text.
            if (!data) data = { error: (text || response.statusText || 'Error del servidor') };
            return data;
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
        // Normalize error message and log full object for debugging
        let message = 'Error al cargar préstamos';
        try {
            if (err && err.message) message = err.message;
            else if (typeof err === 'string') message = err;
            else if (err && typeof err === 'object') message = JSON.stringify(err);
        } catch (ex) {
            message = String(err);
        }

        console.error('Fetch error (prestamosPorBibliotecario):', err);
        if (err && err.stack) console.error(err.stack);

        const tbody = document.getElementById('prestamosTableBody');
        const safe = escapeHtml((message || 'Error').toString()).slice(0, 2000);
        if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">' + safe + '</td></tr>';
        showError(message || 'Error al cargar préstamos');
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
