package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
    private PrestamoServiceClient prestamoClient;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        prestamoClient = new PrestamoServiceClient();
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

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < prestamosPendientes.size(); i++) {
            String idMaterial = "";
            String indice = String.valueOf(i);
            String cantPrestamosStr = "";

            DtPrestamo prestamo = prestamosPendientes.get(i);
            idMaterial = String.valueOf(prestamo.getMaterial());
            cantPrestamosStr = String.valueOf(contarPrestamosPorMaterial(prestamosPendientes, prestamo.getMaterial()));

            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                    .append("\"Indice\":").append(escaparJson(indice)).append("\",")
                    .append("\"IDMaterial\":").append(escaparJson(idMaterial)).append("\",")
                    .append("\"PrestamosPendientes\":").append(escaparJson(cantPrestamosStr)).append("\"")
                    .append("}");
        }
        json.append("]");

        System.out.println("JSON generado: " + json.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
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

    private String escaparJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
