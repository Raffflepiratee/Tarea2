package com.biblioteca.client;

import java.net.URI;
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
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Cliente para consumir el servicio web de Préstamos
 */
public class PrestamoServiceClient {

    private static final String PRESTAMO_SERVICE_URL = "http://localhost:8080/prestamos?wsdl";
    private PrestamoPublishController prestamoService;
    private ObjectMapper objectMapper;

    public PrestamoServiceClient() {
        try {
            System.out.println("Intentando conectar a: " + PRESTAMO_SERVICE_URL);
            URL url = URI.create(PRESTAMO_SERVICE_URL).toURL();
            Service service = Service.create(url,
                    new javax.xml.namespace.QName("http://publicadores/", "PrestamoPublishControllerService"));
            prestamoService = service.getPort(PrestamoPublishController.class);
            System.out.println("✅ Conectado al servicio de préstamos");
        } catch (Exception e) {
            System.err.println(
                    "⚠️ No se pudo conectar con el servicio de préstamos, usando datos de prueba: " + e.getMessage());
            prestamoService = null; // Usar datos mock
        }
        objectMapper = new ObjectMapper();
    }

    /**
     * Agrega un nuevo préstamo
     */
    public void agregarPrestamo(DtPrestamo prestamo) {
        // Log del DtPrestamo que recibe el cliente
        try {
            System.out.println("PrestamoServiceClient.agregarPrestamo - DTO recibido: "
                    + objectMapper.writeValueAsString(prestamo));
        } catch (Exception ex) {
            System.out.println("PrestamoServiceClient - error serializando prestamo: " + ex.getMessage());
        }

        if (prestamoService != null) {
            try {
                // si la interfaz del servicio expone agregarPrestamo(DtPrestamo)
                prestamoService.agregarPrestamo(prestamo);
                System.out.println("PrestamoServiceClient - llamado a webservice agregarPrestamo realizado");
            } catch (WebServiceException e) {
                System.err.println("PrestamoServiceClient - Error al agregar préstamo: " + e.getMessage());
                throw new RuntimeException("Error al agregar préstamo", e);
            }
        } else {
            // modo prueba: loguear y simular agregado
            System.out.println("PrestamoServiceClient - modo prueba, no hay servicio remoto. Prestamo: " + prestamo);
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
                System.out.println("PrestamoServiceClient - obtenerPrestamosPendientes raw length: "
                        + (prestamos == null ? 0 : prestamos.length));
                try {
                    System.out.println("PrestamoServiceClient - obtenerPrestamosPendientes RAW JSON: "
                            + objectMapper.writeValueAsString(prestamos));
                } catch (Exception ex) {
                    // ignorar si falla la serialización
                    System.out.println("PrestamoServiceClient - ERROR " + ex);
                }

                List<DtPrestamo> lista = new ArrayList<>();

                if (prestamos != null) {
                    for (int i = 0; i < prestamos.length; i++) {
                        DtPrestamo prestamo = prestamos[i];
                        try {
                            System.out
                                    .println("Prestamo[" + i + "] JSON: " + objectMapper.writeValueAsString(prestamo));
                        } catch (Exception ex) {
                            System.out.println("Prestamo[" + i + "] toString: " + prestamo);
                        }
                        try {
                            System.out.println(
                                    "Prestamo[" + i + "] getMaterial(): " + String.valueOf(prestamo.getMaterial()));
                        } catch (Exception ex) {
                            System.out.println("Prestamo[" + i + "] getMaterial() error: " + ex.getMessage());
                        }
                        lista.add(prestamo);
                    }
                } else {
                    System.out.println("PrestamoServiceClient - obtenerPrestamosPendientes: prestamos es null");
                }
                return lista;
            } catch (WebServiceException e) {
                System.err.println("Error al obtener préstamos pendientes: " + e.getMessage());
                throw new RuntimeException("Error al obtener préstamos pendientes", e);
            }
        } else {
            throw new RuntimeException("Servicio de préstamos no disponible en modo prueba");
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
