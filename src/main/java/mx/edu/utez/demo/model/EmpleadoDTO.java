package mx.edu.utez.demo.model;


import java.sql.Date;

public class EmpleadoDTO {

    private int idEmpleado;
    private String nombre;
    private String correo;
    private String rol;
    private Date fechaContratacion;
    private boolean activo;

    public EmpleadoDTO() {}

    public EmpleadoDTO(int idEmpleado, String nombre, String correo, Date fechaContratacion) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaContratacion = fechaContratacion;
        this.activo = true;
    }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Date getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(Date fechaContratacion) { this.fechaContratacion = fechaContratacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre + " - " + correo;
    }
}