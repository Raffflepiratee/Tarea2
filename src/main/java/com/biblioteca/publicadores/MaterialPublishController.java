package com.biblioteca.publicadores;

import java.util.Date;
import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;

import com.biblioteca.datatypes.DtMaterial;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public interface MaterialPublishController {

    @WebMethod
    void agregarMaterial(DtMaterial material);

    @WebMethod
    DtMaterial[] obtenerMateriales();

    @WebMethod
    DtMaterial[] obtenerMaterialesPorRango(Date fechaInicio, Date fechaFin);
}
