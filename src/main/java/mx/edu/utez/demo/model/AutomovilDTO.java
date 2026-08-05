package mx.edu.utez.demo.model;


import java.sql.Timestamp;

public class AutomovilDTO {

    private String matricula;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private int anio;
    private String tipoOrigen;
    private double precio;
    private boolean vendido;
    private String descripcion;
    private Timestamp fechaRegistro;
    private String imagen;

    public AutomovilDTO() {}

    public AutomovilDTO(String matricula, String numeroSerie, String marca, String modelo,
                        int anio, String tipoOrigen, double precio, String descripcion, String imagen) {
        this.matricula = matricula;
        this.numeroSerie = numeroSerie;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipoOrigen = tipoOrigen;
        this.precio = precio;
        this.vendido = false;
        this.descripcion = (descripcion != null) ? descripcion : "";
        this.imagen = (imagen != null) ? imagen : "sin_imagen.png";
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getTipoOrigen() { return tipoOrigen; }
    public void setTipoOrigen(String tipoOrigen) { this.tipoOrigen = tipoOrigen; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isVendido() { return vendido; }
    public void setVendido(boolean vendido) { this.vendido = vendido; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + matricula + ")";
    }
}