<!-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prestamos Gestionados por mi</title>
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/prestamosPorBibliotecario.css" rel="stylesheet">
</head>
<body class="with-sidebar no-page-scroll">

    <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />
    <jsp:include page="/WEB-INF/jsp/partials/sidebar.jsp" />

        <main class="page-wrapper">
        <div class="container">
        <h1 class="mb-4"> Historial de Prestamos </h1>

        <!-- Error -->
        <div class="error alert alert-danger" role="alert" style="display:none;">
            <strong>Error:</strong> <span id="errorMessage"></span>
        </div>

        <div class="table-scroll-container">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Fecha Solicitud</th>
                            <th>Fecha Devolución</th>
                            <th>Estado</th>
                            <th>Material</th>
                            <th>Lector</th>
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

    </main>
    </div>
    
    

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
    <script src="${pageContext.request.contextPath}/js/prestamosPorBibliotecario.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboardBiblio.js"></script>
</body>
</html>