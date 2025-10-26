<!-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Material - Biblioteca</title>

    <!-- Estilos -->
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/agregarMaterial.css" rel="stylesheet">
</head>

<body class="with-sidebar">

    <!-- Header -->
    <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/jsp/partials/sidebar.jsp" />

    <!-- CONTENIDO -->
    <main class="page-wrapper">

        <div class="container">

            <div class="card form-card">
                <div class="card-body">

                    <h1 class="mb-4">Registrar Material</h1>

                    <form id="agregarMaterialForm">

                        <!-- Tipo -->
                        <div class="mb-3">
                            <label for="tipoMaterial" class="form-label">Tipo de material</label>
                            <select class="form-select" id="tipoMaterial" required>
                                <option value="" disabled selected hidden>-- Seleccionar tipo --</option>
                                <option value="LIBRO">Libro</option>
                                <option value="ARTICULO">Artículo Especial</option>
                            </select>
                        </div>

                        <!-- Campos LIBRO -->
                        <div id="camposLibro" style="display: none;">
                            <div class="mb-3">
                                <label for="tituloLibro" class="form-label">Título del libro</label>
                                <input type="text" class="form-control" id="tituloLibro">
                            </div>
                            <div class="mb-3">
                                <label for="cantPaginas" class="form-label">Cantidad de páginas</label>
                                <input type="number" min="0" class="form-control" id="cantPaginas">
                            </div>
                        </div>

                        <!-- Campos ARTÍCULO -->
                        <div id="camposArticulo" style="display: none;">
                            <div class="mb-3">
                                <label for="descArticulo" class="form-label">Descripción del artículo</label>
                                <input type="text" class="form-control" id="descArticulo">
                            </div>
                            <div class="mb-3">
                                <label for="peso" class="form-label">Peso (kg)</label>
                                <input type="number" step="0.01" class="form-control" id="peso" name="peso">
                            </div>
                            <div class="mb-3">
                                <label for="dimFisica" class="form-label">Dimensión Física</label>
                                <input type="text" class="form-control" id="dimFisica" name="dimFisica">
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="botones d-flex gap-2">
                            <button type="submit" class="btn btn-primary" id="registrarMaterial">Guardar Material</button>
                            <button type="button" class="btn btn-secondary" id="limpiarForm">Limpiar</button>
                        </div>

                    </form>

                    <!-- Mensajes -->
                    <div id="mensajes" class="mt-3">
                        <div id="mensajeResultado" class="alert d-none" role="alert"></div>
                        <div class="error alert alert-danger d-none">
                            <span id="errorMessage"></span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </main>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
    <script src="${pageContext.request.contextPath}/js/agregarMaterial.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboardBiblio.js"></script>

</body>
</html>
