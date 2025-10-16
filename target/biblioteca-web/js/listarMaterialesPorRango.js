document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const resultsEl = document.getElementById('results');

    if (!form) return;

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const fechaInicio = document.getElementById('fechaInicio')?.value || '';
        const fechaFin = document.getElementById('fechaFin')?.value || '';

        console.log('fechaInicio =', fechaInicio);
        console.log('fechaFin    =', fechaFin);

        const body = new URLSearchParams();
        body.append('fechaInicio', fechaInicio);
        body.append('fechaFin', fechaFin);

        try {
            fetch( '/biblioteca-web/listarMaterialesPorRango', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: body.toString()
            });

            if (!resp.ok) {
                const text = await resp.text();
                console.error('Server error', resp.status, text);
                if (resultsEl) resultsEl.innerHTML = `<div class="error">Error del servidor: ${escapeHtml(text)}</div>`;
                return;
            }

            const data = await resp.json();
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
                return `<tr><td>${escapeHtml(id)}</td><td>${escapeHtml(fecha)}</td><td>${escapeHtml(tipo)}</td><td>${escapeHtml(detalles)}</td></tr>`;
            });

            resultsEl.innerHTML = `<table border="1" style="border-collapse:collapse"><thead><tr><th>ID</th><th>Fecha registro</th><th>Tipo</th><th>Detalles</th></tr></thead><tbody>${rows.join('')}</tbody></table>`;

        } catch (err) {
            console.error('Fetch error:', err);
            if (resultsEl) resultsEl.innerHTML = `<div class="error">Error de red: ${escapeHtml(err.message || String(err))}</div>`;
        }
    });
});

function escapeHtml(s) {
    if (s == null) return '';
    return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
}