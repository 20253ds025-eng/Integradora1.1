package mx.edu.utez.demo.model;

import java.io.Serializable;

/**
 * Representa un ítem dentro del carrito de compras del cliente.
 * Vive únicamente en la sesión HTTP mientras el cliente arma su pedido;
 * al hacer "Proceder a compra" se traduce a filas reales en Ventas,
 * Detalle_Venta_Autos y/o Contrataciones_Servicios.
 */
public class ItemCarritoDTO implements Serializable {

    public static final String TIPO_AUTO = "Auto";
    public static final String TIPO_SERVICIO = "Servicio";

    private String tipo;              // "Auto" o "Servicio"
    private String clave;              // matricula (Auto) o id_servicio como String (Servicio)
    private String nombre;
    private String imagen;
    private double precioUnitario;
    private int cantidad;

    // Solo aplica cuando tipo = "Servicio": a qué auto se le va a aplicar
    private String matriculaAplicacion;
    private String matriculaAplicacionTexto; // "Marca Modelo (MATRICULA)" para mostrar en la vista

    public ItemCarritoDTO() {}

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getMatriculaAplicacion() { return matriculaAplicacion; }
    public void setMatriculaAplicacion(String matriculaAplicacion) { this.matriculaAplicacion = matriculaAplicacion; }

    public String getMatriculaAplicacionTexto() { return matriculaAplicacionTexto; }
    public void setMatriculaAplicacionTexto(String matriculaAplicacionTexto) { this.matriculaAplicacionTexto = matriculaAplicacionTexto; }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }
}
