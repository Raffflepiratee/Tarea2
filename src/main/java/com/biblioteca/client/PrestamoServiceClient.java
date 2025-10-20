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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Cliente para consumir el servicio web de Préstamos
 */
public class PrestamoServiceClient {

    private static final String PRESTAMO_SERVICE_URL = "http://localhost:8080/prestamos?wsdl";
    private PrestamoPublishController prestamoService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public PrestamoServiceClient() {
        try {
            System.out.println("Intentando conectar a: " + PRESTAMO_SERVICE_URL);
            URL url = URI.create(PRESTAMO_SERVICE_URL).toURL();
            System.out.println("URL creada: " + url);
            Service service = Service.create(url,
                    new javax.xml.namespace.QName("http://publicadores/", "PrestamoPublishControllerService"));
            prestamoService = service.getPort(PrestamoPublishController.class);
            System.out.println("Service creado");
            System.out.println("Conectado al servicio de prestamo en " + PRESTAMO_SERVICE_URL);
        } catch (Exception e) {
            System.err.println("No se pudo conectar con el servicio de prestamo, usando datos de prueba. Error: "
                    + e.getClass().getName() + " - " + e.getMessage());
            System.err.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            prestamoService = null;
        }
    }

    /**
     * Agrega un nuevo préstamo
     */
    public void agregarPrestamo(DtPrestamo prestamo) {
        if (prestamoService != null) {
            try {
                prestamoService.agregarPrestamo(prestamo);
                System.out.println("Préstamo agregado al backend: " + prestamo);
            } catch (WebServiceException e) {
                System.err.println("Error al agregar préstamo: " + e.getMessage());
                throw new RuntimeException("Error al agregar préstamo", e);
            }
        } else {
            System.out.println("Préstamo agregado (modo prueba): " + prestamo);
        }
    }

    /**
     * Obtiene todos los préstamos
     */
    public List<DtPrestamo> obtenerPrestamos() {
        List<DtPrestamo> lista = new ArrayList<>();
        if (prestamoService != null) {
            try {
                DtPrestamo[] prestamos = prestamoService.obtenerPrestamos();
                System.out.println("PrestamoServiceClient - obtenerTodosLosPrestamos raw length: "
                        + (prestamos == null ? 0 : prestamos.length));
                try {
                    System.out.println("PrestamoServiceClient - obtenerTodosLosPrestamos RAW JSON: "
                            + objectMapper.writeValueAsString(prestamos));
                } catch (Exception ex) {
                    System.out.println("PrestamoServiceClient - ERROR serializing array: " + ex);
                }

                if (prestamos != null) {
                    boolean allDefault = true;
                    for (int i = 0; i < prestamos.length; i++) {
                        DtPrestamo prestamo = prestamos[i];
                        try {
                            System.out
                                    .println("Prestamo[" + i + "] JSON: " + objectMapper.writeValueAsString(prestamo));
                        } catch (Exception ex) {
                            System.out.println("Prestamo[" + i + "] toString: " + prestamo);
                        }
                        try {
                            System.out.println("Prestamo[" + i + "] getMaterial(): " + prestamo.getMaterial());
                        } catch (Exception ex) {
                            System.out.println("Prestamo[" + i + "] getMaterial() error: " + ex.getMessage());
                        }

                        if (prestamo != null) {
                            if (prestamo.getIdPrestamo() != 0 || prestamo.getMaterial() != 0
                                    || prestamo.getFechaSoli() != null || prestamo.getFechaDev() != null
                                    || prestamo.getLector() != null || prestamo.getBibliotecario() != null
                                    || prestamo.getEstadoPres() != null) {
                                allDefault = false;
                            }
                        }
                        lista.add(prestamo);
                    }

                    if (prestamos.length > 0 && allDefault) {
                        System.out.println(
                                "PrestamoServiceClient - Proxy returned default DTOs, attempting SOAP fallback...");
                        try {
                            List<DtPrestamo> parsed = parsePrestamosFromSoapEndpoint(
                                    PRESTAMO_SERVICE_URL.replace("?wsdl", ""));
                            if (parsed != null && !parsed.isEmpty()) {
                                System.out.println(
                                        "PrestamoServiceClient - SOAP fallback parsed " + parsed.size() + " prestamos");
                                return parsed;
                            } else {
                                System.out.println(
                                        "PrestamoServiceClient - SOAP fallback returned empty list, keeping proxy results");
                            }
                        } catch (Exception ex) {
                            System.err.println("PrestamoServiceClient - SOAP fallback failed: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("PrestamoServiceClient - obtenerTodosLosPrestamos: prestamos es null");
                }
            } catch (WebServiceException e) {
                System.err.println("Error al obtener todos los préstamos (WebServiceException): " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error inesperado al obtener todos los préstamos: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("PrestamoServiceClient - servicio no disponible (modo prueba), devolviendo lista vacía");
        }
        return lista;
    }

    /**
     * Obtiene préstamos pendientes
     */
    public List<DtPrestamo> obtenerPrestamosPendientes() {
        List<DtPrestamo> lista = new ArrayList<>();
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
                    System.out.println("PrestamoServiceClient - ERROR serializing array: " + ex);
                }

                if (prestamos != null) {
                    boolean allDefault = true;
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
                        // detect if object fields are all defaults/nulls
                        if (prestamo != null) {
                            if (prestamo.getIdPrestamo() != 0 || prestamo.getMaterial() != 0
                                    || prestamo.getFechaSoli() != null
                                    || prestamo.getFechaDev() != null || prestamo.getLector() != null
                                    || prestamo.getBibliotecario() != null || prestamo.getEstadoPres() != null) {
                                allDefault = false;
                            }
                        }
                        lista.add(prestamo);
                    }

                    // If proxy returned DTOs but fields are default (likely deserialization
                    // mismatch), try SOAP fallback
                    if (prestamos.length > 0 && allDefault) {
                        System.out.println(
                                "PrestamoServiceClient - Proxy returned default/unpopulated DTOs, attempting SOAP fallback parse...");
                        try {
                            List<DtPrestamo> parsed = parsePrestamosFromSoapEndpoint(
                                    PRESTAMO_SERVICE_URL.replace("?wsdl", ""));
                            if (parsed != null && !parsed.isEmpty()) {
                                System.out.println(
                                        "PrestamoServiceClient - SOAP fallback parsed " + parsed.size() + " prestamos");
                                return parsed;
                            } else {
                                System.out.println(
                                        "PrestamoServiceClient - SOAP fallback returned empty list, keeping proxy results");
                            }
                        } catch (Exception ex) {
                            System.err.println("PrestamoServiceClient - SOAP fallback failed: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("PrestamoServiceClient - obtenerPrestamosPendientes: prestamos es null");
                }
            } catch (WebServiceException e) {
                System.err.println("Error al obtener préstamos pendientes (WebServiceException): " + e.getMessage());
                e.printStackTrace();
                // no lanzar; devolvemos lista vacía para que el front no rompa
            } catch (Exception e) {
                System.err.println("Error inesperado al obtener préstamos pendientes: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("PrestamoServiceClient - servicio no disponible (modo prueba), devolviendo lista vacía");
            // Podríamos agregar datos mock aquí si deseas
        }
        return lista;
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

    // Auxiliares
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

    /**
     * Realiza una llamada HTTP SOAP al endpoint y parsea la respuesta XML para
     * construir DtPrestamo.
     * NOS SABEMOS Q HACE PERO FUNCIONA, al final vemos de cambiarlo
     */
    private List<DtPrestamo> parsePrestamosFromSoapEndpoint(String endpointUrl) throws Exception {
        List<DtPrestamo> result = new ArrayList<>();

        String soapEnvelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pub=\"http://publicadores/\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<pub:obtenerPrestamosPendientes/>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        URL url = URI.create(endpointUrl).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("Accept", "text/xml");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(soapEnvelope.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        InputStream in = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        // read response
        StringBuilder respSb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                respSb.append(line).append('\n');
            }
        }

        String resp = respSb.toString();
        System.out.println("PrestamoServiceClient - SOAP fallback response length: " + resp.length());
        System.out.println(resp);

        // parse XML
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new java.io.ByteArrayInputStream(resp.getBytes(StandardCharsets.UTF_8)));

        XPath xpath = XPathFactory.newInstance().newXPath();
        // find all <return> nodes (common for JAX-WS responses)
        NodeList returns = (NodeList) xpath.evaluate("//*[local-name() = 'return']", doc, XPathConstants.NODESET);
        if (returns == null || returns.getLength() == 0) {
            // try to find any DtPrestamo-like nodes
            returns = (NodeList) xpath.evaluate("//*[local-name() = 'DtPrestamo' or local-name() = 'prestamo']", doc,
                    XPathConstants.NODESET);
        }

        for (int i = 0; returns != null && i < returns.getLength(); i++) {
            Node returnNode = returns.item(i);
            if (returnNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            Element e = (Element) returnNode;

            // If the <return> node contains multiple child elements representing DtPrestamo
            // (common when the SOAP response wraps the array in one <return>), iterate its
            // element children.
            NodeList childElements = e.getChildNodes();
            boolean parsedChilds = false;
            for (int c = 0; childElements != null && c < childElements.getLength(); c++) {
                Node child = childElements.item(c);
                if (child.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                Element childElem = (Element) child;
                // Heuristic: if child element contains an idPrestamo field, treat it as a
                // DtPrestamo node
                String possibleId = getChildText(childElem, "idPrestamo");
                String possibleMaterial = getChildText(childElem, "material");
                if (possibleId != null || possibleMaterial != null) {
                    parsedChilds = true;
                    Integer idPrestamo = tryParseInt(getChildText(childElem, "idPrestamo"));
                    Date fechaSoli = tryParseDate(getChildText(childElem, "fechaSoli"));
                    String estadoStr = getChildText(childElem, "estadoPres");
                    EstadosP estado = null;
                    if (estadoStr != null && !estadoStr.isEmpty()) {
                        try {
                            estado = EstadosP.valueOf(estadoStr);
                        } catch (Exception ex) {
                            // ignore
                        }
                    }
                    Date fechaDev = tryParseDate(getChildText(childElem, "fechaDev"));
                    String lector = getChildText(childElem, "lector");
                    String bibliotecario = getChildText(childElem, "bibliotecario");
                    Integer material = tryParseInt(getChildText(childElem, "material"));

                    DtPrestamo dp = new DtPrestamo(
                            idPrestamo == null ? 0 : idPrestamo,
                            fechaSoli,
                            estado,
                            fechaDev,
                            lector,
                            bibliotecario,
                            material == null ? 0 : material);
                    result.add(dp);
                }
            }

            if (parsedChilds) {
                continue; // processed nested items
            }

            // Fallback: parse the return element itself as a DtPrestamo
            Integer idPrestamo = tryParseInt(getChildText(e, "idPrestamo"));
            Date fechaSoli = tryParseDate(getChildText(e, "fechaSoli"));
            String estadoStr = getChildText(e, "estadoPres");
            EstadosP estado = null;
            if (estadoStr != null && !estadoStr.isEmpty()) {
                try {
                    estado = EstadosP.valueOf(estadoStr);
                } catch (Exception ex) {
                    // ignore
                }
            }
            Date fechaDev = tryParseDate(getChildText(e, "fechaDev"));
            String lector = getChildText(e, "lector");
            String bibliotecario = getChildText(e, "bibliotecario");
            Integer material = tryParseInt(getChildText(e, "material"));

            DtPrestamo dp = new DtPrestamo(
                    idPrestamo == null ? 0 : idPrestamo,
                    fechaSoli,
                    estado,
                    fechaDev,
                    lector,
                    bibliotecario,
                    material == null ? 0 : material);
            result.add(dp);
        }

        return result;
    }

    private String getChildText(Element parent, String childName) {
        NodeList nodes = parent.getElementsByTagName(childName);
        if (nodes != null && nodes.getLength() > 0) {
            Node n = nodes.item(0);
            if (n != null)
                return n.getTextContent();
        }
        // try with any namespace
        NodeList any = parent.getElementsByTagNameNS("*", childName);
        if (any != null && any.getLength() > 0) {
            Node n = any.item(0);
            if (n != null)
                return n.getTextContent();
        }
        return null;
    }

    private Integer tryParseInt(String s) {
        if (s == null || s.trim().isEmpty())
            return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Date tryParseDate(String s) {
        if (s == null || s.trim().isEmpty())
            return null;
        String v = s.trim();
        // Try ISO 8601-ish formats
        String[] patterns = { "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd" };
        for (String p : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(p);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sdf.parse(v.replaceAll("Z$", "Z"));
            } catch (Exception e) {
                // continue
            }
        }
        return null;
    }
}