<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h1 class="mb-4"> Lista de Prestamos</h1>
        
        <!-- Loading -->
        <div class="loading">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="mt-2">Cargando prestamos...</p>
        </div>

        <!-- Error -->
        <div class="error alert alert-danger" role="alert">
            <strong>Error:</strong> <span id="errorMessage"></span>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Fecha Solicitud</th>
                        <th>Fecha Devolucion</th>
                        <th>Estado</th>
                        <th>Correo Lector</th>
                        <th>ID Material</th>
                        <th>Correo Bibliotecario</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody id="prestamosTableBody">
                    <tr>
                        <td colspan="8" class="text-center">Cargando prestamos...</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="exampleModalLabel">Modificar Datos</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="mb-3">
                                <label for="fechaSolicitud" class="col-form-label">Fecha solicitud:</label>
                                <input type="date" class="form-control" id="fechaSoli">
                            </div>
                            <div class="mb-3">
                                <label for="fechaDevolucion" class="col-form-label">Fecha devolucion:</label>
                                <input type="date" class="form-control" id="fechaDev">
                            </div>
                            <div class="mb-3">
                                <label for="estadoP" class="col-form-label">Estado:</label>
                                <select class="form-select" id="estadoP">
                                    <option value="" disabled selected hidden>-- Seleccionar --</option>
                                    <option value="PENDIENTE">Pendiente</option>
                                    <option value="EN_CURSO">En curso</option>
                                    <option value="DEVUELTO">Devuelto</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="correoL" class="col-form-label">Correo Lector:</label>
                                <input type="email" class="form-control" id="correoL">
                            </div>
                            <div class="mb-3">
                                <label for="idMaterial" class="col-form-label">ID Material:</label>
                                <input type="text" class="form-control" id="idMaterial">
                            </div>
                            <div class="mb-3">
                                <label for="correoB" class="col-form-label">Correo Bibliotecario:</label>
                                <input type="email" class="form-control" id="correoB">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" id="modificarPrestamoForm">Guardar cambios</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/listarPrestamos.js"></script>
</body>
</html>