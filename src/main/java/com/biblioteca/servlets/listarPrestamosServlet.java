package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/listarPrestamos")
public class listarPrestamosServlet extends HttpServlet {

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
        try {
            listarPrestamos(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            modificarPrestamo(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void listarPrestamos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PrestamoServiceClient prestamoClient = new PrestamoServiceClient();
            List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamos();

            System.out.println("Prestamos obtenidos: " + prestamos.size());

            StringBuilder json = new StringBuilder("[");
            SimpleDateFormat salidaFecha = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < prestamos.size(); i++) {
                DtPrestamo prestamo = prestamos.get(i);

                String id = String.valueOf(prestamo.getIdPrestamo());
                String fechaSolicitud = prestamo.getFechaSoli() != null
                        ? salidaFecha.format(prestamo.getFechaSoli())
                        : "";
                String fechaDevolucion = prestamo.getFechaDev() != null
                        ? salidaFecha.format(prestamo.getFechaDev())
                        : "";
                String estado = prestamo.getEstadoPres() != null ? prestamo.getEstadoPres().name() : "";
                String lector = prestamo.getLector();
                String idMaterial = String.valueOf(prestamo.getMaterial());
                String bibliotecario = prestamo.getBibliotecario();

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"id\":\"").append(escaparJson(id)).append("\",")
                        .append("\"fechaSoli\":\"").append(escaparJson(fechaSolicitud)).append("\",")
                        .append("\"fechaDev\":\"").append(escaparJson(fechaDevolucion)).append("\",")
                        .append("\"estadoP\":\"").append(escaparJson(estado)).append("\",")
                        .append("\"correoL\":\"").append(escaparJson(lector)).append("\",")
                        .append("\"idMaterial\":\"").append(escaparJson(idMaterial)).append("\",")
                        .append("\"correoB\":\"").append(escaparJson(bibliotecario)).append("\"")
                        .append("}");
            }
            json.append("]");

            System.out.println("JSON generado: " + json.toString());

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void modificarPrestamo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("idPrestamo");
            String estadoStr = request.getParameter("estadoP");
            String fechaSoliStr = request.getParameter("fechaSoli");
            String fechaDevStr = request.getParameter("fechaDev");
            String correoL = request.getParameter("correoL");
            String idMaterialStr = request.getParameter("idMaterial");
            String correoB = request.getParameter("correoB");

            if (idStr == null || estadoStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"ID y Estado son requeridos\"}");
                return;
            }

            EstadosP nuevoEstado = estadoStr != null ? EstadosP.valueOf(estadoStr) : null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date nuevaFechaSoli = (fechaSoliStr != null && !fechaSoliStr.isEmpty())
                    ? sdf.parse(fechaSoliStr)
                    : null;
            Date nuevaFechaDev = (fechaDevStr != null && !fechaDevStr.isEmpty())
                    ? sdf.parse(fechaDevStr)
                    : null;
            int nuevoMaterialId = Integer.parseInt(idMaterialStr);

            DtPrestamo prestamo = new DtPrestamo();
            prestamo.setIdPrestamo(Integer.parseInt(idStr));

            if (nuevoEstado != null) {
                prestamoClient.cambiarEstadoPrestamo(prestamo, nuevoEstado);
            }
            if (nuevaFechaSoli != null) {
                prestamoClient.cambiarFechaSolicitudPrestamo(prestamo, nuevaFechaSoli);
            }
            if (nuevaFechaDev != null) {
                prestamoClient.cambiarFechaDevolucionPrestamo(prestamo, nuevaFechaDev);
            }
            if (correoL != null && !correoL.isEmpty()) {
                prestamoClient.cambiarCorreoLectorPrestamo(prestamo, correoL);
            }
            if (correoB != null && !correoB.isEmpty()) {
                prestamoClient.cambiarCorreoBibliotecarioPrestamo(prestamo, correoB);
            }
            prestamoClient.cambiarMaterialPrestamo(prestamo, nuevoMaterialId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Préstamo modificado exitosamente\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String mensaje = e.getMessage();
            response.getWriter().write("{\"status\":\"error\", \"message\": \"" + escaparJson(mensaje) + "\"}");
        }
    }

    private String escaparJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
