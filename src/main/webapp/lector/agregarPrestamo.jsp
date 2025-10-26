<!-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prestamos - Biblioteca</title>
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/agregarPrestamo.css" rel="stylesheet">
</head>
<body class="with-sidebar">
    <jsp:include page="/WEB-INF/jsp/partials/header.jsp"/>
    <jsp:include page="/WEB-INF/jsp/partials/sidebarLector.jsp"/>

    <div class="container">
        <h1 class="mb-4">Prestamos</h1>
        <div>   
            <form id="agregarPrestamoForm">
                <div class="mb-3">
                    <div class="mb-3">
                        <label for = "idMaterial" class ="form-label">Id Material</label>
                        <input type="text" class="form-control" id="idMaterial" name="idMaterial" required>
                    </div>

                    <div class="mb-3">
                        <label for = "fechaInicial" class ="form-label">Fecha Inicial</label>
                        <input type="date" class="form-control" id="fechaInicial" name="fechaInicial" required>
                    </div>

                    <div class="mb-3">
                        <label for = "fechaFinal" class ="form-label">Fecha Final</label>
                        <input type="date" class="form-control" id="fechaFinal" name="fechaFinal" required>
                    </div>

                    <div class="mb-3">
                        <button type="reset" class="btn btn-secondary">Cancelar</button>
                        <button type="submit" class="btn btn-primary" id="registrarPrestamo">Guardar Prestamo</button>
                    </div>
                    <!-- Message area for JS feedback -->
                    <div id="mensajeResultado" class="alert d-none mt-3" role="alert"></div>
                    <div id="errorMessage" class="text-danger mt-2" style="display:none;"></div>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboardLector.js"></script>
    <script src="${pageContext.request.contextPath}/js/agregarPrestamo.js"></script>
</body>
</html>