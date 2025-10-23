package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.biblioteca.client.MaterialServiceClient;
import com.biblioteca.datatypes.DtLibro;
import com.biblioteca.datatypes.DtMaterial;
import com.biblioteca.datatypes.DtArticuloEspecial;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet para manejar operaciones de Materiales
 */
@WebServlet("/agregarMaterial")
public class agregarMaterialServlet extends HttpServlet {

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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoMaterial = request.getParameter("tipoMaterial");
        response.setContentType("application/json;charset=UTF-8");
        try {
            if ("LIBRO".equals(tipoMaterial)) {

                String titulo = request.getParameter("titulo");
                String cantPagStr = request.getParameter("cantPag");
                String fechaRegStr = request.getParameter("fechaRegistro");

                if (titulo == null || cantPagStr == null || fechaRegStr == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Missing required parameters for LIBRO\"}");
                    return;
                }

                Integer cantPag;
                try {
                    cantPag = Integer.parseInt(cantPagStr);
                } catch (NumberFormatException nfe) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid integer for cantPag: '" + cantPagStr + "'\"}");
                    return;
                }

                Date fechaReg;
                try {
                    fechaReg = new SimpleDateFormat("yyyy-MM-dd").parse(fechaRegStr);
                } catch (ParseException pe) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter()
                            .write("{\"error\": \"Invalid date format for fechaRegistro: '" + fechaRegStr + "'\"}");
                    return;
                }

                DtLibro libro = new DtLibro(fechaReg, titulo, cantPag);
                agregarMaterial(libro);

            } else {
                String fechaRegStr = request.getParameter("fechaRegistro");
                String descripcion = request.getParameter("descripcion");
                String pesoStr = request.getParameter("peso");
                String dimFisicaStr = request.getParameter("dimFisica");

                if (fechaRegStr == null || descripcion == null || pesoStr == null || dimFisicaStr == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Missing required parameters for ARTICULO\"}");
                    return;
                }

                Date fechaReg;
                try {
                    fechaReg = new SimpleDateFormat("yyyy-MM-dd").parse(fechaRegStr);
                } catch (ParseException pe) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter()
                            .write("{\"error\": \"Invalid date format for fechaRegistro: '" + fechaRegStr + "'\"}");
                    return;
                }

                Float peso;
                Float dimFisica;
                try {
                    peso = Float.parseFloat(pesoStr);
                } catch (NumberFormatException nfe) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid float for peso: '" + pesoStr + "'\"}");
                    return;
                }
                try {
                    dimFisica = Float.parseFloat(dimFisicaStr);
                } catch (NumberFormatException nfe) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid float for dimFisica: '" + dimFisicaStr + "'\"}");
                    return;
                }

                DtArticuloEspecial articulo = new DtArticuloEspecial(fechaReg, descripcion, peso, dimFisica);
                agregarMaterial(articulo);

            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\": \"Success\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void agregarMaterial(DtMaterial material) {
        System.out.println(" Agregando material en 135 Servlet: " + material + "\n");
        MaterialServiceClient client = new MaterialServiceClient();
        client.agregarMaterial(material);
    }

}
