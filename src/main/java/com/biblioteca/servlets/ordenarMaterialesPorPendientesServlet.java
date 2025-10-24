package com.biblioteca.servlets;

import java.io.IOException;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.datatypes.DtPrestamo;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/ordenarMaterialesPorPendientes")
public class ordenarMaterialesPorPendientesServlet extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /* try { */
        ordenarMaterialesPorPendientes(request, response);
        /*
         * } catch (Exception e) {
         * response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
         * response.getWriter().write("Error al procesar la solicitud: " +
         * e.getMessage());
         * }
         */
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void ordenarMaterialesPorPendientes(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrestamoServiceClient prestamoClient = new PrestamoServiceClient();
        List<DtPrestamo> prestamosPendientes = prestamoClient.obtenerPrestamosPendientes();

        // Si se solicitó materialId, devolver los prestamos pendientes filtrados por
        // ese material
        String materialParam = request.getParameter("materialId");
        if (materialParam != null && !materialParam.trim().isEmpty()) {
            int materialId = -1;
            try {
                materialId = Integer.parseInt(materialParam);
            } catch (NumberFormatException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(
                        java.util.Collections.singletonMap("error", "Parámetro materialId inválido")));
                return;
            }

            List<DtPrestamo> filtrados = new java.util.ArrayList<>();
            for (DtPrestamo p : prestamosPendientes) {
                if (p != null && p.getMaterial() == materialId) {
                    filtrados.add(p);
                }
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(filtrados));
            return;
        }

        // Mantener orden de aparición de materiales
        Set<Integer> idsMateriales = new LinkedHashSet<>();
        for (DtPrestamo p : prestamosPendientes) {
            if (p != null)
                idsMateriales.add(p.getMaterial());
        }

        // Construir salida con Indice, IDMaterial y PrestamosPendientes (numéricos)
        List<Map<String, Object>> salida = new java.util.ArrayList<>();
        int indice = 1;
        for (Integer idMat : idsMateriales) {
            int idValue = idMat == null ? 0 : idMat.intValue();
            int cantPrestamos = contarPrestamosPorMaterial(prestamosPendientes, idValue);

            Map<String, Object> row = new HashMap<>();
            row.put("Indice", indice);
            row.put("IDMaterial", idValue);
            row.put("PrestamosPendientes", cantPrestamos);
            salida.add(row);
            indice++;
        }

        String outJson = objectMapper.writeValueAsString(salida);
        System.out.println("JSON generado: " + outJson);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(outJson);
    }

    public int contarPrestamosPorMaterial(List<DtPrestamo> prestamos, int idMaterial) {
        int contador = 0;
        for (DtPrestamo prestamo : prestamos) {
            if (prestamo.getMaterial() == idMaterial) {
                contador++;
            }
        }
        return contador;
    }

}
