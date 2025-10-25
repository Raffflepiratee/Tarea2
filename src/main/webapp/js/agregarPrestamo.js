document.addEventListener('DOMContentLoaded', function() {
    const tipoMaterialSelect = document.getElementById('tipoMaterial');
    if (tipoMaterialSelect) {
        tipoMaterialSelect.addEventListener('change', function() {
            if (typeof mostrarCampos === 'function') mostrarCampos(this.value);
        });
    }

    const correoLector = localStorage.getItem('correo') || '';

    const registrarBtn = document.getElementById('registrarPrestamo');
    if (registrarBtn) {
        registrarBtn.addEventListener('click', function(event) {
            event.preventDefault();

            const idMaterialEl = document.getElementById('idMaterial');
            const fechaInicialEl = document.getElementById('fechaInicial');
            const fechaFinalEl = document.getElementById('fechaFinal');

            const idMaterial = idMaterialEl ? idMaterialEl.value : '';
            const fechaInicial = fechaInicialEl ? fechaInicialEl.value : '';
            const fechaFinal = fechaFinalEl ? fechaFinalEl.value : '';

            const body = new URLSearchParams();
            body.append('material', idMaterial);
            body.append('lector', correoLector);
            body.append('fechaSoli', fechaInicial);
            body.append('fechaDev', fechaFinal);

            const correoBibliotecario = 'admin@admin.com';
            body.append('bibliotecario', correoBibliotecario);

            console.log('Request body:', body.toString());

            fetch('/biblioteca-web/agregarPrestamo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
                body: body.toString()
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error('Server error: ' + text); });
                }
                return response.json();
            })
            .then(data => {
                console.log('Prestamo registrado:', data);
                const form = document.getElementById('agregarPrestamoForm');
                if (form) form.reset();
                mostrarMensaje('Prestamo agregado correctamente', 'success');

                const modalEl = document.getElementById('exampleModal');
                if (modalEl) {
                    const modalInstance = bootstrap.Modal.getInstance(modalEl) || bootstrap.Modal.getOrCreateInstance(modalEl);
                    if (modalInstance && typeof modalInstance.hide === 'function') {
                        modalInstance.hide();
                    }
                }
            })
            .catch(error => {
                console.error('Error al agregar:', error);
                let msg = (error && error.message) ? error.message : String(error);
                const serverPrefix = 'Server error:';
                if (msg.startsWith(serverPrefix)) {
                    const jsonPart = msg.substring(serverPrefix.length).trim();
                    try {
                        const parsed = JSON.parse(jsonPart);
                        msg = parsed && (parsed.error || parsed.message) ? (parsed.error || parsed.message) : jsonPart;
                    } catch (e) {
                        msg = jsonPart;
                    }
                }
                mostrarMensaje(msg, 'danger');
            });
        });
    }
});

    function showError(message) {
        const el = document.getElementById('errorMessage');
        if (el) {
            el.textContent = message;
            el.style.display = 'block';
        } else {
            console.error('showError: #errorMessage not found. Message:', message);
        }
    }

    function mostrarMensaje(texto, tipo) {
        const mensaje = document.getElementById('mensajeResultado');
        if (mensaje) {
            mensaje.textContent = texto;
            mensaje.className = 'alert alert-' + tipo;
            mensaje.classList.remove('d-none');

            setTimeout(() => {
                mensaje.classList.add('d-none');
            }, 3000);
        } else {
            console.warn('mostrarMensaje: #mensajeResultado not found. Message:', texto);
        }
    }