<!-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiales - Biblioteca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/materiales.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h1 class="mb-4">📚 Materiales</h1>
        
        <!-- Loading -->
        <!-- <div class="loading">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="mt-2">Cargando materiales...</p>
        </div> -->
        
        <!-- Error -->
        <!-- <div class="error alert alert-danger" role="alert">
            <strong>Error:</strong> <span id="errorMessage"></span>
        </div> -->

        <!-- Tabla de Materiales -->
        <!-- <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Nombre del material</th>
                    </tr>
                </thead>
                <tbody id="materialesTableBody">
                    <tr>
                        <td colspan="5" class="text-center">Cargando materiales...</td>
                    </tr>
                </tbody>
            </table>
        </div>  -->
        <div>
            <form id="agregarMaterialForm">
                <div class="mb-3">
                    <label for="tipoMaterial" class="form-label">Tipo de material</label>
                    <select class="form-select" id="tipoMaterial" required>
                        <option value="" disabled selected hidden>-- Seleccionar tipo --</option>
                        <option value="LIBRO">Libro</option>
                        <option value="ARTICULO">Articulo Especial</option>
                    </select>
                </div>

                <div id="camposLibro" style="display: none;"><!-- Información del libro -->
                    <div class="mb-3">
                        <label for="tituloLibro" class="form-label">Título del libro</label>
                        <input type="text" class="form-control" id="tituloLibro" required>
                    </div>
                    <div class="mb-3">
                        <label for="cantPaginas" class="form-label">Cantidad de páginas</label>
                        <input type="number" class="form-control" id="cantPaginas" required>
                    </div>
                </div>
                

                <div id="camposArticulo" style="display: none;"><!-- Información del material especial -->
                    <div class="mb-3">
                        <label for="descArticulo" class="form-label">Descripción del artículo</label>
                        <input type="text" class="form-control" id="descArticulo" required>
                    </div>
                    <div class="mb-3">
                        <label for="peso" class="form-label">Peso (kg)</label>
                        <input type="number" step="0.01" class="form-control" id="peso" name="peso" required>
                    </div>
                    <div class="mb-3">
                        <label for="dimFisica" class="form-label">Dimensión Física</label>
                        <input type="text" class="form-control" id="dimFisica" name="dimFisica" required>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" id="registrarMaterial">Guardar Material</button>
            </form>
        </div>
        <br><br>
        <div id="mensajeResultado" class="error alert alert-dange" role="alert"></div>

        
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/materiales.js"></script>
</body>
</html>