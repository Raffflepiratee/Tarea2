<!-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Lectores - Biblioteca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/listarLectores.css" rel="stylesheet">
</head>

<body class="with-sidebar no-page-scroll">
    <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />
    <jsp:include page="/WEB-INF/jsp/partials/sidebar.jsp" />

    <main class="page-wrapper">
        <div class="container">
            <h1 class="mb-4">Lista de Lectores</h1>
            <input type="text" id="searchInput" class="form-control mb-3" placeholder="Buscar por nombre o correo">

            <div class="loading">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Cargando...</span>
                </div>
                <p class="mt-2">Cargando lectores...</p>
            </div>

            <div class="error alert alert-danger" role="alert">
                <strong>Error:</strong> <span id="errorMessage"></span>
            </div>

            <div class="table-scroll-container">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>Nombre</th>
                                <th>Correo</th>
                                <th>Tipo</th>
                                <th>Detalles</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody id="usuariosTableBody">
                            <tr>
                                <td colspan="5" class="text-center">Cargando lectores...</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">Modificar Datos</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form>
                                <div class="mb-3">
                                    <label for="nombre" class="col-form-label">Zona:</label>
                                    <select class="form-control" id="zona">
                                        <option value="BIBLIOTECA_CENTRAL">BIBLIOTECA_CENTRAL</option>
                                        <option value="SUCURSAL_ESTE">SUCURSAL_ESTE</option>
                                        <option value="SUCURSAL_OESTE">SUCURSAL_OESTE</option>
                                        <option value="BIBLIOTECA_INFANTIL">BIBLIOTECA_INFANTIL</option>
                                        <option value="ARCHIVO_GENERAL">ARCHIVO_GENERAL</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="estadoU" class="col-form-label">Estado:</label>
                                    <select class="form-select" id="estadoU">
                                        <option value="" disabled selected hidden>-- Seleccionar --</option>
                                        <option value="ACTIVO">Activo</option>
                                        <option value="SUSPENDIDO">Suspendido</option>
                                    </select>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" id="modificarUsuarioForm">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
        <script src="${pageContext.request.contextPath}/js/listarLectores.js"></script>
        <script src="${pageContext.request.contextPath}/js/dashboardBiblio.js"></script>
</body>

</html>
