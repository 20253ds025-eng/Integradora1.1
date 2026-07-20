package mx.edu.utez.demo.model;

import java.util.List;

public class User {

    private int id;
    private String nombre;
    private String apellidos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCodigo_recuperacion() {
        return codigo_recuperacion;
    }

    public void setCodigo_recuperacion(String codigo_recuperacion) {
        this.codigo_recuperacion = codigo_recuperacion;
    }

    private String correo;
    private String contrasena;
    private String codigo_recuperacion;


}
