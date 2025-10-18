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
    
    materiales.forEach(materiales => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${materiales.Indice || 'N/A'}</td>
            <td>${materiales.IDMaterial || 'N/A'}</td>
            <td>${materiales.PrestamosPendientes || 'N/A'}</td>
        `;
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