package mx.edu.utez.demo.controller;

import mx.edu.utez.demo.model.dao.*;
import mx.edu.utez.demo.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/CarritoServlet")
public class CarritoServlet extends HttpServlet {

    private VentaDAO ventaDAO;
    private DetalleVentaDAO detalleDAO;
    private AutomovilDAO autoDAO;
    private ServicioDAO servicioDAO;
    private ContratacionDAO contratacionDAO;
    private ClienteDAO clienteDAO;
    private EmpleadoDAO empleadoDAO;

    @Override
    public void init() {
        ventaDAO = new VentaDAO();
        detalleDAO = new DetalleVentaDAO();
        autoDAO = new AutomovilDAO();
        servicioDAO = new ServicioDAO();
        contratacionDAO = new ContratacionDAO();
        clienteDAO = new ClienteDAO();
        empleadoDAO = new EmpleadoDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"No hay sesion activa\"}");
            return;
        }

        int idCliente = (int) session.getAttribute("usuario");

        // Leer JSON del body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();

        // Parseo manual simple del JSON (sin dependencias externas)
        // Espera: {"items":[{"id":"...","tipo":"Auto|Servicio","precio":12345,"cantidad":1},...]}
        try {
            double totalGeneral = 0;

            // Buscar un asesor válido (FK constraint requiere que exista en Empleados)
            int idAsesor = 0;
            ClienteDTO cliente = clienteDAO.getById(idCliente);
            if (cliente != null && cliente.getIdAsesor() > 0) {
                idAsesor = cliente.getIdAsesor();
            } else {
                // Si el cliente no tiene asesor, buscar el primero activo
                List<EmpleadoDTO> empleadosActivos = empleadoDAO.getActivos();
                if (!empleadosActivos.isEmpty()) {
                    idAsesor = empleadosActivos.get(0).getIdEmpleado();
                }
            }

            if (idAsesor <= 0) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"No hay asesores disponibles para procesar la compra\"}");
                return;
            }

            // Extraer array de items
            int itemsStart = json.indexOf("\"items\"");
            if (itemsStart == -1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"Falta el array de items\"}");
                return;
            }

            int arrayStart = json.indexOf("[", itemsStart);
            int arrayEnd = json.indexOf("]", arrayStart);
            String itemsStr = json.substring(arrayStart + 1, arrayEnd);

            // Separar cada objeto de item
            String[] itemObjects = itemsStr.split("\\}\\s*,\\s*\\{");

            // Crear la venta
            VentaDTO venta = new VentaDTO();
            venta.setIdCliente(idCliente);
            venta.setIdAsesorHistorico(idAsesor);
            venta.setTipoAdquisicion("Linea");
            venta.setEstatusPago("En espera de recepcion/aplicacion");
            venta.setTotal(0); // Se calcula abajo

            int idVenta = ventaDAO.createReturnId(venta);
            if (idVenta <= 0) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"Error al crear la venta\"}");
                return;
            }

            boolean alMenosUno = false;

            for (String itemStr : itemObjects) {
                // Limpiar y extraer campos
                itemStr = itemStr.trim();
                if (!itemStr.startsWith("{")) itemStr = "{" + itemStr;

                String id = extractField(itemStr, "id");
                String tipo = extractField(itemStr, "tipo");
                double precio = extractDoubleField(itemStr, "precio");
                int cantidad = (int) extractDoubleField(itemStr, "cantidad");
                if (cantidad <= 0) cantidad = 1;

                double subtotal = precio * cantidad;
                totalGeneral += subtotal;

                if ("Auto".equals(tipo) && id != null && !id.isEmpty()) {
                    // Verificar que el auto exista y no esté vendido
                    AutomovilDTO auto = autoDAO.getById(id);
                    if (auto != null && !auto.isVendido()) {
                        DetalleVentaDTO detalle = new DetalleVentaDTO();
                        detalle.setIdVenta(idVenta);
                        detalle.setMatriculaAuto(id);
                        detalle.setPrecioVenta(precio);
                        detalleDAO.create(detalle);
                        autoDAO.marcarVendido(id);
                        alMenosUno = true;
                    }
                } else if ("Servicio".equals(tipo) && id != null) {
                    String idServicioStr = id.replace("SRV-", "");
                    String matricula = extractField(itemStr, "matricula");
                    try {
                        int idServicio = Integer.parseInt(idServicioStr);
                        ServicioDTO servicio = servicioDAO.getById(idServicio);
                        if (servicio != null) {
                            ContratacionDTO contratacion = new ContratacionDTO();
                            contratacion.setIdVenta(idVenta);
                            contratacion.setIdCliente(idCliente);
                            contratacion.setIdServicio(idServicio);
                            contratacion.setMatriculaAuto(matricula);
                            contratacion.setCostoAplicado(servicio.getCosto());
                            LocalDate hoy = LocalDate.now();
                            contratacion.setFechaVigenciaInicio(Date.valueOf(hoy));
                            
                            // Calcular fecha de fin segun tipo de servicio
                            String tipoApp = servicio.getTipoAplicacion();
                            if ("Mensual".equalsIgnoreCase(tipoApp)) {
                                contratacion.setFechaVigenciaFin(Date.valueOf(hoy.plusMonths(1)));
                            } else if ("Anual".equalsIgnoreCase(tipoApp)) {
                                contratacion.setFechaVigenciaFin(Date.valueOf(hoy.plusYears(1)));
                            }
                            // Para "Unica", fechaVigenciaFin queda NULL
                            
                            contratacion.setEstatusServicio("Pendiente_Aplicacion");
                            contratacionDAO.create(contratacion);
                            alMenosUno = true;
                        }
                    } catch (NumberFormatException e) {
                        // ID de servicio no válido, ignorar
                    }
                }
            }

            // Actualizar el total de la venta
            ventaDAO.updateTotal(idVenta, totalGeneral);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            if (alMenosUno) {
                out.write("{\"success\":true,\"idVenta\":" + idVenta + ",\"total\":" + totalGeneral + "}");
            } else {
                out.write("{\"error\":\"No se pudo procesar ningun item del carrito\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Error al procesar la compra: " + e.getMessage() + "\"}");
        }
    }

    private String extractField(String json, String field) {
        String search = "\"" + field + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;
        int colonIdx = json.indexOf(":", idx);
        int valueStart = json.indexOf("\"", colonIdx + 1);
        int valueEnd = json.indexOf("\"", valueStart + 1);
        if (valueStart == -1 || valueEnd == -1) return null;
        return json.substring(valueStart + 1, valueEnd);
    }

    private double extractDoubleField(String json, String field) {
        String search = "\"" + field + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return 0;
        int colonIdx = json.indexOf(":", idx);
        int valueStart = colonIdx + 1;
        while (valueStart < json.length() && (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '"')) {
            valueStart++;
        }
        int valueEnd = valueStart;
        while (valueEnd < json.length() && json.charAt(valueEnd) != ',' && json.charAt(valueEnd) != '}' && json.charAt(valueEnd) != '"') {
            valueEnd++;
        }
        try {
            return Double.parseDouble(json.substring(valueStart, valueEnd).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
