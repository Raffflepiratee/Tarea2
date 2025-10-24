package com.biblioteca.servlets;

import java.io.IOException;
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

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Leer parámetros desde form-data o JSON body
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if ((email == null || password == null) && request.getContentType() != null
                && request.getContentType().toLowerCase().contains("application/json")) {
            // Lectura sencilla del body JSON (sin dependencias externas)
            StringBuilder sb = new StringBuilder();
            String line;
            try (java.io.BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String body = sb.toString();
            if (!body.isEmpty()) {
                try {
                    java.util.regex.Matcher m = java.util.regex.Pattern
                            .compile("\"(email|username|password)\"\\s*:\\s*\"(.*?)\"")
                            .matcher(body);
                    while (m.find()) {
                        String k = m.group(1);
                        String v = m.group(2);
                        if ("email".equals(k) || "username".equals(k)) {
                            if (email == null)
                                email = v;
                        } else if ("password".equals(k)) {
                            if (password == null)
                                password = v;
                        }
                    }
                } catch (Exception ex) {
                    // ignorar parseo y seguir; validación posterior fallará si faltan campos
                }
            }
        }

        if (email == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"error\": \"Faltan parámetros 'email' o 'password'\"}");
            return;
        }

        try {
            UsuarioServiceClient usuarioClient = new UsuarioServiceClient();
            List<DtUsuario> usuarios = usuarioClient.obtenerUsuarios();
            if (usuarios == null) {
                usuarios = new java.util.ArrayList<>();
            }

            DtUsuario match = null;
            for (DtUsuario usuario : usuarios) {
                if (usuario.getCorreo() != null && usuario.getCorreo().equalsIgnoreCase(email)
                        && usuario.getPassword() != null && usuario.getPassword().equals(password)) {
                    match = usuario;
                    break;
                }
            }

            if (match == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"error\": \"Credenciales inválidas\"}");
                return;
            }

            String role = "usuario";
            String correoUsuario = match.getCorreo();
            String nombre = match.getNombre();
            String redirect = null;
            if (match instanceof DtLector) {
                role = "lector";
                redirect = request.getContextPath() + "/lector/dashboard.jsp";
            } else if (match instanceof DtBibliotecario) {
                role = "bibliotecario";
                redirect = request.getContextPath() + "/bibliotecario/dashboard.jsp";
            }

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            if (redirect != null) {
                response.getWriter().write("{\"role\": \"" + role + "\", \"nombre\": \"" + nombre + "\", \"correo\": \"" + correoUsuario + "\", \"redirect\": \"" + redirect + "\"}");
            } else {
                response.getWriter().write("{\"role\": \"" + role + "\", \"nombre\": \"" + nombre + "\", \"correo\": \"" + correoUsuario + "\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"error\": \"Error interno al validar credenciales\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Para autenticación usamos POST (no enviar credenciales por GET)
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported for login.");
    }
}
