package com.biblioteca.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// import java.util.List;
import java.util.List;

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

@WebServlet("/listarMaterialesPorRango")
public class listarMaterialesPorRangoServlet extends HttpServlet {

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
        String fechaInicioStr = request.getParameter("fechaInicio");
        Date fechaInicio;
        try {
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicioStr);
        } catch (ParseException pe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter()
                    .write("{\"error\": \"Invalid date format for fechaRegistro: '" + fechaInicioStr + "'\"}");
            return;
        }
        String fechaFinStr = request.getParameter("fechaFin");
        Date fechaFin;
        try {
            fechaFin = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinStr);
        } catch (ParseException pe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter()
                    .write("{\"error\": \"Invalid date format for fechaRegistro: '" + fechaFinStr + "'\"}");
            return;
        }
        listarMaterialesPorRango(request, response, fechaInicio, fechaFin);
    }

    private void listarMaterialesPorRango(HttpServletRequest request, HttpServletResponse response, Date fechaInicio,
            Date fechaFin) throws IOException {
        MaterialServiceClient client = new MaterialServiceClient();
        List<DtMaterial> materiales = client.obtenerMaterialesPorRango(fechaInicio, fechaFin);

        StringBuilder json = new StringBuilder("[");
        java.text.SimpleDateFormat salidaFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < materiales.size(); i++) {
            String idMaterialstr = "";
            String fechaRegstr = "";
            String tipo = "Material";
            String detalles = "";
            DtMaterial dtmat = materiales.get(i);
            if (dtmat instanceof DtLibro) {
                DtLibro libro = (DtLibro) dtmat;
                idMaterialstr = String.valueOf(libro.getIdMaterial());
                fechaRegstr = libro.getFechaRegistro() != null ? salidaFecha.format(libro.getFechaRegistro()) : "";
                tipo = "Libro";
                detalles = "Título: " + libro.getTitulo() + "\n Páginas: " + libro.getCantPag();
                System.out.println("Id Material" + libro.getIdMaterial() + "Libro: " + libro.getTitulo() + ", Páginas: "
                        + libro.getCantPag());
            } else if (dtmat instanceof DtArticuloEspecial) {
                DtArticuloEspecial articulo = (DtArticuloEspecial) dtmat;
                idMaterialstr = String.valueOf(articulo.getIdMaterial());
                fechaRegstr = articulo.getFechaRegistro() != null ? salidaFecha.format(articulo.getFechaRegistro())
                        : "";
                tipo = "Artículo Especial";
                detalles = "Descripción: " + articulo.getDescripcion() + "\n Peso: " + articulo.getPeso()
                        + "\n DimFisica: " + articulo.getDimFisica();
                System.out.println(
                        "Id Material" + articulo.getIdMaterial() + "Artículo Especial: " + articulo.getDescripcion()
                                + ", Peso: " + articulo.getPeso() + ", DimFisica: " + articulo.getDimFisica());
            }

            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                    .append("\"ID Material\":\"").append(escaparJson(idMaterialstr)).append("\",")
                    .append("\"Fecha de Registro\":\"").append(escaparJson(fechaRegstr)).append("\",")
                    .append("\"Tipo\":\"").append(escaparJson(tipo)).append("\",")
                    .append("\"Detalles\":\"").append(escaparJson(detalles)).append("\"")
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
}
