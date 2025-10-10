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

import com.biblioteca.client.MaterialServiceClient;
import com.biblioteca.datatypes.DtMaterial;
import com.biblioteca.datatypes.DtLibro;
import com.biblioteca.datatypes.DtArticuloEspecial;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet para manejar operaciones de Materiales
 */
@WebServlet("/materiales")
public class MaterialServlet extends HttpServlet {
    
    private MaterialServiceClient materialClient;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        materialClient = new MaterialServiceClient();
        objectMapper = new ObjectMapper();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listarMateriales(request, response);
                    break;
                case "listByRange":
                    listarMaterialesPorRango(request, response);
                    break;
                default:
                    listarMateriales(request, response);
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
                    agregarMaterial(request, response);
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
    
    private void listarMateriales(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        List<DtMaterial> materiales = materialClient.obtenerMateriales();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(materiales);
        response.getWriter().write(json);
    }
    
    private void listarMaterialesPorRango(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ParseException {
        
        String fechaInicioStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");
        
        if (fechaInicioStr == null || fechaFinStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Fechas de inicio y fin son requeridas\"}");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicio = sdf.parse(fechaInicioStr);
        Date fechaFin = sdf.parse(fechaFinStr);
        
        List<DtMaterial> materiales = materialClient.obtenerMaterialesPorRango(fechaInicio, fechaFin);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(materiales);
        response.getWriter().write(json);
    }
    
    private void agregarMaterial(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ParseException {
        
        String tipo = request.getParameter("tipo");
        String fechaRegistroStr = request.getParameter("fechaRegistro");
        
        if (tipo == null || fechaRegistroStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Tipo y fecha de registro son requeridos\"}");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaRegistro = sdf.parse(fechaRegistroStr);
        
        DtMaterial material = null;
        
        if ("libro".equals(tipo)) {
            String titulo = request.getParameter("titulo");
            String cantPagStr = request.getParameter("cantPag");
            
            if (titulo == null || cantPagStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Título y cantidad de páginas son requeridos para libros\"}");
                return;
            }
            
            int cantPag = Integer.parseInt(cantPagStr);
            material = new DtLibro(fechaRegistro, titulo, cantPag);
            
        } else if ("articulo".equals(tipo)) {
            String descripcion = request.getParameter("descripcion");
            String pesoStr = request.getParameter("peso");
            String dimFisicaStr = request.getParameter("dimFisica");
            
            if (descripcion == null || pesoStr == null || dimFisicaStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Descripción, peso y dimensión física son requeridos para artículos\"}");
                return;
            }
            
            float peso = Float.parseFloat(pesoStr);
            float dimFisica = Float.parseFloat(dimFisicaStr);
            material = new DtArticuloEspecial(fechaRegistro, descripcion, peso, dimFisica);
            
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Tipo de material no válido\"}");
            return;
        }
        
        materialClient.agregarMaterial(material);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Material agregado exitosamente\"}");
    }
}

