package com.example.dadmsql.db;

public class EmpresaDTO {

    private Integer id;
    private String nombre;
    private String url;
    private String telefono;
    private String email;
    private String producto;
    private String clasificacion;

    public  EmpresaDTO(){

    }

    public EmpresaDTO(Integer id, String nombre, String url, String telefono, String email, String producto, String clasificacion) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.telefono = telefono;
        this.email = email;
        this.producto = producto;
        this.clasificacion = clasificacion;
    }

    public EmpresaDTO(String nombre, String url, String telefono, String email, String producto, String clasificacion) {
        this.nombre = nombre;
        this.url = url;
        this.telefono = telefono;
        this.email = email;
        this.producto = producto;
        this.clasificacion = clasificacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
