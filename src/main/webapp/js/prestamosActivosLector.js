document.addEventListener('DOMContentLoaded', function () {
	const form = document.querySelector('form#prestamosActivosForm') || document.querySelector('form');
	const buscarBtn = document.getElementById('buscarPrestamosActivos');

	if (form) {
		form.addEventListener('submit', function (e) {
			e.preventDefault();
			const correo = document.getElementById('lectorEmail')?.value?.trim() || '';
			if (!correo) { showError('Ingrese el correo del lector'); return; }
			cargarPrestamosActivos(correo);
		});
		return;
	}

	if (buscarBtn) {
		buscarBtn.addEventListener('click', function (e) {
			e.preventDefault();
			const correo = document.getElementById('lectorEmail')?.value?.trim() || '';
			if (!correo) { showError('Ingrese el correo del lector'); return; }
			cargarPrestamosActivos(correo);
		});
	}

	// allow enter on input when no form
	const input = document.getElementById('lectorEmail');
	if (input) {
		input.addEventListener('keydown', function (e) {
			if (e.key === 'Enter') {
				e.preventDefault();
				const correo = input.value.trim();
				if (!correo) { showError('Ingrese el correo del lector'); return; }
				cargarPrestamosActivos(correo);
			}
		});
	}
});

function cargarPrestamosActivos(correo) {
	showLoading(true);
	hideError();
	const tbody = document.getElementById('prestamosTableBody');
	if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando préstamos...</td></tr>';

	const body = new URLSearchParams();
	body.append('lector', correo);

	fetch('/biblioteca-web/prestamosActivosLector', {
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
			// If parsing failed and server returned non-OK, include raw text in error
			if (!response.ok) throw new Error(text || response.statusText || 'Error del servidor');
			// If parsing failed but response is OK, treat as unexpected payload
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

		// If server returned an object with an error or message field, show it
		if (data && typeof data === 'object' && !Array.isArray(data)) {
			if (data.error) {
				showError(data.error);
				const tbody = document.getElementById('prestamosTableBody');
				if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-dark">' + escapeHtml(data.error) + '</td></tr>';
				return;
			}
			if (data.message) {
				showError(data.message);
				const tbody = document.getElementById('prestamosTableBody');
				if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center text-dark">' + escapeHtml(data.message) + '</td></tr>';
				return;
			}
		}

		// If server returned an array (possibly empty), render it
		if (Array.isArray(data)) {
			if (data.length === 0 || prestamoVaciosLector(data)) {
				const tbody = document.getElementById('prestamosTableBody');
				if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">El lector no tiene préstamos activos</td></tr>';
				return;
			}
			// Normal non-empty array -> render
			renderPrestamos(data);
			return;
		}

		// If data is null (empty body), treat as no loans
		if (data == null) {
			const tbody = document.getElementById('prestamosTableBody');
			if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">El lector no tiene préstamos activos</td></tr>';
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

function renderPrestamos(prestamos) {
	const tbody = document.getElementById('prestamosTableBody');
	if (!tbody) return;
	tbody.innerHTML = '';
	if (!prestamos || prestamos.length === 0) {
		tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay préstamos activos para este lector</td></tr>';
		return;
	}

	let rowsHtml = '';
	prestamos.forEach(p => {
		const fechaS = formatDate(p.fechaSoli);
		const fechaD = formatDate(p.fechaDev);
		const id = escapeHtml(p.id || '');
		const estado = escapeHtml(p.estadoP || '');
		const idMaterial = escapeHtml(p.idMaterial || '');
		const correoB = escapeHtml(p.correoB || '');
		rowsHtml += `<tr><td data-label="ID: ">${id}</td><td data-label="Solicitud: ">${escapeHtml(fechaS)}</td><td data-label="Fecha Devolución: ">${escapeHtml(fechaD)}</td><td data-label="Estado: ">${estado}</td><td data-label="ID Material: ">${idMaterial}</td><td data-label="Correo Bibliotecario: ">${correoB}</td></tr>`;
	});
	tbody.innerHTML = rowsHtml;
}

// New helper: mostrarPrestamosLector is a thin wrapper kept for compatibility
function mostrarPrestamosLector(prestamos) {
	renderPrestamos(prestamos);
}

// Detect arrays that only contain default/empty placeholder prestamos
function prestamoVaciosLector(prestamos) {
	if (!Array.isArray(prestamos) || prestamos.length === 0) return true;
	return prestamos.every(p =>
		(!p || (!p.id || p.id === 0 || p.id === '0')) &&
		(!p || !p.fechaSoli || String(p.fechaSoli).trim() === '') &&
		(!p || !p.fechaDev || String(p.fechaDev).trim() === '') &&
		(!p || !p.estadoP || String(p.estadoP).trim() === '') &&
		(!p || !p.idMaterial || p.idMaterial === 0 || p.idMaterial === '0') &&
		(!p || !p.correoB || String(p.correoB).trim() === '')
	);
}

function formatDate(value) {
	if (!value) return '';
	// already dd/MM/yyyy from servlet, but try to parse if needed
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

