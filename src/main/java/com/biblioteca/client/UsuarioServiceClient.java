package com.biblioteca.client;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;

import com.biblioteca.datatypes.DtUsuario;
import com.biblioteca.datatypes.DtLector;
import com.biblioteca.datatypes.DtBibliotecario;
import com.biblioteca.datatypes.Zonas;
import com.biblioteca.datatypes.EstadosU;

import com.biblioteca.publicadores.UsuarioPublishController;

/**
 * Cliente para consumir el servicio web de Usuarios
 */
public class UsuarioServiceClient {
    
    private static final String USUARIO_SERVICE_URL = "http://localhost:8080/usuarios?wsdl";
    private UsuarioPublishController usuarioService;
    
    public UsuarioServiceClient() {
        try {
            System.out.println("Intentando conectar a: " + USUARIO_SERVICE_URL);
            URL url = URI.create(USUARIO_SERVICE_URL).toURL();
            System.out.println("URL creada: " + url);
            Service service = Service.create(url, 
                new javax.xml.namespace.QName("http://publicadores/", "UsuarioPublishControllerService"));
            System.out.println("Service creado");
            usuarioService = service.getPort(UsuarioPublishController.class);
            System.out.println("Conectado al servicio de usuarios en " + USUARIO_SERVICE_URL);
        } catch (Exception e) {
            System.err.println("No se pudo conectar con el servicio de usuarios, usando datos de prueba");
            System.err.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            usuarioService = null; 
        }
    }
    
    /**
     * Agrega un nuevo usuario
     */
    public void agregarUsuario(DtUsuario usuario) {
        if (usuarioService != null) {
            try {
                usuarioService.agregarUsuario(usuario);
                System.out.println("Usuario agregado al backend: " + usuario);
            } catch (WebServiceException e) {
                System.err.println("Error al agregar usuario: " + e.getMessage());
                throw new RuntimeException("Error al agregar usuario", e);
            }
        } else {
            System.out.println("Usuario agregado (modo prueba): " + usuario);
        }
    }
    
    /**
     * Obtiene todos los usuarios
     */
    public List<DtUsuario> obtenerUsuarios() {
        if (usuarioService != null) {
            try {
                DtUsuario[] usuarios = usuarioService.obtenerUsuarios();
                List<DtUsuario> lista = new ArrayList<>();
                for (DtUsuario usuario : usuarios) {
                    if (usuario instanceof DtLector) {
                        lista.add(usuario); // en este caso, solo nos interesan mostrar lectores
                    }
                }
                System.out.println("Usuarios obtenidos del backend: " + lista.size() + " elementos");
                return lista;
            } catch (WebServiceException e) {
                System.err.println("Error al obtener usuarios del backend: " + e.getMessage());
                throw new RuntimeException("Error al obtener usuarios del backend", e);
            }
        } else {
            throw new RuntimeException("Servicio de usuarios no disponible. El backend SOAP no está conectado.");
        }
    }
    
    /**
     * Verifica si el servicio está disponible
     */
    public boolean isServiceAvailable() {
        try {
            usuarioService.obtenerUsuarios();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void cambiarZonaLector(String correo,String zona){
        if (usuarioService != null){
            try{
                usuarioService.cambiarZonaLector(correo, zona);
                System.out.println("Zona cambiada del usuario " + correo + " a " + zona);
            } catch (Exception e) {
                System.err.println("Error al cambiar zona de lector: " + e.getMessage());
                throw new RuntimeException("Error al cambiar zona de lector", e);
            }
        } else {
            throw new RuntimeException("Servicio de usuarios no disponible. El backend SOAP no está conectado.");
        }
    }

    public void cambiarEstadoLector(String correo, EstadosU estado){
        if (usuarioService != null){
            try{
                usuarioService.cambiarEstadoLector(correo, estado);
                System.out.println("Estado cambiado del usuario " + correo + " a " + estado);
            } catch (Exception e) {
                System.err.println("Error al cambiar estado de lector: " + e.getMessage());
                throw new RuntimeException("Error al cambiar estado de lector", e);
            }
        } else {
            throw new RuntimeException("Servicio de usuarios no disponible. El backend SOAP no está conectado.");
        }
    }
}

