<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Bibliotecario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
</head>
<body>
    <header class="site-header">
        <div class="container header-content">
            <div class="brand">Biblioteca Comunitaria UY</div>
        </div>
    </header>

    <!-- Navbar con botones de acción -->
    <nav class="container mt-3 d-flex justify-content-between align-items-center" aria-label="Main navigation">
        <div>
            <button type="button" class="btn btn-outline-light" onclick="history.back()">Volver</button>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/bibliotecario/listarLectores.jsp" class="btn btn-light">Listar Lectores</a>
        </div>
    </nav>

    <main class="dashboard-wrapper">
        <div class="container mt-4">
            <h1>Bienvenido "Nombre del Bibliotecario"</h1>
        </div>
    </main>

    <footer class="page-footer">
        <div class="container text-center small">© Biblioteca Comunitaria</div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/dashboardBiblio.js"></script>
</body>
</html>
