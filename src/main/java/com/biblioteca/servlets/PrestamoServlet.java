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
@WebServlet("/prestamos")
public class PrestamoServlet extends HttpServlet {
    
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
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listarPrestamos(request, response);
                    break;
                case "pendientes":
                    listarPrestamosPendientes(request, response);
                    break;
                case "porZona":
                    listarPrestamosPorZona(request, response);
                    break;
                case "porBibliotecario":
                    listarPrestamosPorBibliotecario(request, response);
                    break;
                case "activosLector":
                    listarPrestamosActivosLector(request, response);
                    break;
                default:
                    listarPrestamos(request, response);
                    break;
            }
        } catch (Exception e) {
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
                    agregarPrestamo(request, response);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Acción no válida\"}");
                    break;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    private void listarPrestamos(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamos();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(prestamos);
        response.getWriter().write(json);
    }
    
    private void listarPrestamosPendientes(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamosPendientes();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(prestamos);
        response.getWriter().write(json);
    }
    
    private void listarPrestamosPorZona(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String zonaStr = request.getParameter("zona");
        
        if (zonaStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Zona es requerida\"}");
            return;
        }
        
        Zonas zona = Zonas.valueOf(zonaStr);
        List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamosPorZona(zona);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(prestamos);
        response.getWriter().write(json);
    }
    
    private void listarPrestamosPorBibliotecario(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String idEmpStr = request.getParameter("idEmp");
        
        if (idEmpStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID del empleado es requerido\"}");
            return;
        }
        
        int idEmp = Integer.parseInt(idEmpStr);
        List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamosPorBibliotecario(idEmp);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(prestamos);
        response.getWriter().write(json);
    }
    
    private void listarPrestamosActivosLector(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String correoLector = request.getParameter("correoLector");
        
        if (correoLector == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Correo del lector es requerido\"}");
            return;
        }
        
        List<DtPrestamo> prestamos = prestamoClient.obtenerPrestamosActivosLector(correoLector);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(prestamos);
        response.getWriter().write(json);
    }
    
    private void agregarPrestamo(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ParseException {
        
        String fechaSoliStr = request.getParameter("fechaSoli");
        String fechaDevStr = request.getParameter("fechaDev");
        String estadoStr = request.getParameter("estado");
        String correoLector = request.getParameter("correoLector");
        String correoBiblio = request.getParameter("correoBiblio");
        String idMaterialStr = request.getParameter("idMaterial");
        
        if (fechaSoliStr == null || fechaDevStr == null || estadoStr == null || 
            correoLector == null || correoBiblio == null || idMaterialStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Todos los campos son requeridos\"}");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaSoli = sdf.parse(fechaSoliStr);
        Date fechaDev = sdf.parse(fechaDevStr);
        EstadosP estado = EstadosP.valueOf(estadoStr);
        int idMaterial = Integer.parseInt(idMaterialStr);
        
        prestamoClient.agregarPrestamo(fechaSoli, fechaDev, estado, correoLector, correoBiblio, idMaterial);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Préstamo agregado exitosamente\"}");
    }
}

