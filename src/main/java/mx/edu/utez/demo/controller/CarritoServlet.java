package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.demo.model.*;
import mx.edu.utez.demo.model.dao.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Maneja el carrito de compras del cliente. El carrito en sí vive en la
 * sesión HTTP (no tiene su propia tabla en la BD, como cualquier carrito de
 * compras típico); solo al hacer "Proceder a compra" se traduce en filas
 * reales dentro de Ventas, Detalle_Venta_Autos y/o Contrataciones_Servicios.
 */
@WebServlet(name = "CarritoServlet", value = "/CarritoServlet")
public class CarritoServlet extends HttpServlet {

    private static final String ATTR_CARRITO = "carrito";

    private AutomovilDAO autoDAO;
    private ServicioDAO servicioDAO;
    private VentaDAO ventaDAO;
    private DetalleVentaDAO detalleVentaDAO;
    private ContratacionDAO contratacionDAO;
    private ClienteDAO clienteDAO;
    private EmpleadoDAO empleadoDAO;

    @Override
    public void init() {
        autoDAO = new AutomovilDAO();
        servicioDAO = new ServicioDAO();
        ventaDAO = new VentaDAO();
        detalleVentaDAO = new DetalleVentaDAO();
        contratacionDAO = new ContratacionDAO();
        clienteDAO = new ClienteDAO();
        empleadoDAO = new EmpleadoDAO();
    }

    // ==========================================
    // DO GET - Mostrar el carrito
    // ==========================================
    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        List<ItemCarritoDTO> carrito = obtenerCarrito(session);

        double total = 0;
        for (ItemCarritoDTO item : carrito) {
            total += item.getSubtotal();
        }

        request.setAttribute("itemsCarrito", carrito);
        request.setAttribute("totalCarrito", total);
        request.getRequestDispatcher("/carrito.jsp").forward(request, response);
    }

    // ==========================================
    // DO POST - Agregar / eliminar / actualizar / comprar
    // ==========================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);

        if ("agregarAuto".equals(action)) {
            agregarAuto(request, session);
        } else if ("agregarServicio".equals(action)) {
            agregarServicio(request, session);
        } else if ("eliminar".equals(action)) {
            eliminar(request, session);
        } else if ("actualizarCantidad".equals(action)) {
            actualizarCantidad(request, session);
        } else if ("comprar".equals(action)) {
            comprar(request, response, session);
            return; // comprar() ya maneja su propia redirección
        }

        response.sendRedirect(request.getContextPath() + "/CarritoServlet");
    }

    // ==========================================
    // AGREGAR AUTO AL CARRITO
    // ==========================================
    private void agregarAuto(HttpServletRequest request, HttpSession session) {
        String matricula = request.getParameter("matricula");
        if (matricula == null || matricula.isBlank()) return;

        AutomovilDTO auto = autoDAO.getById(matricula);
        if (auto == null || auto.isVendido()) return;

        List<ItemCarritoDTO> carrito = obtenerCarrito(session);

        // Un auto es único por matrícula: si ya está en el carrito, no lo duplicamos.
        boolean yaExiste = carrito.stream().anyMatch(i ->
                ItemCarritoDTO.TIPO_AUTO.equals(i.getTipo()) && matricula.equals(i.getClave()));
        if (yaExiste) return;

        ItemCarritoDTO item = new ItemCarritoDTO();
        item.setTipo(ItemCarritoDTO.TIPO_AUTO);
        item.setClave(matricula);
        item.setNombre(auto.getMarca() + " " + auto.getModelo() + " " + auto.getAnio());
        item.setImagen(auto.getImagen());
        item.setPrecioUnitario(auto.getPrecio());
        item.setCantidad(1);

        carrito.add(item);
        guardarCarrito(session, carrito);
    }

    // ==========================================
    // AGREGAR SERVICIO AL CARRITO (requiere matrícula del vehículo)
    // ==========================================
    private void agregarServicio(HttpServletRequest request, HttpSession session) {
        String idServicioParam = request.getParameter("idServicio");
        String matricula = request.getParameter("matricula");

        if (idServicioParam == null || idServicioParam.isBlank()) return;
        if (matricula == null || matricula.isBlank()) return; // obligatorio elegir vehículo

        int idServicio;
        try {
            idServicio = Integer.parseInt(idServicioParam);
        } catch (NumberFormatException e) {
            return;
        }

        ServicioDTO servicio = servicioDAO.getById(idServicio);
        if (servicio == null) return;

        AutomovilDTO auto = autoDAO.getById(matricula);
        if (auto == null) return;

        Integer idCliente = (Integer) session.getAttribute("usuario");
        if (idCliente == null) return;

        List<ItemCarritoDTO> carrito = obtenerCarrito(session);

        // La matrícula debe ser legítimamente del cliente: ya sea un auto que
        // compró antes, uno que registró como Externo, o uno que trae en el
        // carrito en este momento (lo está comprando ahora mismo).
        boolean esAutoDelCliente = autoDAO.esVehiculoDeCliente(matricula, idCliente);
        boolean esAutoEnCarrito = carrito.stream().anyMatch(i ->
                ItemCarritoDTO.TIPO_AUTO.equals(i.getTipo()) && matricula.equals(i.getClave()));

        if (!esAutoDelCliente && !esAutoEnCarrito) return;

        ItemCarritoDTO item = new ItemCarritoDTO();
        item.setTipo(ItemCarritoDTO.TIPO_SERVICIO);
        item.setClave(String.valueOf(idServicio));
        item.setNombre(servicio.getNombreServicio());
        item.setImagen(servicio.getImagen());
        item.setPrecioUnitario(servicio.getCosto());
        item.setCantidad(1);
        item.setMatriculaAplicacion(matricula);
        item.setMatriculaAplicacionTexto(auto.getMarca() + " " + auto.getModelo() + " (" + matricula + ")");

        carrito.add(item);
        guardarCarrito(session, carrito);
    }

    // ==========================================
    // ELIMINAR ITEM
    // ==========================================
    private void eliminar(HttpServletRequest request, HttpSession session) {
        try {
            int index = Integer.parseInt(request.getParameter("index"));
            List<ItemCarritoDTO> carrito = obtenerCarrito(session);
            if (index >= 0 && index < carrito.size()) {
                carrito.remove(index);
                guardarCarrito(session, carrito);
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // ==========================================
    // ACTUALIZAR CANTIDAD
    // ==========================================
    private void actualizarCantidad(HttpServletRequest request, HttpSession session) {
        try {
            int index = Integer.parseInt(request.getParameter("index"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            if (cantidad < 1) cantidad = 1;

            List<ItemCarritoDTO> carrito = obtenerCarrito(session);
            if (index >= 0 && index < carrito.size()) {
                carrito.get(index).setCantidad(cantidad);
                guardarCarrito(session, carrito);
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // ==========================================
    // CHECKOUT - Aquí es donde todo se refleja en la base de datos
    // ==========================================
    private void comprar(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {

        List<ItemCarritoDTO> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CarritoServlet");
            return;
        }

        Integer idCliente = (Integer) session.getAttribute("usuario");
        if (idCliente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<ItemCarritoDTO> autosCarrito = new ArrayList<>();
        List<ItemCarritoDTO> serviciosCarrito = new ArrayList<>();
        for (ItemCarritoDTO item : carrito) {
            if (ItemCarritoDTO.TIPO_AUTO.equals(item.getTipo())) {
                autosCarrito.add(item);
            } else {
                serviciosCarrito.add(item);
            }
        }

        Integer idVentaCreada = null;

        // --- 1. Si hay autos en el carrito, se crea UNA venta que los agrupa ---
        if (!autosCarrito.isEmpty()) {

            // Regla del DFR: si el cliente no tiene asesor asignado, se le
            // asigna uno automáticamente antes de completar la venta.
            ClienteDTO cliente = clienteDAO.getById(idCliente);
            int idAsesor = (cliente != null) ? cliente.getIdAsesor() : 0;

            if (idAsesor <= 0) {
                Integer asesorAsignado = empleadoDAO.getAsesorConMenosClientes();
                if (asesorAsignado == null) {
                    response.sendRedirect(request.getContextPath()
                            + "/CarritoServlet?error=No hay asesores disponibles en este momento, intenta más tarde");
                    return;
                }
                clienteDAO.reasignarAsesor(idCliente, asesorAsignado);
                idAsesor = asesorAsignado;
            }

            double totalAutos = 0;
            for (ItemCarritoDTO item : autosCarrito) {
                totalAutos += item.getSubtotal();
            }

            VentaDTO venta = new VentaDTO(idCliente, idAsesor, "Linea",
                    "En espera de recepcion/aplicacion", totalAutos);

            int idVenta = ventaDAO.createReturnId(venta);
            if (idVenta <= 0) {
                response.sendRedirect(request.getContextPath()
                        + "/CarritoServlet?error=No se pudo registrar la compra, intenta de nuevo");
                return;
            }
            idVentaCreada = idVenta;

            for (ItemCarritoDTO item : autosCarrito) {
                DetalleVentaDTO detalle = new DetalleVentaDTO(idVenta, item.getClave(), item.getPrecioUnitario());
                detalleVentaDAO.create(detalle);
                autoDAO.marcarVendido(item.getClave());
            }
        }

        // --- 2. Se registran los servicios contratados ---
        for (ItemCarritoDTO item : serviciosCarrito) {
            int idServicio = Integer.parseInt(item.getClave());

            // Si el servicio se aplica a un auto que se está comprando en este
            // mismo pedido, lo enlazamos a la venta recién creada. Si se aplica
            // a un auto que el cliente ya tenía, queda como contratación
            // independiente (id_venta = null, tal como permite el esquema).
            boolean autoEnEstaCompra = autosCarrito.stream()
                    .anyMatch(a -> a.getClave().equals(item.getMatriculaAplicacion()));

            ContratacionDTO contratacion = new ContratacionDTO();
            contratacion.setIdVenta(autoEnEstaCompra && idVentaCreada != null ? idVentaCreada : 0);
            contratacion.setIdCliente(idCliente);
            contratacion.setIdServicio(idServicio);
            contratacion.setMatriculaAuto(item.getMatriculaAplicacion());
            contratacion.setCostoAplicado(item.getSubtotal());
            contratacion.setFechaVigenciaInicio(Date.valueOf(LocalDate.now()));
            contratacion.setEstatusServicio("Pendiente_Aplicacion");

            contratacionDAO.create(contratacion);
        }

        // --- 3. Vaciar el carrito y redirigir a la confirmación ---
        session.removeAttribute(ATTR_CARRITO);
        response.sendRedirect(request.getContextPath() + "/mis_compras.jsp?exito=1");
    }

    // ==========================================
    // HELPERS DE SESIÓN
    // ==========================================
    @SuppressWarnings("unchecked")
    private List<ItemCarritoDTO> obtenerCarrito(HttpSession session) {
        Object obj = session.getAttribute(ATTR_CARRITO);
        if (obj instanceof List) {
            return (List<ItemCarritoDTO>) obj;
        }
        return new ArrayList<>();
    }

    private void guardarCarrito(HttpSession session, List<ItemCarritoDTO> carrito) {
        session.setAttribute(ATTR_CARRITO, carrito);
    }
}
