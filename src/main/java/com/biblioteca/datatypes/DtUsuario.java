package com.biblioteca.datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtUsuario", propOrder = { "nombre", "correo", "password" })
@XmlSeeAlso({DtLector.class, DtBibliotecario.class})

public class DtUsuario {
    private String nombre;
    private String correo;
    private String password;

    // Constructor vacío requerido por JAXB
    public DtUsuario() {
        super();
    }

    public DtUsuario(String nombre, String correo, String password) {
        super();
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public String toString() {
        return "NOMBRE = " + nombre + "\nCORREO = " + correo + "\nCONTRASEÑA = " + password;

    }
}

