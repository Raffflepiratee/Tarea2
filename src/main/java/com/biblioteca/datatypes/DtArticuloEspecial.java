package com.biblioteca.datatypes;

import java.util.Date;

public class DtArticuloEspecial extends DtMaterial {

    private String descripcion;
    private float peso;
    private float dimFisica;

    // Mostrar informacion
    public DtArticuloEspecial(int idMaterial, Date fechaRegistro, String descripcion, float peso, float dimFisica) {
        super(idMaterial, fechaRegistro);
        this.descripcion = descripcion;
        this.peso = peso;
        this.dimFisica = dimFisica;
    }

    // Registrar nuevo
    public DtArticuloEspecial(Date fechaRegistro, String descripcion, float peso, float dimFisica) {
        super(fechaRegistro);
        this.descripcion = descripcion;
        this.peso = peso;
        this.dimFisica = dimFisica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getPeso() {
        return peso;
    }

    public float getDimFisica() {
        return dimFisica;
    }

    @Override
    public String toString() {
        return "DtArticuloEspecial [descripcion=" + descripcion + ", peso=" + peso + ", dimFisica=" + dimFisica
                + ", toString()=" + super.toString() + "]";
    }
}

