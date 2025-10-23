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
                <small class="role">lector</small>
                <span class="name">Nombre Apellido</span>
                <div>
                    <button class="logoutBtn" id="logoutBtn">Salir</button>
                </div>
            </div>
        </div>
    </header>

    <main class="main">
        <div class="grid">
            <a class="card" href="#">
                <div class="title">Buscar Libros</div>
            </a>
            <a class="card" href="#">
                <div class="title">Mis Préstamos</div>
            </a>
           <!--  <a class="card" href="#">
                <div class="title">Donaciones por rango</div>
            </a> -->
            <!-- <a class="card" href="#">
                <div class="title">Materiales con más préstamos</div>
            </a>
            <a class="card" href="#">
                <div class="title">Préstamos de un lector</div>
            </a>
            <a class="card" href="#">
                <div class="title">Reporte zonal</div>
            </a>
            <a class="card" href="#">
                <div class="title">Mi historial de préstamos</div>
            </a> -->
        </div>
    </main>

    <footer class="page-footer">
        <div class="container text-center small">© Biblioteca Comunitaria</div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/dashboardLector.js"></script>
</body>
</html>
