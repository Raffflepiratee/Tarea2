package com.biblioteca.publicadores;

import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;

import com.biblioteca.datatypes.DtUsuario;

@WebService(targetNamespace = "http://publicadores/")
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public interface UsuarioPublishController {

    @WebMethod
    void agregarUsuario(DtUsuario usuario);

    @WebMethod
    DtUsuario[] obtenerUsuarios();
}
