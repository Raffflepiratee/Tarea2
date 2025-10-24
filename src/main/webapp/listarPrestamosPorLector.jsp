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
        <h1 class="mb-4"> Lista de Prestamos de un Lector</h1>
        
        <div class="mb-3">
            <label for="correoLector" class="form-label">Correo del Lector:</label>
            <input type="email" class="form-control" id="correoLector" placeholder="Ingrese el correo del lector">
            <button id="btnListarPrestamos" class="btn btn-primary mt-2">Listar Prestamos</button>
        </div>

        <!-- Error -->
        <div class="error alert alert-danger" role="alert" style="display: none;">
            <strong>Error:</strong> <span id="errorMessage"></span>
        </div>

        <div class="table-responsive">
            <div class="mb-3 d-flex align-items-center">
                <label for="filtroEstado" class="me-2 mb-0">Filtrar por estado:</label>
                <select id="filtroEstado" class="form-select w-auto">
                    <option value="ALL">Todos</option>
                    <option value="PENDIENTE">Pendientes</option>
                    <option value="EN_CURSO">En curso</option>
                    <option value="DEVUELTO">Devueltos</option>
                </select>
            </div>
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
                    </tr>
                </thead>
                <tbody id="prestamosTableBody">
                    <tr>
                        <td colspan="7" class="text-center">Ingrese un correo y presione "Listar Prestamos"</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/listarPrestamosPorLector.js"></script>
</body>
</html>