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
import com.biblioteca.publicadores.UsuarioPublishController;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/ModificarUsuario")
public class ModificarUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ModificarUsuarioServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // No se utiliza en este servlet
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        string correo = request.getParameter("correo");
        string zona = request.getParameter("zona");
        EstadosU estado = EstadosU.valueOf(request.getParameter("estado"));

        try {
            modificarUsuario(correo, zona, estado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modificarUsuario(String correo, String zona, EstadosU estado) throws Exception {
    }
}
