<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Bibliotecario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
</head>
<body>
    
    <header class="topbar">
        <div class="inner">
            <div class="brand">Biblioteca Comunitaria UY</div>
            <div class="profile">
                <p id="nombre_usuario"></p>
                <div>
                    <button class="logoutBtn btn btn-danger" id="logoutBtn">Salir</button>
                </div>
            </div>
        </div>
    </header>

    <main class="main">
        <div class="grid">
            <a class="card" href="${pageContext.request.contextPath}/lector/listarMateriales.jsp">
                <div class="title">Libros y Artículos</div>
            </a>

            <a class="card" href="${pageContext.request.contextPath}/lector/listarPrestamosPorLector.jsp">
                <div class="title">Mis Préstamos</div>
            </a>

            <a class="card" href="${pageContext.request.contextPath}/lector/agregarPrestamo.jsp">
                <div class="title">Nuevo Préstamo</div>
            </a>
        </div>
    </main>

    <footer class="page-footer">
        <div class="container text-center small">© Biblioteca Comunitaria</div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/dashboardLector.js"></script>
</body>
</html>
