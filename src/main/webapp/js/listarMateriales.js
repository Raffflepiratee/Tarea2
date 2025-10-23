document.addEventListener('DOMContentLoaded', function() {
    cargarMateriales();
});

function cargarMateriales() {
    showLoading(true);
    hideError();
    
    fetch('/biblioteca-web/listarMateriales')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al conectar con el servidor');
            }
            return response.json();
        })
        .then(data => {
            showLoading(false);
            mostrarMateriales(data);
            console.log('Materiales cargados:', data);
        })
        .catch(error => {
            showLoading(false);
            showError('Error al cargar materiales: ' + error.message);
            console.error('Error:', error);
        });
}

function mostrarMateriales(materiales){
    const tbody = document.getElementById('materialesTableBody');
    tbody.innerHTML = '';
    
    if (!materiales || materiales.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">No hay Materiales registrados</td></tr>';
        return;
    }
    
    materiales.forEach(materiales => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${materiales.IDMaterial || 'N/A'}</td>
            <td>${materiales.FechaRegistro || 'N/A'}</td>
            <td>${materiales.Tipo || 'N/A'}</td>
            <td>${materiales.Detalles || 'N/A'}</td>
        `;
        tbody.appendChild(row);
    });
    
    console.log('Total materiales mostrados:', materiales.length);
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