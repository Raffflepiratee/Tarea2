package com.biblioteca.datatypes;

public class DtBibliotecario extends DtUsuario {

    private int IdEmp;

    // Constructor vacío requerido por JAXB
    public DtBibliotecario() {
        super();
    }

    public DtBibliotecario(String nombre, String email, String password) {
        super(nombre, email, password);
    }

    public DtBibliotecario(String nombre, String email, String password, int IdEmp) {
        super(nombre, email, password);
        this.IdEmp = IdEmp;
    }

    public int getIdEmp() {
        return IdEmp;
    }

    @Override
    public String toString() {
        return super.toString() + "\nID EMPLEADO = " + IdEmp;
    }

}

