package com.biblioteca.datatypes;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtMaterial", propOrder = { "idMaterial", "fechaRegistro" })
@XmlSeeAlso({DtLibro.class,DtArticuloEspecial.class })
public class DtMaterial {

    private int idMaterial;
    private Date fechaRegistro;

    // Mostrar informacion
    public DtMaterial(int idMaterial, Date fechaRegistro) {
        this.idMaterial = idMaterial;
        this.fechaRegistro = fechaRegistro;
    }

    // Registrar nuevo
    public DtMaterial(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        return "DtMaterial [idMaterial=" + idMaterial + ", fechaRegistro=" + fechaRegistro + "]";
    }
}

