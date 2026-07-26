package mx.edu.utez.demo.model;

public class DetalleVentaDTO {

    private int idDetalle;
    private int idVenta;
    private String matriculaAuto;
    private String marca;
    private String modelo;
    private double precioVenta;

    public DetalleVentaDTO() {}

    public DetalleVentaDTO(int idVenta, String matriculaAuto, double precioVenta) {
        this.idVenta = idVenta;
        this.matriculaAuto = matriculaAuto;
        this.precioVenta = precioVenta;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String getMatriculaAuto() { return matriculaAuto; }
    public void setMatriculaAuto(String matriculaAuto) { this.matriculaAuto = matriculaAuto; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    @Override
    public String toString() {
        return matriculaAuto + " - $" + precioVenta;
    }
}