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

import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;
import com.biblioteca.datatypes.Zonas;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet para manejar operaciones de Préstamos
 */
@WebServlet("/listarPrestamosPorZona")
public class listarPrestamosPorZonaServlet extends HttpServlet {

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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String zonaStr = request.getParameter("zona");
            Zonas zona = Zonas.valueOf(zonaStr);
            List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamosPorZona(zona);

            StringBuilder json = new StringBuilder("[");
            SimpleDateFormat salidaFecha = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < prestamos.size(); i++) {
                DtPrestamo prestamo = prestamos.get(i);

                String idPrestamo = String.valueOf(prestamo.getIdPrestamo());
                String fechaSolicitud = prestamo.getFechaSoli() != null
                        ? salidaFecha.format(prestamo.getFechaSoli())
                        : "";
                String fechaDevolucion = prestamo.getFechaDev() != null
                        ? salidaFecha.format(prestamo.getFechaDev())
                        : "";
                String estado = prestamo.getEstadoPres() != null ? prestamo.getEstadoPres().name() : "";
                String lector = prestamo.getLector();
                String material = String.valueOf(prestamo.getMaterial());
                String bibliotecario = prestamo.getBibliotecario();

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"idPrestamo\":\"").append(escaparJson(idPrestamo)).append("\",")
                        .append("\"fechaSoli\":\"").append(escaparJson(fechaSolicitud)).append("\",")
                        .append("\"fechaDev\":\"").append(escaparJson(fechaDevolucion)).append("\",")
                        .append("\"estadoP\":\"").append(escaparJson(estado)).append("\",")
                        .append("\"lector\":\"").append(escaparJson(lector)).append("\",")
                        .append("\"material\":\"").append(escaparJson(material)).append("\",")
                        .append("\"bibliotecario\":\"").append(escaparJson(bibliotecario)).append("\"")
                        .append("}");
            }
            json.append("]");
            System.out.println("JSON generado: " + json.toString());
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + escaparJson(e.getMessage()) + "\"}");
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
