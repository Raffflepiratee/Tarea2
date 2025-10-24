package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;

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
                response.getWriter().write("{\"error\": \"Invalid integer for material: '" + materialStr + "'\"}");
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