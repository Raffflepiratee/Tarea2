package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.biblioteca.client.MaterialServiceClient;
import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;
import com.biblioteca.datatypes.DtMaterial;

@WebServlet("/agregarPrestamo")
public class agregarPrestamoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        try {
            // parámetros esperados: lector, bibliotecario, material, fechaSoli, fechaDev,
            // estado (opcional)
            String lector = request.getParameter("lector");
            String bibliotecario = request.getParameter("bibliotecario");
            String materialStr = request.getParameter("material");
            String fechaSoliStr = request.getParameter("fechaSoli");
            String fechaDevStr = request.getParameter("fechaDev");
            String estadoStr = request.getParameter("estado");

            if (lector == null || bibliotecario == null || materialStr == null || fechaSoliStr == null
                    || fechaDevStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        "{\"error\": \"Missing required parameters (lector,bibliotecario,material,fechaSoli,fechaDev)\"}");
                return;
            }

            int materialId;
            try {
                materialId = Integer.parseInt(materialStr);
            } catch (NumberFormatException nfe) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Entero no valido para el material: '" + materialStr + "'\"}");
                return;
            }

            MaterialServiceClient matClient = new MaterialServiceClient();
            List<DtMaterial> mats = null;
            try {
                mats = matClient.obtenerMateriales();
                // imprimamos los materiales obtenidos
                for (DtMaterial m : mats) {
                    System.out.println("Material obtenido: " + m);
                }
            } catch (Exception ex) {
                System.err.println("Warning: no se pudo validar material: " + ex.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(
                        "{\"error\": \"No se pudo validar el material: servicio de materiales no disponible\"}");
                return;
            }

            boolean exists = false;
            StringBuilder availableIds = new StringBuilder();
            if (mats != null) {
                for (DtMaterial m : mats) {
                    if (m == null)
                        continue;
                    int mid = m.getIdMaterial();
                    if (mid == materialId) {
                        exists = true;
                        break;
                    }
                    if (availableIds.length() > 0)
                        availableIds.append(",");
                    availableIds.append(mid);
                }
            }

            if (!exists) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        "{\"error\": \"El id de material '" + materialId
                                + "' no corresponde a ningún material\", \"availableIds\": \""
                                + (availableIds.length() > 0 ? availableIds.toString() : "(none)") + "\"}");
                return;
            }

            Date fechaSoli;
            Date fechaDev;
            try {
                fechaSoli = new SimpleDateFormat("yyyy-MM-dd").parse(fechaSoliStr);
            } catch (ParseException pe) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter()
                        .write("{\"error\": \"Invalid date format for fechaSoli: '" + fechaSoliStr + "'\"}");
                return;
            }
            try {
                fechaDev = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDevStr);
            } catch (ParseException pe) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid date format for fechaDev: '" + fechaDevStr + "'\"}");
                return;
            }

            // validación de que la fecha de devolución no sea anterior a la de solicitud
            if (fechaDev.before(fechaSoli)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter()
                        .write("{\"error\": \"La fecha de devolución no puede ser anterior a la fecha de solicitud\"}");
                return;
            }

            EstadosP estado = EstadosP.PENDIENTE;
            if (estadoStr != null && !estadoStr.trim().isEmpty()) {
                try {
                    estado = EstadosP.valueOf(estadoStr);
                } catch (IllegalArgumentException iae) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid estado value: '" + estadoStr + "'\"}");
                    return;
                }
            }

            DtPrestamo nuevo = new DtPrestamo(fechaSoli, estado, fechaDev, lector, bibliotecario, materialId);
            agregarPrestamo(nuevo);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\": \"Success\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void agregarPrestamo(DtPrestamo prestamo) {
        System.out.println("Agregando prestamo: " + prestamo + "\n");
        PrestamoServiceClient client = new PrestamoServiceClient();
        client.agregarPrestamo(prestamo);
    }

}