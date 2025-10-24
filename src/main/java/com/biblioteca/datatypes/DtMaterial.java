package com.biblioteca.datatypes;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtMaterial", propOrder = { "idMaterial", "fechaRegistro" })
@XmlSeeAlso({ DtLibro.class, DtArticuloEspecial.class })
public class DtMaterial {

    private int idMaterial;
    private Date fechaRegistro;

    public DtMaterial() {
    }

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

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "DtMaterial [idMaterial=" + idMaterial + ", fechaRegistro=" + fechaRegistro + "]";
    }
}
