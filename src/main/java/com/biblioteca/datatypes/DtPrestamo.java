package com.biblioteca.datatypes;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtPrestamo", propOrder = { "idPrestamo", "fechaSoli", "estadoPres", "fechaDev", "lector", "bibliotecario",
        "material" })
public class DtPrestamo {
    private int idPrestamo;
    private Date fechaSoli;
    private EstadosP estadoPres;
    private Date fechaDev;

    // ver de cambiarlos a objetos
    private String lector;
    private String bibliotecario;
    private int material;

    // ni lo usamos
    public DtPrestamo(Date fechaSoli, EstadosP estadoPres, Date fechaDev, String correoLector,
            String correoBibliotecario, int idMaterial) {
        this.fechaSoli = fechaSoli;
        this.estadoPres = estadoPres;
        this.fechaDev = fechaDev;
        this.lector = correoLector;
        this.bibliotecario = correoBibliotecario;
        this.material = idMaterial;
    }

    // Constructor completo para mostrar datos en la tabla de listar prestamos :)
    public DtPrestamo(int idPrestamo, Date fechaSoli, EstadosP estadoPres, Date fechaDev, String correoLector,
            String correoBibliotecario, int idMaterial) {
        this.idPrestamo = idPrestamo;
        this.fechaSoli = fechaSoli;
        this.estadoPres = estadoPres;
        this.fechaDev = fechaDev;
        this.lector = correoLector;
        this.bibliotecario = correoBibliotecario;
        this.material = idMaterial;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public Date getFechaSoli() {
        return fechaSoli;
    }

    public EstadosP getEstadoPres() {
        return estadoPres;
    }

    public Date getFechaDev() {
        return fechaDev;
    }

    public String getLector() {
        return lector;
    }

    public String getBibliotecario() {
        return bibliotecario;
    }

    public int getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return "DtPrestamo [idPrestamo=" + idPrestamo + ", fechaSoli=" + fechaSoli + ", estadoPres=" + estadoPres
                + ", fechaDev=" + fechaDev + "]";
    }
}

