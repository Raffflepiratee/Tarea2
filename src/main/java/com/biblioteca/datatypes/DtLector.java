package com.biblioteca.datatypes;

import java.util.Date;

public class DtLector extends DtUsuario {
    private Date fechaIngreso;
    private EstadosU estadoUsuario;
    private Zonas zona;
    private String direccion;

    // Constructor vacío requerido por JAXB
    public DtLector() {
        super();
    }

    public DtLector(String nombre, String email, String password, Date fechaIngreso, EstadosU estadoUsuario, Zonas zona, String direccion) {
        super(nombre, email, password);
        this.fechaIngreso = fechaIngreso;
        this.estadoUsuario = estadoUsuario;
        this.zona = zona;
        this.direccion = direccion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public EstadosU getEstadoUsuario() {
        return estadoUsuario;
    }

    public Zonas getZona() {
        return zona;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return super.toString() + "\nFECHA DE INGRESO = " + fechaIngreso + "\nESTADO USUARIO = " + estadoUsuario
                + "\nZONA = " + zona + "\nDIRECCION = " + direccion;
    }
    
}

