package com.biblioteca.client;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;

import com.biblioteca.datatypes.DtMaterial;
import com.biblioteca.datatypes.DtLibro;
import com.biblioteca.datatypes.DtArticuloEspecial;
import com.biblioteca.publicadores.MaterialPublishController;

/**
 * Cliente para consumir el servicio web de Materiales
 */
public class MaterialServiceClient {
    
    private static final String MATERIAL_SERVICE_URL = "http://localhost:8080/materiales?wsdl";
    private MaterialPublishController materialService;
    
    public MaterialServiceClient() {
        try {
            URL url = new URL(MATERIAL_SERVICE_URL);
            Service service = Service.create(url, 
                new javax.xml.namespace.QName("http://publicadores/", "MaterialPublishControllerService"));
            materialService = service.getPort(MaterialPublishController.class);
            System.out.println("✅ Conectado al servicio de materiales");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo conectar con el servicio de materiales, usando datos de prueba: " + e.getMessage());
            materialService = null; // Usar datos mock
        }
    }
    
    /**
     * Agrega un nuevo material
     */
    public void agregarMaterial(DtMaterial material) {
        if (materialService != null) {
            try {
                materialService.agregarMaterial(material);
                System.out.println("✅ Material agregado al backend: " + material);
            } catch (WebServiceException e) {
                System.err.println("Error al agregar material: " + e.getMessage());
                throw new RuntimeException("Error al agregar material", e);
            }
        } else {
            System.out.println("✅ Material agregado (modo prueba): " + material);
        }
    }
    
    /**
     * Obtiene todos los materiales
     */
    public List<DtMaterial> obtenerMateriales() {
        if (materialService != null) {
            try {
                DtMaterial[] materiales = materialService.obtenerMateriales();
                List<DtMaterial> lista = new ArrayList<>();
                for (DtMaterial material : materiales) {
                    lista.add(material);
                }
                return lista;
            } catch (WebServiceException e) {
                System.err.println("Error al obtener materiales: " + e.getMessage());
                throw new RuntimeException("Error al obtener materiales", e);
            }
        } else {
            // Datos de prueba
            List<DtMaterial> materiales = new ArrayList<>();
            materiales.add(new DtMaterial(1, new Date()));
            materiales.add(new DtMaterial(2, new Date()));
            materiales.add(new DtMaterial(3, new Date()));
            System.out.println("📋 Retornando materiales de prueba: " + materiales.size() + " elementos");
            return materiales;
        }
    }
    
    /**
     * Obtiene materiales por rango de fechas
     */
    public List<DtMaterial> obtenerMaterialesPorRango(Date fechaInicio, Date fechaFin) {
        if (materialService != null) {
            try {
                DtMaterial[] materiales = materialService.obtenerMaterialesPorRango(fechaInicio, fechaFin);
                List<DtMaterial> lista = new ArrayList<>();
                for (DtMaterial material : materiales) {
                    lista.add(material);
                }
                return lista;
            } catch (WebServiceException e) {
                System.err.println("Error al obtener materiales por rango: " + e.getMessage());
                throw new RuntimeException("Error al obtener materiales por rango", e);
            }
        } else {
            // Datos de prueba filtrados
            List<DtMaterial> materiales = new ArrayList<>();
            materiales.add(new DtMaterial(1, fechaInicio));
            System.out.println("📋 Retornando materiales filtrados de prueba: " + materiales.size() + " elementos");
            return materiales;
        }
    }
    
    /**
     * Verifica si el servicio está disponible
     */
    public boolean isServiceAvailable() {
        try {
            materialService.obtenerMateriales();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

