package com.biblioteca.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.biblioteca.client.MaterialServiceClient;
import com.biblioteca.client.PrestamoServiceClient;
import com.biblioteca.client.UsuarioServiceClient;
import com.biblioteca.datatypes.DtBibliotecario;
import com.biblioteca.datatypes.DtLector;
import com.biblioteca.datatypes.DtMaterial;
import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.DtUsuario;
import com.biblioteca.datatypes.EstadosP;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            sdf.setLenient(false);
            sdf.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));
            Date nuevaFechaSoli = (fechaSoliStr != null && !fechaSoliStr.isEmpty())
                    ? sdf.parse(fechaSoliStr)
                    : null;
            Date nuevaFechaDev = (fechaDevStr != null && !fechaDevStr.isEmpty())
                    ? sdf.parse(fechaDevStr)
                    : null;

            System.out.println("Fecha solicitud parseada: " + nuevaFechaSoli);
            System.out.println("Fecha devolución parseada: " + nuevaFechaDev);

            int nuevoMaterialId = Integer.parseInt(idMaterialStr);

            DtPrestamo prestamo = new DtPrestamo();
            prestamo.setIdPrestamo(Integer.parseInt(idStr));

            System.out.println("Modificando préstamo ID: " + nuevoMaterialId);

            if (nuevoMaterialId <= 0 || !validarMaterial(nuevoMaterialId)) {
                System.out.println("ID del material no válido: " + nuevoMaterialId);
                /*
                 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                 * response.setContentType("application/json");
                 * response.setCharacterEncoding("UTF-8");
                 * response.getWriter().write(
                 * "{\"message\": \"El ID del material no existe en el sistema\"}");
                 * return;
                 */
            } else if (nuevoEstado == EstadosP.EN_CURSO && !validarEstadoEnCurso(nuevoMaterialId)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(
                        "{\"message\": \"Ya existe un préstamo en el estado en CURSO\"}");
                return;
            }
            prestamoClient.cambiarEstadoPrestamo(prestamo, nuevoEstado);

            if (nuevaFechaSoli != null && nuevaFechaDev != null && nuevaFechaSoli.after(nuevaFechaDev)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(
                        "{\"message\": \"La fecha de solicitud no puede ser posterior a la fecha de devolución\"}");
                return;
            }
            prestamoClient.cambiarFechaSolicitudPrestamo(prestamo, nuevaFechaSoli);
            prestamoClient.cambiarFechaDevolucionPrestamo(prestamo, nuevaFechaDev);
            if (correoL != null && !correoL.isEmpty()) {
                if (!validarCorreoLector(correoL)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                            "{\"message\": \"El correo del lector no existe en el sistema\"}");
                    return;
                }
                prestamoClient.cambiarCorreoLectorPrestamo(prestamo, correoL);
            }
            if (correoB != null && !correoB.isEmpty()) {
                if (!validarCorreoBibliotecario(correoB)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                            "{\"message\": \"El correo del bibliotecario no existe en el sistema\"}");
                    return;
                }
                prestamoClient.cambiarCorreoBibliotecarioPrestamo(prestamo, correoB);
            }
            prestamoClient.cambiarMaterialPrestamo(prestamo, nuevoMaterialId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Préstamo modificado exitosamente\"}");
        } catch (

        Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String mensaje = e.getMessage();
            response.getWriter().write("{\"message\": \"" + escaparJson(mensaje) + "\"}");
        }
    }

    private boolean validarEstadoEnCurso(int idMaterial) {
        return !prestamoClient.existePrestamoActivo(idMaterial);
    }

    private boolean validarCorreoLector(String correoL) {
        UsuarioServiceClient usuarioClient = new UsuarioServiceClient();
        List<DtUsuario> usuarios = usuarioClient.obtenerUsuarios();
        for (DtUsuario u : usuarios) {
            if (u != null
                    && u.getCorreo() != null
                    && u.getCorreo().equalsIgnoreCase(correoL.trim())
                    && u instanceof DtLector) {
                return true;
            }
        }
        return false;
    }

    private boolean validarCorreoBibliotecario(String correoB) {
        UsuarioServiceClient usuarioClient = new UsuarioServiceClient();
        List<DtUsuario> usuarios = usuarioClient.obtenerUsuarios();
        for (DtUsuario u : usuarios) {
            if (u != null
                    && u.getCorreo() != null
                    && u.getCorreo().equalsIgnoreCase(correoB.trim())
                    && u instanceof DtBibliotecario) {
                return true;
            }
        }
        return false;
    }

    private boolean validarMaterial(int idMaterial) {
        System.out.println("Validando material con ID: " + idMaterial);
        MaterialServiceClient materialClient = new MaterialServiceClient();
        List<DtMaterial> materiales = materialClient.obtenerMateriales();
        for (DtMaterial m : materiales) {
            if (m != null && m.getIdMaterial() > 0) {
                System.out.println("→ Material recibido: " + m);
            }
            if (m != null && m.getIdMaterial() == idMaterial) {
                return true;
            }
        }
        return false;
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
