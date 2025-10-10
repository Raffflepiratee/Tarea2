package com.biblioteca.datatypes;

import java.util.Date;

public class DtLibro extends DtMaterial {
    private String titulo;
    private int cantPag;

    // Mostrar informacion
    public DtLibro(int idMaterial, Date fechaRegistro, String titulo, int cantPag) {
        super(idMaterial, fechaRegistro);
        this.titulo = titulo;
        this.cantPag = cantPag;
    }

    // Registrar nuevo
    public DtLibro(Date fechaRegistro, String titulo, int cantPag) {
        super(fechaRegistro);
        this.titulo = titulo;
        this.cantPag = cantPag;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCantPag() {
        return cantPag;
    }

    @Override
    public String toString() {
        return "DtLibro [titulo=" + titulo + ", cantPag=" + cantPag + ", toString()=" + super.toString() + "]";
    }
}

