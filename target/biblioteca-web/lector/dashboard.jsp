<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Lector</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
<!--     <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet"> -->
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
    
    <!-- Optional: Inter font to match login if not globally available -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <header class="topbar">
        <div class="inner">
            <div class="brand">Biblioteca Comunitaria UY</div>
            <div class="profile"></div>
        </div>
    </header>

    <main class="dashboard-wrapper">
        <div class="container">
            <h1>Bienvenido "Nombre del Lector"</h1>
            <nav>
                <ul>
                    <li><a href="buscarLibro.jsp">Buscar Libro *CAMBIAR*</a></li>
                    <li><a href="misPrestamos.jsp">Mis Prestamos *CAMBIAR*</a></li>
                </ul>
            </nav>
        </div>
    </main>

    <footer class="page-footer">
        <div class="container text-center small">© Biblioteca Comunitaria</div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/dashboardLector.js"></script>
</body>
</html>
