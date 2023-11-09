package com.example.dadmsql.db.util;

public enum DetalleEmpresa {

    NOMBRE_COLUMNA_ID("id"),
    NOMBRE_COLUMNA_NOMBRE_EMPRESA("nombre"),
    NOMBRE_COLUMNA_URL("url"),
    NOMBRE_COLUMNA_TELEFONO_CONTACTO("telefono"),
    NOMBRE_COLUMNA_EMAIL_CONTACTO("email"),
    NOMBRE_COLUMNA_PRODUCTOS("producto"),
    NOMBRE_COLUMNA_CLASIFICACION("clasificacion");

    private String nombre;

    DetalleEmpresa(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }
}
