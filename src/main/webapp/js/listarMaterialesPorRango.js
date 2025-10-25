document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const resultsEl = document.getElementById('materialesBody');

    if (!form) return;

    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        resultsEl.innerHTML = '';
        const fechaInicio = document.getElementById('fechaInicio')?.value || '';
        const fechaFin = document.getElementById('fechaFin')?.value || '';

        console.log('fechaInicio = ', fechaInicio);
        console.log('fechaFin    = ', fechaFin);

        const body = new URLSearchParams();
        body.append('fechaInicio', fechaInicio);
        body.append('fechaFin', fechaFin);

        console.log('Cuerpo de la solicitud:', body.toString());

        fetch('/biblioteca-web/listarMaterialesPorRango', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: body.toString()
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error('Server error: ' + text); });
            }
            return response.text().then(text => {
                try {
                    return JSON.parse(text);
                } catch (e) {
                    throw new Error('Invalid JSON from server: ' + text);
                }
            });
        })
        .then(data => {
            console.log('Datos recibidos:', data);

            if (!resultsEl) return;
            if (!Array.isArray(data) || data.length === 0) {
                resultsEl.innerHTML = '<p>No se encontraron materiales en ese rango.</p>';
                return;
            }

            const rows = data.map(item => {
                const id = item['ID Material'] || item.id || '';
                const fecha = item['Fecha de Registro'] || item.fechaRegistro || '';
                const tipo = item['Tipo'] || item.tipo || '';
                const detalles = item['Detalles'] || item.detalles || '';
                // Escape HTML then convert newlines to <br> for rendering
                const detallesHtml = escapeHtml(detalles).replace(/\n/g, '<br>');
                resultsEl.innerHTML += `<tr><td data-label="ID Material">${escapeHtml(id)}</td><td data-label="Fecha">${escapeHtml(fecha)}</td><td data-label="Tipo">${escapeHtml(tipo)}</td><td data-label="Detalles">${detallesHtml}</td></tr>`;
            });

            //resultsEl.innerHTML = `<table border="1" style="border-collapse:collapse"><thead><tr><th>ID</th><th>Fecha registro</th><th>Tipo</th><th>Detalles</th></tr></thead><tbody>${rows.join('')}</tbody></table>`;
        })
        .catch(err => {
            console.error('Fetch error:', err);
            if (resultsEl) resultsEl.innerHTML = `<div class="error">Error: ${escapeHtml(err.message || String(err))}</div>`;
        });
    });
});

function escapeHtml(s) {
    if (s == null) return '';
    return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
}

