document.addEventListener('DOMContentLoaded', function() {
    cargarMateriales();
});

function cargarMateriales() {
    showLoading(true);
    hideError();
    
    fetch('/biblioteca-web/ordenarMaterialesPorPendientes')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al conectar con el servidor(status: ' + response.status + ')');
            }
            return response.json();
        })
        .then(data => {
            showLoading(false);
            mostrarMaterialesConPendientes(data);
            console.log('Materiales cargados:', data);  
        })
        .catch(error => {
            showLoading(false);
            showError('Error al cargar materiales: ' + error.message);
            console.error('Error:', error);
        });
}

function mostrarMaterialesConPendientes(materiales){
    const tbody = document.getElementById('materialesTableBody');
    tbody.innerHTML = '';
    
    if (!materiales || materiales.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">No hay Materiales registrados</td></tr>';
        return;
    }
    
    materiales.forEach(item => {
        const row = document.createElement('tr');
        const indice = (item.Indice !== undefined && item.Indice !== null) ? item.Indice : 'N/A';
        const idMaterial = (item.IDMaterial !== undefined && item.IDMaterial !== null) ? item.IDMaterial : 'N/A';
        const prestamosPendientes = (item.PrestamosPendientes !== undefined && item.PrestamosPendientes !== null) ? item.PrestamosPendientes : 'N/A';

        // Crear celda de datos
        const html = `
            <td data-label="Indice:">${indice}</td>
            <td data-label="Material:">${idMaterial}</td>
            <td data-label="Prestamos:">${prestamosPendientes}</td>
        `;
        row.innerHTML = html;

        // Crear boton 'Mas info'
        const btnCell = document.createElement('td');
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'btn btn-sm ver-mas btn-primary';
        btn.textContent = 'Mas info';
        btn.dataset.materialId = idMaterial;
        btn.addEventListener('click', function() {
            const mid = this.dataset.materialId;
            fetchPrestamosPorMaterial(mid);
        });
        btnCell.appendChild(btn);
        row.appendChild(btnCell);

        tbody.appendChild(row);
    });
    
    console.log('Total materiales mostrados:', materiales.length);
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

// Fetch and display prestamos pendientes for a specific material in the modal
function fetchPrestamosPorMaterial(materialId) {
    if (!materialId) return;
    const modalBody = document.getElementById('prestamosModalBody');
    const modalTitle = document.getElementById('prestamosModalLabel');
    if (modalBody) modalBody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando...</td></tr>';
    if (modalTitle) modalTitle.textContent = 'Préstamos pendientes - Material ' + materialId;

    fetch('/biblioteca-web/ordenarMaterialesPorPendientes?materialId=' + encodeURIComponent(materialId))
        .then(resp => {
            if (!resp.ok) throw new Error('Error al obtener préstamos (status: ' + resp.status + ')');
            return resp.json();
        })
        .then(data => {
            // data expected to be an array of prestamos
            if (!Array.isArray(data) || data.length === 0) {
                if (modalBody) modalBody.innerHTML = '<tr><td colspan="6" class="text-center">No hay préstamos pendientes para este material</td></tr>';
            } else {
                const rows = data.map(p => {
                    // DtPrestamo JSON fields: idPrestamo, fechaSoli, estadoPres, fechaDev, lector, bibliotecario, material
                    const id = p.idPrestamo !== undefined ? escapeHtml(p.idPrestamo) : '';
                    const fs = formatDateISOFromValue(p.fechaSoli);
                    const fd = formatDateISOFromValue(p.fechaDev);
                    const estado = p.estadoPres ? escapeHtml(String(p.estadoPres)) : '';
                    const lector = p.lector ? escapeHtml(p.lector) : '';
                    const bib = p.bibliotecario ? escapeHtml(p.bibliotecario) : '';
                    return `<tr><td>${id}</td><td>${fs}</td><td>${fd}</td><td>${estado}</td><td>${lector}</td><td>${bib}</td></tr>`;
                }).join('');
                if (modalBody) modalBody.innerHTML = rows;
            }

            // show modal
            const modalEl = document.getElementById('prestamosModal');
            if (modalEl) {
                const modal = new bootstrap.Modal(modalEl);
                modal.show();
            }
        })
        .catch(err => {
            console.error('Error fetching prestamos por material:', err);
            if (modalBody) modalBody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">' + escapeHtml(err.message || 'Error') + '</td></tr>';
            const modalEl = document.getElementById('prestamosModal');
            if (modalEl) {
                const modal = new bootstrap.Modal(modalEl);
                modal.show();
            }
        });
}

// Simple HTML escaper used by this file
function escapeHtml(s) {
    if (s == null) return '';
    return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// Devuelve fecha en formato ISO 'yyyy-mm-dd' para mostrar en tablas/inputs.
// Acepta timestamps numéricos (ms), ISO strings, o ya-formateadas y hace fallback a cadena vacía.
function formatDateISOFromValue(value) {
    if (value == null || value === '') return '';

    // Si ya está en formato yyyy-mm-dd
    if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(value)) return value;

    // Si es número (puede venir como número o como string con dígitos)
    if (typeof value === 'number' || (/^\d+$/.test(String(value)))) {
        const ms = Number(value);
        // Si el número parece excesivamente grande pero en ms aún es plausible, intentar
        if (!isNaN(ms)) {
            const d = new Date(ms);
            if (!isNaN(d.getTime())) return formatDateISOFromDate(d);
        }
    }

    // Intentar parsear con Date (ISO u otros)
    const d = new Date(value);
    if (!isNaN(d.getTime())) return formatDateISOFromDate(d);

    // Fallback: devolver cadena vacía (no mostrar timestamps raros)
    return '';
}

function formatDateISOFromDate(d) {
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
}