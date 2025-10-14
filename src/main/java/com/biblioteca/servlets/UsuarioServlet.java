package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// import java.util.ArrayList;
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
// import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet para manejar operaciones de Usuarios
 */
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    
    private UsuarioServiceClient usuarioClient;
    // private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        usuarioClient = new UsuarioServiceClient();
        // objectMapper = new ObjectMapper();
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
            switch (action != null ? action : "add") {
                case "add":
                    agregarUsuario(request, response);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Acción no válida\"}");
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

            if (u instanceof DtLector) {
                DtLector l = (DtLector) u;
                tipo = "Lector";
                detalles = "Zona: " + l.getZona() + ", Dirección: " + l.getDireccion();
            } else if (u instanceof DtBibliotecario) {
                DtBibliotecario b = (DtBibliotecario) u;
                tipo = "Bibliotecario";
                detalles = "ID Empleado: " + b.getIdEmp();
            }

            if (i > 0) json.append(",");
            json.append("{")
                .append("\"nombre\":\"").append(escaparJson(u.getNombre())).append("\",")
                .append("\"correo\":\"").append(escaparJson(u.getCorreo())).append("\",")
                .append("\"tipo\":\"").append(escaparJson(tipo)).append("\",")
                .append("\"detalles\":\"").append(escaparJson(detalles)).append("\"")
                .append("}");
        }

        json.append("]");
        
        System.out.println("JSON generado: " + json.toString());
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
}

    private String escaparJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    
    private void agregarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ParseException {
        
        String tipo = request.getParameter("tipo");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        
        if (tipo == null || nombre == null || correo == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Todos los campos son requeridos\"}");
            return;
        }
        
        DtUsuario usuario = null;
        
        if ("lector".equals(tipo)) {
            String fechaIngresoStr = request.getParameter("fechaIngreso");
            String estadoStr = request.getParameter("estado");
            String zonaStr = request.getParameter("zona");
            String direccion = request.getParameter("direccion");
            
            if (fechaIngresoStr == null || estadoStr == null || zonaStr == null || direccion == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Todos los campos del lector son requeridos\"}");
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaIngreso = sdf.parse(fechaIngresoStr);
            EstadosU estado = EstadosU.valueOf(estadoStr);
            Zonas zona = Zonas.valueOf(zonaStr);
            
            usuario = new DtLector(nombre, correo, password, fechaIngreso, estado, zona, direccion);
            
        } else if ("bibliotecario".equals(tipo)) {
            String idEmpStr = request.getParameter("idEmp");
            
            if (idEmpStr != null && !idEmpStr.isEmpty()) {
                int idEmp = Integer.parseInt(idEmpStr);
                usuario = new DtBibliotecario(nombre, correo, password, idEmp);
            } else {
                usuario = new DtBibliotecario(nombre, correo, password);
            }
            
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Tipo de usuario no válido\"}");
            return;
        }
        
        usuarioClient.agregarUsuario(usuario);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Usuario agregado exitosamente\"}");
    }
}
