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

    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/ordenarMaterialesPorPendientes.js"></script>
</body>
</html>