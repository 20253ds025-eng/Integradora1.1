package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.demo.model.*;
import mx.edu.utez.demo.model.dao.*;

import java.io.BufferedReader;
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

        Integer idCliente = (Integer) session.getAttribute("usuario");
        if (idCliente == null) {
            if ("application/json".equals(request.getContentType()) || request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json")) {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"No hay sesión activa. Inicia sesión para continuar.\"}");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
            return;
        }

        List<ItemCarritoDTO> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) {
            carrito = parsearBodyJson(request);
        }

        if (carrito.isEmpty()) {
            if ("application/json".equals(request.getContentType())) {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"El carrito está vacío\"}");
            } else {
                response.sendRedirect(request.getContextPath() + "/CarritoServlet");
            }
            return;
        }

        List<ItemCarritoDTO> autosCarrito = new ArrayList<>();
        List<ItemCarritoDTO> serviciosCarrito = new ArrayList<>();
        for (ItemCarritoDTO item : carrito) {
            if (ItemCarritoDTO.TIPO_AUTO.equalsIgnoreCase(item.getTipo()) || "Auto".equalsIgnoreCase(item.getTipo())) {
                autosCarrito.add(item);
            } else {
                serviciosCarrito.add(item);
            }
        }

        Integer idVentaCreada = null;

        // --- 1. Si hay autos en el carrito, se crea UNA venta que los agrupa ---
        if (!autosCarrito.isEmpty()) {

            ClienteDTO cliente = clienteDAO.getById(idCliente);
            int idAsesor = (cliente != null) ? cliente.getIdAsesor() : 0;

            if (idAsesor <= 0) {
                Integer asesorAsignado = empleadoDAO.getAsesorConMenosClientes();
                if (asesorAsignado != null && asesorAsignado > 0) {
                    clienteDAO.reasignarAsesor(idCliente, asesorAsignado);
                    idAsesor = asesorAsignado;
                }
            }

            double totalAutos = 0;
            for (ItemCarritoDTO item : autosCarrito) {
                totalAutos += item.getSubtotal();
            }

            VentaDTO venta = new VentaDTO(idCliente, idAsesor, "Linea",
                    "En espera de recepcion/aplicacion", totalAutos);

            int idVenta = ventaDAO.createReturnId(venta);
            if (idVenta > 0) {
                idVentaCreada = idVenta;
                for (ItemCarritoDTO item : autosCarrito) {
                    DetalleVentaDTO detalle = new DetalleVentaDTO(idVenta, item.getClave(), item.getPrecioUnitario());
                    detalleVentaDAO.create(detalle);
                    autoDAO.marcarVendido(item.getClave());
                }
            }
        }

        // --- 2. Se registran los servicios contratados ---
        for (ItemCarritoDTO item : serviciosCarrito) {
            int idServicio = 0;
            try {
                String clave = item.getClave();
                if (clave != null && clave.startsWith("SRV-")) {
                    clave = clave.substring(4);
                }
                idServicio = Integer.parseInt(clave);
            } catch (Exception e) {
                continue;
            }

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

        // --- 3. Vaciar el carrito y responder ---
        session.removeAttribute(ATTR_CARRITO);

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\":true,\"redirect\":\"" + request.getContextPath() + "/mis_compras.jsp?exito=1\"}");
        } else {
            response.sendRedirect(request.getContextPath() + "/mis_compras.jsp?exito=1");
        }
    }

    private List<ItemCarritoDTO> parsearBodyJson(HttpServletRequest request) {
        List<ItemCarritoDTO> items = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString().trim();
            if (json.isEmpty()) return items;

            int idxItems = json.indexOf("\"items\"");
            if (idxItems == -1) return items;

            int startArray = json.indexOf("[", idxItems);
            int endArray = json.lastIndexOf("]");
            if (startArray == -1 || endArray == -1 || endArray <= startArray) return items;

            String arrayContent = json.substring(startArray + 1, endArray);
            String[] rawObjects = arrayContent.split("\\}\\s*,\\s*\\{");
            for (String raw : rawObjects) {
                raw = raw.replace("{", "").replace("}", "").trim();
                if (raw.isEmpty()) continue;

                ItemCarritoDTO item = new ItemCarritoDTO();
                String id = extraerValorJson(raw, "id");
                String nombre = extraerValorJson(raw, "nombre");
                String precioStr = extraerValorJson(raw, "precio");
                if (precioStr == null || precioStr.isEmpty()) {
                    precioStr = extraerValorJson(raw, "precioUnitario");
                }
                String tipo = extraerValorJson(raw, "tipo");
                String cantidadStr = extraerValorJson(raw, "cantidad");
                String matricula = extraerValorJson(raw, "matricula");

                String clave = id;
                if (clave.startsWith("SRV-")) {
                    clave = clave.substring(4);
                }

                item.setClave(clave);
                item.setNombre(nombre);
                item.setTipo(tipo);
                try {
                    item.setPrecioUnitario(precioStr.isEmpty() ? 0 : Double.parseDouble(precioStr.replaceAll("[^0-9.-]", "")));
                } catch(Exception e) { item.setPrecioUnitario(0); }

                try {
                    item.setCantidad(cantidadStr.isEmpty() ? 1 : Integer.parseInt(cantidadStr));
                } catch(Exception e) { item.setCantidad(1); }

                item.setMatriculaAplicacion(matricula);

                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    private String extraerValorJson(String jsonObj, String key) {
        String searchKey = "\"" + key + "\":";
        int idx = jsonObj.indexOf(searchKey);
        if (idx == -1) return "";
        int start = idx + searchKey.length();
        String rest = jsonObj.substring(start).trim();

        if (rest.startsWith("\"")) {
            int endQuote = rest.indexOf("\"", 1);
            if (endQuote != -1) {
                return rest.substring(1, endQuote);
            }
        } else {
            int comma = rest.indexOf(",");
            if (comma != -1) {
                return rest.substring(0, comma).trim();
            } else {
                return rest.trim();
            }
        }
        return "";
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
