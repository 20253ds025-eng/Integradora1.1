package mx.edu.utez.demo.model;

import java.sql.Timestamp;

public class VentaDTO {

    private int idVenta;
    private String folio;
    private int idCliente;
    private String nombreCliente;
    private int idAsesorHistorico;
    private String nombreAsesor;
    private Timestamp fechaVenta;
    private String tipoAdquisicion;
    private String estatusPago;
    private double total;

    public VentaDTO() {}

    public VentaDTO(int idCliente, int idAsesorHistorico, String tipoAdquisicion,
                    String estatusPago, double total) {
        this.idCliente = idCliente;
        this.idAsesorHistorico = idAsesorHistorico;
        this.tipoAdquisicion = tipoAdquisicion;
        this.estatusPago = estatusPago;
        this.total = total;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public int getIdAsesorHistorico() { return idAsesorHistorico; }
    public void setIdAsesorHistorico(int idAsesorHistorico) { this.idAsesorHistorico = idAsesorHistorico; }

    public String getNombreAsesor() { return nombreAsesor; }
    public void setNombreAsesor(String nombreAsesor) { this.nombreAsesor = nombreAsesor; }

    public Timestamp getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Timestamp fechaVenta) { this.fechaVenta = fechaVenta; }

    public String getTipoAdquisicion() { return tipoAdquisicion; }
    public void setTipoAdquisicion(String tipoAdquisicion) { this.tipoAdquisicion = tipoAdquisicion; }

    public String getEstatusPago() { return estatusPago; }
    public void setEstatusPago(String estatusPago) { this.estatusPago = estatusPago; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    @Override
    public String toString() {
        return folio + " - $" + total + " (" + estatusPago + ")";
    }
}