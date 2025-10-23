<%-- Fragmento reusable para la sidebar y overlay --%>
<aside class="sidebar" aria-hidden="true">
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/bibliotecario/dashboard.jsp">Dashboard</a>
        <a href="${pageContext.request.contextPath}/bibliotecario/listarLectores.jsp">Listar lectores</a>
        <a href="${pageContext.request.contextPath}/bibliotecario/agregarMaterial.jsp">Registrar material</a>
        <a href="#">Donaciones por rango</a>
        <a href="#">Materiales con mas prestamos</a>
        <a href="#">Prestamos de un lector</a>
        <a href="#">Reporte zonal</a>
        <a href="#">Mi historial</a>
    </nav>
</aside>
<div class="sidebar-overlay"></div>
