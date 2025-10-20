package com.biblioteca.datatypes;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DtPrestamo", propOrder = { "idPrestamo", "fechaSoli", "estadoPres", "fechaDev", "lector",
        "bibliotecario",
        "material" })
public class DtPrestamo {
    @XmlElement
    private int idPrestamo;
    @XmlElement
    private Date fechaSoli;
    @XmlElement
    private EstadosP estadoPres;
    @XmlElement
    private Date fechaDev;

    // ver de cambiarlos a objetos
    @XmlElement
    private String lector;
    @XmlElement
    private String bibliotecario;
    @XmlElement
    private int material;

    public DtPrestamo() {
    }

    // ni lo usamos
    public DtPrestamo(Date fechaSoli, EstadosP estadoPres, Date fechaDev, String correoLector,
            String correoBibliotecario, int material) {
        this.fechaSoli = fechaSoli;
        this.estadoPres = estadoPres;
        this.fechaDev = fechaDev;
        this.lector = correoLector;
        this.bibliotecario = correoBibliotecario;
        this.material = material;
    }

    // Constructor completo para mostrar datos en la tabla de listar prestamos :)
    public DtPrestamo(int idPrestamo, Date fechaSoli, EstadosP estadoPres, Date fechaDev, String correoLector,
            String correoBibliotecario, int material) {
        this.idPrestamo = idPrestamo;
        this.fechaSoli = fechaSoli;
        this.estadoPres = estadoPres;
        this.fechaDev = fechaDev;
        this.lector = correoLector;
        this.bibliotecario = correoBibliotecario;
        this.material = material;
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

    /* @XmlElement(name = "material") */
    public int getMaterial() {
        return material;
    }

    // quiero todos los setter
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public void setFechaSoli(Date fechaSoli) {
        this.fechaSoli = fechaSoli;
    }

    public void setEstadoPres(EstadosP estadoPres) {
        this.estadoPres = estadoPres;
    }

    public void setFechaDev(Date fechaDev) {
        this.fechaDev = fechaDev;
    }

    public void setLector(String lector) {
        this.lector = lector;
    }

    public void setBibliotecario(String bibliotecario) {
        this.bibliotecario = bibliotecario;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "DtPrestamo [idPrestamo=" + idPrestamo + ", fechaSoli=" + fechaSoli + ", estadoPres=" + estadoPres
                + ", fechaDev=" + fechaDev + ", lector=" + lector + ", bibliotecario=" + bibliotecario + ", material= "
                + material + "]";
    }
}
