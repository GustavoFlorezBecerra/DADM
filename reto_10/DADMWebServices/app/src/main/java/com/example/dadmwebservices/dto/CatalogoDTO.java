package com.example.dadmwebservices.dto;

public class CatalogoDTO {

    private String programaRecoleccion;

    private String tipoResiduos;

    private String departamento;

    private String municipio;

    private String codigoDane;

    private String direccion;

    private String razonSocial;

    public CatalogoDTO(){

    }

    public CatalogoDTO(String programaRecoleccion, String tipoResiduos, String departamento, String municipio, String codigoDane, String direccion, String razonSocial) {
        this.programaRecoleccion = programaRecoleccion;
        this.tipoResiduos = tipoResiduos;
        this.departamento = departamento;
        this.municipio = municipio;
        this.codigoDane = codigoDane;
        this.direccion = direccion;
        this.razonSocial = razonSocial;
    }

    public String getProgramaRecoleccion() {
        return programaRecoleccion;
    }

    public void setProgramaRecoleccion(String programaRecoleccion) {
        this.programaRecoleccion = programaRecoleccion;
    }

    public String getTipoResiduos() {
        return tipoResiduos;
    }

    public void setTipoResiduos(String tipoResiduos) {
        this.tipoResiduos = tipoResiduos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCodigoDane() {
        return codigoDane;
    }

    public void setCodigoDane(String codigoDane) {
        this.codigoDane = codigoDane;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Override
    public String toString() {
        return programaRecoleccion +
                ";" + tipoResiduos +
                ";" + departamento +
                ";" + municipio +
                ";" + codigoDane +
                ";" + direccion +
                ";" + razonSocial;
    }
}
