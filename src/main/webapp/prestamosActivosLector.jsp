<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prestamos Activos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
</head>
<body>
    <div class="container">
        <h1 class="mb-4"> Lista de Prestamos Activos </h1>

        <!-- Error -->
        <div class="error alert alert-danger" role="alert" style="display:none;">
            <strong>Error:</strong> <span id="errorMessage"></span>
        </div>

        <!-- Buscador por correo de lector -->
        <form id="prestamosActivosForm" class="mb-3 row align-items-center">
            <label for="lectorEmail" class="col-auto col-form-label">Correo del lector</label>
            <div class="col-auto">
                <input type="text" class="form-control" id="lectorEmail" placeholder="correo@ejemplo.com">
            </div>
            <div class="col-auto">
                <button id="buscarPrestamosActivos" type="submit" class="btn btn-primary">Buscar</button>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Fecha Solicitud</th>
                        <th>Fecha Devolucion</th>
                        <th>Estado</th>
                        <th>Material</th>
                        <th>Bibliotecario</th>
                    </tr>
                </thead>
                <tbody id="prestamosTableBody">
                    <tr>
                        <td colspan="6" class="text-center">Ingrese un correo y presione Buscar</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/prestamosActivosLector.js"></script>
</body>
</html>