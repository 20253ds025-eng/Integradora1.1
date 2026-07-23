package mx.edu.utez.demo.model;
public class ServicioDTO {

    private int idServicio;
    private String nombreServicio;
    private String descripcion;
    private double costo;
    private String tipoAplicacion;
    private boolean activo;

    public ServicioDTO() {}

    public ServicioDTO(int idServicio, String nombreServicio, String descripcion,
                       double costo, String tipoAplicacion) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.costo = costo;
        this.tipoAplicacion = tipoAplicacion;
        this.activo = true;
    }

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public String getTipoAplicacion() { return tipoAplicacion; }
    public void setTipoAplicacion(String tipoAplicacion) { this.tipoAplicacion = tipoAplicacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombreServicio + " - $" + costo;
    }
}