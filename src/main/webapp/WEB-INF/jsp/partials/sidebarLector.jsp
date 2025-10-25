<aside class="sidebar" aria-hidden="true">
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/lector/dashboard.jsp">Dashboard</a>

        <a href="${pageContext.request.contextPath}/lector/listarMateriales.jsp">Libros y Artículos</a>

        <a href="${pageContext.request.contextPath}/lector/listarPrestamosPorLector.jsp">Mis préstamos</a>

        <a href="${pageContext.request.contextPath}/lector/agregarPrestamo.jsp">Nuevo Préstamo</a>

        <div class="sidebar-user d-none">
            <button class="btn btn-logout btn-danger"onclick="location.href='${pageContext.request.contextPath}/logout'">Salir</button>
        </div>
    </nav>
</aside>
<div class="sidebar-overlay"></div>
