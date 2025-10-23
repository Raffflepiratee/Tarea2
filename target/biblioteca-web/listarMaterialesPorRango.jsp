<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiales</title>
</head>
<body>
    <form>  
        <label for="fechaInicio">Rango de inicio:</label>
        <input type="date" id="fechaInicio" name="fechaInicio" required>
        
        <label for="fechaFin">Rango de fin:</label>
        <input type="date" id="fechaFin" name="fechaFin" required>
        
        <button id="Boton" type="submit">Listar Materiales</button>
        
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
</body>
</html>