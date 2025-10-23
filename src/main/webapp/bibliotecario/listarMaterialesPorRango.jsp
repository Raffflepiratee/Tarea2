<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiales</title>
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/listarMaterialesPorRango.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- <link href="../css/listarMaterialesPorRango.css" rel="stylesheet"> -->
</head>
<body class="with-sidebar">
    
     <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />
     <jsp:include page="/WEB-INF/jsp/partials/sidebar.jsp" />

    <form>  
        <label for="fechaInicio">Rango de inicio:</label>
        <input type="date" id="fechaInicio" name="fechaInicio" required>
        
        <label for="fechaFin">Rango de fin:</label>
        <input type="date" id="fechaFin" name="fechaFin" required>
        
        <button class="btn btn-primary" id="Boton" type="submit">Listar Materiales</button>    
    </form>

    <div id="results">
        <table border="1">
            <thead>
                <tr>
                    <th>ID Material</th>
                    <th>Fecha de Registro</th>
                    <th>Tipo</th>
                    <th>Detalles</th>
                </tr>
            </thead>
            <tbody id="materialesBody">
            </tbody>
        </table>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/listarMaterialesPorRango.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboardLector.js"></script>
</body>
</html>