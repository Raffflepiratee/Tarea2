package com.biblioteca.client;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;

import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;
import com.biblioteca.datatypes.Zonas;
import com.biblioteca.publicadores.PrestamoPublishController;

/**
 * Cliente para consumir el servicio web de Préstamos
 */
public class PrestamoServiceClient {
    
    private static final String PRESTAMO_SERVICE_URL = "http://localhost:8080/prestamos?wsdl";
    private PrestamoPublishController prestamoService;
    
    public PrestamoServiceClient() {
        try {
            URL url = new URL(PRESTAMO_SERVICE_URL);
            Service service = Service.create(url, 
                new javax.xml.namespace.QName("http://publicadores/", "PrestamoPublishControllerService"));
            prestamoService = service.getPort(PrestamoPublishController.class);
            System.out.println("✅ Conectado al servicio de préstamos");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo conectar con el servicio de préstamos, usando datos de prueba: " + e.getMessage());
            prestamoService = null; // Usar datos mock
        }
    }
    
    /**
     * Agrega un nuevo préstamo
     */
    public void agregarPrestamo(Date fechaSoli, Date fechaDev, EstadosP estadoP,
            String correoLector, String correoBiblio, int idMaterial) {
        if (prestamoService != null) {
            try {
                prestamoService.agregarPrestamo(fechaSoli, fechaDev, estadoP, correoLector, correoBiblio, idMaterial);
                System.out.println("✅ Préstamo agregado al backend: " + correoLector + " -> " + idMaterial);
            } catch (WebServiceException e) {
                System.err.println("Error al agregar préstamo: " + e.getMessage());
                throw new RuntimeException("Error al agregar préstamo", e);
            }
        } else {
            System.out.println("✅ Préstamo agregado (modo prueba): " + correoLector + " -> " + idMaterial);
        }
    }
    
    /**
     * Obtiene todos los préstamos
     */
    public List<DtPrestamo> obtenerPrestamos() {
        if (prestamoService != null) {
            try {
                DtPrestamo[] prestamos = prestamoService.obtenerPrestamos();
                List<DtPrestamo> lista = new ArrayList<>();
                for (DtPrestamo prestamo : prestamos) {
                    lista.add(prestamo);
                }
                return lista;
            } catch (WebServiceException e) {
                System.err.println("Error al obtener préstamos: " + e.getMessage());
                throw new RuntimeException("Error al obtener préstamos", e);
            }
        } else {
            // Datos de prueba
            List<DtPrestamo> prestamos = new ArrayList<>();
            prestamos.add(new DtPrestamo(1, new Date(), EstadosP.PENDIENTE, new Date(), 
                "juan@test.com", "biblio@test.com", 1));
            prestamos.add(new DtPrestamo(2, new Date(), EstadosP.EN_CURSO, new Date(), 
                "maria@test.com", "biblio@test.com", 2));
            prestamos.add(new DtPrestamo(3, new Date(), EstadosP.DEVUELTO, new Date(), 
                "carlos@test.com", "biblio@test.com", 3));
            System.out.println("📋 Retornando préstamos de prueba: " + prestamos.size() + " elementos");
            return prestamos;
        }
    }
    
    /**
     * Obtiene préstamos pendientes
     */
    public List<DtPrestamo> obtenerPrestamosPendientes() {
        if (prestamoService != null) {
            try {
                DtPrestamo[] prestamos = prestamoService.obtenerPrestamosPendientes();
                List<DtPrestamo> lista = new ArrayList<>();
                for (DtPrestamo prestamo : prestamos) {
                    lista.add(prestamo);
                }
                return lista;
            } catch (WebServiceException e) {
                System.err.println("Error al obtener préstamos pendientes: " + e.getMessage());
                throw new RuntimeException("Error al obtener préstamos pendientes", e);
            }
        } else {
            // Datos de prueba - solo pendientes
            List<DtPrestamo> prestamos = new ArrayList<>();
            prestamos.add(new DtPrestamo(1, new Date(), EstadosP.PENDIENTE, new Date(), 
                "juan@test.com", "biblio@test.com", 1));
            System.out.println("📋 Retornando préstamos pendientes de prueba: " + prestamos.size() + " elementos");
            return prestamos;
        }
    }
    
    /**
     * Obtiene préstamos por zona
     */
    public List<DtPrestamo> obtenerPrestamosPorZona(Zonas zona) {
        try {
            DtPrestamo[] prestamos = prestamoService.obtenerPrestamosPorZona(zona);
            List<DtPrestamo> lista = new ArrayList<>();
            for (DtPrestamo prestamo : prestamos) {
                lista.add(prestamo);
            }
            return lista;
        } catch (WebServiceException e) {
            System.err.println("Error al obtener préstamos por zona: " + e.getMessage());
            throw new RuntimeException("Error al obtener préstamos por zona", e);
        }
    }
    
    /**
     * Obtiene préstamos por bibliotecario
     */
    public List<DtPrestamo> obtenerPrestamosPorBibliotecario(int idEmp) {
        try {
            DtPrestamo[] prestamos = prestamoService.obtenerPrestamosPorBibliotecario(idEmp);
            List<DtPrestamo> lista = new ArrayList<>();
            for (DtPrestamo prestamo : prestamos) {
                lista.add(prestamo);
            }
            return lista;
        } catch (WebServiceException e) {
            System.err.println("Error al obtener préstamos por bibliotecario: " + e.getMessage());
            throw new RuntimeException("Error al obtener préstamos por bibliotecario", e);
        }
    }
    
    /**
     * Obtiene préstamos activos de un lector
     */
    public List<DtPrestamo> obtenerPrestamosActivosLector(String correoLector) {
        try {
            DtPrestamo[] prestamos = prestamoService.obtenerPrestamosActivosLector(correoLector);
            List<DtPrestamo> lista = new ArrayList<>();
            for (DtPrestamo prestamo : prestamos) {
                lista.add(prestamo);
            }
            return lista;
        } catch (WebServiceException e) {
            System.err.println("Error al obtener préstamos activos del lector: " + e.getMessage());
            throw new RuntimeException("Error al obtener préstamos activos del lector", e);
        }
    }
    
    /**
     * Verifica si el servicio está disponible
     */
    public boolean isServiceAvailable() {
        try {
            prestamoService.obtenerPrestamos();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

