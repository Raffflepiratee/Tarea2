package com.biblioteca.publicadores;

import java.util.Date;
import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;

import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.Zonas;

@WebService(targetNamespace = "http://publicadores/")
@SOAPBinding(style = Style.DOCUMENT, parameterStyle = ParameterStyle.WRAPPED)
public interface PrestamoPublishController {

    @WebMethod
    void agregarPrestamo(DtPrestamo prestamo);

    @WebMethod
    DtPrestamo[] obtenerPrestamos();

    @WebMethod
    DtPrestamo[] obtenerPrestamosPendientes();

    @WebMethod
    DtPrestamo[] obtenerPrestamosPorZona(Zonas zona);

    @WebMethod
    DtPrestamo[] obtenerPrestamosPorBibliotecario(int idEmp);

    @WebMethod
    DtPrestamo[] obtenerPrestamosActivosLector(String correoLector);
}
