package mx.edu.utez.demo.model;

import java.sql.Date;

public class ClienteDTO {

    private int idCliente;
    private String nombre;
    private String correo;
    private String rol;
    private int idAsesor;
    private Date fechaRegistro;

    public ClienteDTO() {}

    public ClienteDTO(int idCliente, String nombre, String correo, int idAsesor, Date fechaRegistro) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.correo = correo;
        this.idAsesor = idAsesor;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getIdAsesor() { return idAsesor; }
    public void setIdAsesor(int idAsesor) { this.idAsesor = idAsesor; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return nombre + " - Asesor: " + idAsesor;
    }
}