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

import com.biblioteca.client.UsuarioServiceClient;
import com.biblioteca.datatypes.DtUsuario;
import com.biblioteca.datatypes.DtLector;
import com.biblioteca.datatypes.DtBibliotecario;
import com.biblioteca.datatypes.EstadosU;
import com.biblioteca.datatypes.Zonas;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet para manejar operaciones de Usuarios
 */
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioServiceClient usuarioClient;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioClient = new UsuarioServiceClient();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "list":
                    listarUsuarios(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "update") {
                case "update":
                    modificarLector(request, response);
                    break;
                default:
                    modificarLector(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        List<DtUsuario> usuarios = usuarioClient.obtenerUsuarios();
        System.out.println("Usuarios recibidos desde el cliente: " + usuarios);

        // Construir JSON manualmente
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < usuarios.size(); i++) {
            DtUsuario u = usuarios.get(i);
            System.out.println("Usuario: " + u.getNombre() + " | Correo: " + u.getCorreo());

            String tipo = "Usuario";
            String detalles = "";
            String zona = "";
            String estado = "";

            if (u instanceof DtLector) {
                DtLector l = (DtLector) u;
                tipo = "Lector";
                detalles = "Zona: " + l.getZona() + ", Dirección: " + l.getDireccion();
                zona = l.getZona() != null ? l.getZona().toString() : "";
                estado = l.getEstadoUsuario() != null ? l.getEstadoUsuario().toString() : "";
            } else if (u instanceof DtBibliotecario) {
                DtBibliotecario b = (DtBibliotecario) u;
                tipo = "Bibliotecario";
                detalles = "ID Empleado: " + b.getIdEmp();
            }

            if (i > 0)
                json.append(",");
            json.append("{")
                    .append("\"nombre\":\"").append(escaparJson(u.getNombre())).append("\",")
                    .append("\"correo\":\"").append(escaparJson(u.getCorreo())).append("\",")
                    .append("\"tipo\":\"").append(escaparJson(tipo)).append("\",")
                    .append("\"detalles\":\"").append(escaparJson(detalles)).append("\",")
                    .append("\"zona\":\"").append(escaparJson(zona)).append("\",")
                    .append("\"estado\":\"").append(escaparJson(estado)).append("\"")
                    .append("}");
        }

        json.append("]");

        System.out.println("JSON generado: " + json.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private String escaparJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private void modificarLector(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String correo = request.getParameter("correo");
        String zona = request.getParameter("zona");
        String estadoParam = request.getParameter("estado");

        // System.out.println("modificarLector called with correo='" + correo + "', zona='" + zona + "', estado='" + estadoParam + "'");

        // if (correo == null || correo.trim().isEmpty()) {
        //     response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        //     response.getWriter().write("{\"error\": \"Missing parameter: correo\"}");
        //     return;
        // }

        // if (zona == null || zona.trim().isEmpty()) {
        //     response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        //     response.getWriter().write("{\"error\": \"Missing parameter: zona\"}");
        //     return;
        // }

        // if (estadoParam == null || estadoParam.trim().isEmpty()) {
        //     response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        //     response.getWriter().write("{\"error\": \"Missing parameter: estado\"}");
        //     return;
        // }

        EstadosU estado;

        estado = EstadosU.valueOf(estadoParam);
       


        // System.out.println("About to call usuarioClient.cambiarZonaLector with correo='" + correo + "', zona='" + zona + "'");
        try {
            usuarioClient.cambiarZonaLector(correo, zona);
            System.out.println("cambiarZonaLector succeeded for '" + correo + "'");
        } catch (Exception e) {
            System.err.println("Error in cambiarZonaLector: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error changing zona: " + e.getMessage() + "\"}");
            return;
        }

        // System.out.println("About to call usuarioClient.cambiarEstadoLector with correo='" + correo + "', estado='" + estado + "'");
        try {
            usuarioClient.cambiarEstadoLector(correo, estado);
            System.out.println("cambiarEstadoLector succeeded for '" + correo + "' -> " + estado);
        } catch (Exception e) {
            System.err.println("Error in cambiarEstadoLector: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error changing estado: " + e.getMessage() + "\"}");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"success\"}");
    }
}
