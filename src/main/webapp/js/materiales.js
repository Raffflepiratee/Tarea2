document.addEventListener("DOMContentLoaded", function() {
    const tipoMaterialSelect = document.getElementById("tipoMaterial");
    tipoMaterialSelect.addEventListener("change", function() {
        mostrarCampos(this.value);
    });
});

function mostrarCampos(tipo){
    const libro = document.getElementById('camposLibro');
    const articulo = document.getElementById('camposArticulo');

    if (tipo === 'LIBRO') {
        libro.style.display = 'block';
        articulo.style.display = 'none';
    } else if (tipo === 'ARTICULO') {
        libro.style.display = 'none';
        articulo.style.display = 'block';
    } else {
        libro.style.display = 'none';
        articulo.style.display = 'none';
    }
}

document.getElementById('registrarMaterial').addEventListener('click', function(event) {
    event.preventDefault();
    const tipo = document.getElementById('tipoMaterial').value;

    const body =  new URLSearchParams();
    body.append('tipoMaterial', tipo);

    const hoy = new Date();
    const yyyy = hoy.getFullYear();
    const mm = String(hoy.getMonth() + 1).padStart(2, '0');
    const dd = String(hoy.getDate()).padStart(2, '0');
    const fechaRegistro = `${yyyy}-${mm}-${dd}`;
    body.append('fechaRegistro', fechaRegistro);

    
    if (tipo=='ARTICULO'){
        const descArticulo= document.getElementById('descArticulo').value;
        const peso = document.getElementById('peso').value;
        const dimFisica = document.getElementById('dimFisica').value;

        body.append('descripcion', descArticulo);
        body.append('peso', peso);
        body.append('dimFisica', dimFisica);
    } else if (tipo == 'LIBRO') {
        const titulo = document.getElementById('tituloLibro').value;
        const cantPag = document.getElementById('cantPaginas').value;

        body.append('titulo', titulo);
        body.append('cantPag',cantPag);
    } else {
        showError('Tipo de material no válido');
        return;
    }

    console.log('Request body:', body.toString());

    fetch('/biblioteca-web/agregarMaterial', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body:body.toString()
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error('Server error: ' + text); });
        }
        return response.json();
    })
    .then(data => {
        console.log('Material registrado:', data);

        document.getElementById('agregarMaterialForm').reset();
        mostrarCampos('');
        mostrarMensaje("Material agregado correctamente", "success");

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
        mostrarMensaje("Error de conexión: " + error.message, "danger");
    });
});

function showError(message) {
    document.getElementById('errorMessage').textContent = message;
    document.querySelector('.error').style.display = 'block';
}

function mostrarMensaje(texto, tipo) {
    const mensaje = document.getElementById("mensajeResultado");
    mensaje.textContent = texto;
    mensaje.className = "alert alert-" + tipo;
    mensaje.classList.remove("d-none");

    setTimeout(() => {
        mensaje.classList.add("d-none");
    }, 3000);
}