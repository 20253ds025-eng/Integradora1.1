package mx.edu.utez.demo.model;


import java.sql.Date;

public class ContratacionDTO {

    private int idContratacion;
    private String folio;
    private int idVenta;
    private int idCliente;
    private int idServicio;
    private String matriculaAuto;
    private double costoAplicado;
    private Date fechaContratacion;
    private Date fechaVigenciaInicio;
    private Date fechaVigenciaFin;
    private String estatusServicio;

    // Campos adicionales para JOIN
    private String nombreServicio;
    private String tipoAplicacion;
    private String marca;
    private String modelo;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    public ContratacionDTO() {}

    public ContratacionDTO(int idContratacion, int idCliente, int idServicio,
                           String matriculaAuto, double costoAplicado,
                           Date fechaVigenciaInicio, String estatusServicio) {
        this.idContratacion = idContratacion;
        this.idCliente = idCliente;
        this.idServicio = idServicio;
        this.matriculaAuto = matriculaAuto;
        this.costoAplicado = costoAplicado;
        this.fechaVigenciaInicio = fechaVigenciaInicio;
        this.estatusServicio = estatusServicio;
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public int getIdContratacion() {
        return idContratacion;
    }

    public void setIdContratacion(int idContratacion) {
        this.idContratacion = idContratacion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getMatriculaAuto() {
        return matriculaAuto;
    }

    public void setMatriculaAuto(String matriculaAuto) {
        this.matriculaAuto = matriculaAuto;
    }

    public double getCostoAplicado() {
        return costoAplicado;
    }

    public void setCostoAplicado(double costoAplicado) {
        this.costoAplicado = costoAplicado;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Date getFechaVigenciaInicio() {
        return fechaVigenciaInicio;
    }

    public void setFechaVigenciaInicio(Date fechaVigenciaInicio) {
        this.fechaVigenciaInicio = fechaVigenciaInicio;
    }

    public Date getFechaVigenciaFin() {
        return fechaVigenciaFin;
    }

    public void setFechaVigenciaFin(Date fechaVigenciaFin) {
        this.fechaVigenciaFin = fechaVigenciaFin;
    }

    public String getEstatusServicio() {
        return estatusServicio;
    }

    public void setEstatusServicio(String estatusServicio) {
        this.estatusServicio = estatusServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getTipoAplicacion() {
        return tipoAplicacion;
    }

    public void setTipoAplicacion(String tipoAplicacion) {
        this.tipoAplicacion = tipoAplicacion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "ContratacionDTO{" +
                "idContratacion=" + idContratacion +
                ", folio='" + folio + '\'' +
                ", idCliente=" + idCliente +
                ", idServicio=" + idServicio +
                ", matriculaAuto='" + matriculaAuto + '\'' +
                ", costoAplicado=" + costoAplicado +
                ", estatusServicio='" + estatusServicio + '\'' +
                '}';
    }
}