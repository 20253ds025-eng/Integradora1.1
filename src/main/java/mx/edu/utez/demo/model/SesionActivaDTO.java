package mx.edu.utez.demo.model;

import java.sql.Timestamp;

public class SesionActivaDTO {

    private int idSesion;
    private int idUsuario;
    private String tokenSesion;
    private Timestamp fechaInicio;
    private Timestamp fechaExpiracion;
    private String ipOrigen;
    private boolean activa;

    public SesionActivaDTO() {}

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTokenSesion() { return tokenSesion; }
    public void setTokenSesion(String tokenSesion) { this.tokenSesion = tokenSesion; }

    public Timestamp getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

    public Timestamp getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Timestamp fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public String getIpOrigen() { return ipOrigen; }
    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
