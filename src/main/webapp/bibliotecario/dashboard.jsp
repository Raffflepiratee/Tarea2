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

    <main class="dashboard-wrapper">
        <div class="container">
            <h1>Bienvenido "Nombre del Bibliotecario"</h1>
            <nav>
                <ul>
                    <li><a href="listarLectores.jsp">Listar Lectores</a></li>
                </ul>
            </nav>
        </div>
    </main>

    <footer class="page-footer">
        <div class="container text-center small">© Biblioteca Comunitaria</div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/dashboardBiblio.js"></script>
</body>
</html>
