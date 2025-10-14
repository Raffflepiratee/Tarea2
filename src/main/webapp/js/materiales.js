document.getElementById('registrarMaterial').addEventListener('click', function(event) {
    event.preventDefault();
    const tipo = document.getElementById('tipoMaterial').value;

    const body =  new URLSearchParams();
    // servlet expects parameter name 'tipoMaterial'
    body.append('tipoMaterial', tipo);

    // include fechaRegistro so server won't receive null
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

    // servlet expects 'descripcion', 'peso', 'dimFisica'
    body.append('descripcion', descArticulo);
    body.append('peso', peso);
    body.append('dimFisica', dimFisica);

    } else if (tipo == 'LIBRO') {
    const titulo = document.getElementById('tituloLibro').value;
    const cantPag = document.getElementById('cantPaginas').value;
    // servlet expects 'titulo' and 'cantPag'
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
        // attempt to hide modal if it exists and has a Bootstrap instance
        const modalEl = document.getElementById('exampleModal');
        if (modalEl) {
            const modalInstance = bootstrap.Modal.getInstance(modalEl) || bootstrap.Modal.getOrCreateInstance(modalEl);
            if (modalInstance && typeof modalInstance.hide === 'function') {
                modalInstance.hide();
            }
        }
        // cargarMateriales();
    })
    .catch(error => {
        console.error('Error al agregar:', error);
    });
});

function showError(message) {
    document.getElementById('errorMessage').textContent = message;
    document.querySelector('.error').style.display = 'block';
}