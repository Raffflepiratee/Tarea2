<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Lista de Materiales - Biblioteca</title>
</head>
<body>
    <div class="container">
        <h1 class="mb-4">Lista de Materiales Con Prestamos Pendientes</h1>
        
        <div class="loading">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="mt-2">Cargando materiales...</p>
        </div>

        <div class="error alert alert-danger" role="alert">
            <strong>Error:</strong> <span id="errorMessage"></span>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Indice</th>
                        <th>Id Material</th>
                        <th>Prestamos Pendientes</th>
                        <th>Detalles</th>
                    </tr>
                </thead>
                <tbody id="materialesTableBody">
                    <tr>
                        <td colspan="4" class="text-center">Cargando materiales...</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

        <!-- Modal para mostrar prestamos pendientes por material -->
        <div class="modal fade" id="prestamosModal" tabindex="-1" aria-labelledby="prestamosModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="prestamosModalLabel">Préstamos pendientes</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Fecha Solicitud</th>
                                        <th>Fecha Devolucion</th>
                                        <th>Estado</th>
                                        <th>Lector</th>
                                        <th>Bibliotecario</th>
                                    </tr>
                                </thead>
                                <tbody id="prestamosModalBody">
                                    <tr><td colspan="6" class="text-center">Sin datos</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/ordenarMaterialesPorPendientes.js"></script>
</body>
</html>