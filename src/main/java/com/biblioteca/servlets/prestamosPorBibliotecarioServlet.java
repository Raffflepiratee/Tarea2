package com.biblioteca.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.client.UsuarioServiceClient;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.DtUsuario;
import com.biblioteca.datatypes.DtBibliotecario;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/prestamosPorBibliotecario")
public class prestamosPorBibliotecarioServlet extends HttpServlet {

    // prestamoClient is intentionally not stored here; we create a new client per
    // request when needed
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Esto se va cuando este el login, agarramos del navegador el usuario logueado
        // quiero el idEmpleado del bibliotecario logueado

        String bibliotecario = request.getParameter("bibliotecario");

        Integer idEmp = obtenerIdBibliotecario(bibliotecario);
        if (idEmp == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"Bibliotecario no encontrado\"}");
            return;
        }

        // llamamos a la versión que acepta idEmp
        PrestamosPorBibliotecario(idEmp, request, response);
    }

    private void PrestamosPorBibliotecario(int idEmp, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            PrestamoServiceClient client = new PrestamoServiceClient();
            List<DtPrestamo> prestamos = client.obtenerPrestamosPorBibliotecario(idEmp);

            System.out.println("Prestamos obtenidos: " + (prestamos == null ? 0 : prestamos.size()));

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
                String lector = prestamo.getLector();
                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"id\":\"").append(escaparJson(id)).append("\",")
                        .append("\"fechaSoli\":\"").append(escaparJson(fechaSolicitud)).append("\",")
                        .append("\"fechaDev\":\"").append(escaparJson(fechaDevolucion)).append("\",")
                        .append("\"estadoP\":\"").append(escaparJson(estado)).append("\",")
                        .append("\"idMaterial\":\"").append(escaparJson(idMaterial)).append("\",")
                        .append("\"correoL\":\"").append(escaparJson(lector)).append("\"")
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

    private Integer obtenerIdBibliotecario(String biblio) {
        if (biblio == null || biblio.trim().isEmpty()) {
            return null;
        }
        try {
            UsuarioServiceClient usuarioClient = new UsuarioServiceClient();
            List<DtUsuario> usuarios = usuarioClient.obtenerUsuarios();
            if (usuarios != null) {
                String correoBusq = biblio.trim();
                for (DtUsuario usuario : usuarios) {
                    if (usuario instanceof DtBibliotecario) {
                        DtBibliotecario bibliotecario = (DtBibliotecario) usuario;
                        if (bibliotecario.getCorreo() != null
                                && bibliotecario.getCorreo().equalsIgnoreCase(correoBusq)) {
                            return bibliotecario.getIdEmp();
                        }
                    }
                }
                // not found
                return null;
            } else {
                System.out.println("No se pudieron obtener usuarios del servicio.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo usuarios: " + e.getMessage());
            return null;
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