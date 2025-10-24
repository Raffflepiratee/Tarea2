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
import com.biblioteca.client.UsuarioServiceClient;
import com.biblioteca.datatypes.DtUsuario;
import com.biblioteca.datatypes.DtLector;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/prestamosActivosLector")
public class prestamosActivosLectorServlet extends HttpServlet {

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
        String lector = request.getParameter("lector");
        if (lector == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing required parameter: lector\"}");
            return;
        }

        // Antes de consultar los préstamos, verificamos que el correo exista y sea un
        // lector
        try {
            UsuarioServiceClient usuarioClient = new UsuarioServiceClient();
            List<DtUsuario> usuarios = usuarioClient.obtenerUsuarios();
            DtUsuario encontrado = null;
            if (usuarios != null) {
                for (DtUsuario u : usuarios) {
                    if (u != null && u.getCorreo() != null && u.getCorreo().equalsIgnoreCase(lector.trim())) {
                        encontrado = u;
                        break;
                    }
                }
            }

            if (encontrado == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"El correo ingresado no corresponde a ningún usuario\"}");
                return;
            }

            if (!(encontrado instanceof DtLector)) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"El usuario encontrado no es un lector\"}");
                return;
            }

        } catch (Exception ex) {
            // Si hay un error consultando usuarios, seguimos pero logueamos
            System.err.println("Error verificando usuario: " + ex.getMessage());
        }

        PrestamosActivosLector(lector, request, response);

    }

    private void PrestamosActivosLector(String lector, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            // use the client initialized in init()
            PrestamoServiceClient client = new PrestamoServiceClient();
            List<DtPrestamo> prestamos = client.obtenerPrestamosActivosLector(lector);

            System.out.println("Prestamos obtenidos: " + prestamos.size());

            // Debug: log each DtPrestamo received and its getters to verify data
            for (int i = 0; i < prestamos.size(); i++) {
                DtPrestamo p = prestamos.get(i);
                System.out.println("[DEBUG] Prestamo[" + i + "] toString(): " + p);
                try {
                    System.out.println("[DEBUG] Prestamo[" + i + "] id=" + p.getIdPrestamo()
                            + " fechaSoli=" + p.getFechaSoli() + " fechaDev=" + p.getFechaDev()
                            + " estado=" + p.getEstadoPres() + " lector=" + p.getLector()
                            + " bibliotecario=" + p.getBibliotecario() + " material=" + p.getMaterial());
                } catch (Exception ex) {
                    System.out.println("[DEBUG] Error leyendo getters del prestamo[" + i + "]: " + ex);
                }
            }

            StringBuilder json = new StringBuilder("[");
            SimpleDateFormat salidaFecha = new SimpleDateFormat("dd/MM/yyyy");

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