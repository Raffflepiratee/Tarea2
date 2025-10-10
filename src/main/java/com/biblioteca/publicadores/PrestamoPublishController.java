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

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public interface PrestamoPublishController {

    @WebMethod
    void agregarPrestamo(Date fechaSoli, Date fechaDev, EstadosP estadoP,
            String correoLector, String correoBiblio, int idMaterial);

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
