package com.biblioteca.publicadores;

import java.util.Date;
import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;

import com.biblioteca.datatypes.DtPrestamo;
import com.biblioteca.datatypes.EstadosP;
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

    @WebMethod
    void cambiarEstadoPrestamo(DtPrestamo prestamo, EstadosP nuevoEstado);

    @WebMethod
    void cambiarMaterialPrestamo(DtPrestamo prestamo, int nuevoMaterialID);

    @WebMethod
    void cambiarCorreoLectorPrestamo(DtPrestamo prestamo, String nuevoCorreo);

    @WebMethod
    void cambiarCorreoBibliotecarioPrestamo(DtPrestamo prestamo, String nuevoCorreo);

    @WebMethod
    void cambiarFechaDevolucionPrestamo(DtPrestamo prestamo, Date nuevaFecha);

    @WebMethod
    void cambiarFechaSolicitudPrestamo(DtPrestamo prestamo, Date nuevaFecha);
}