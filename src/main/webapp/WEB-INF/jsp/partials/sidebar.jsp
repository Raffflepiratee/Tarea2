<%-- Fragmento reusable para la sidebar y overlay --%>
<aside class="sidebar" aria-hidden="true">
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/bibliotecario/dashboard.jsp">Dashboard</a>
        <a href="${pageContext.request.contextPath}/bibliotecario/listarLectores.jsp">Listar lectores</a>
        <a href="${pageContext.request.contextPath}/bibliotecario/agregarMaterial.jsp">Registrar material</a>
        <a href="${pageContext.request.contextPath}/bibliotecario/listarMaterialesPorRango.jsp">Donaciones por rango</a>
        <a href="#">Materiales con mas prestamos</a>
        <a href="#">Prestamos de un lector</a>
        <a href="#">Reporte zonal</a>
        <a href="#">Mi historial</a>
        <div class="sidebar-user d-none">
            <button class="btn btn-logout btn-danger"onclick="location.href='${pageContext.request.contextPath}/logout'">Salir</button>
        </div>
    </nav>
</aside>
<div class="sidebar-overlay"></div>
