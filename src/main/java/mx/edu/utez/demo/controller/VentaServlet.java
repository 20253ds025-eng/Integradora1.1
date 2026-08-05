package mx.edu.utez.demo.controller;


import mx.edu.utez.demo.model.dao.*;
import mx.edu.utez.demo.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/VentaServlet")
public class VentaServlet extends HttpServlet {

    private VentaDAO ventaDAO;
    private DetalleVentaDAO detalleDAO;
    private AutomovilDAO autoDAO;
    private ClienteDAO clienteDAO;
    private ContratacionDAO contratacionDAO;
    private ServicioDAO servicioDAO;

    @Override
    public void init() {
        ventaDAO = new VentaDAO();
        detalleDAO = new DetalleVentaDAO();
        autoDAO = new AutomovilDAO();
        clienteDAO = new ClienteDAO();
        contratacionDAO = new ContratacionDAO();
        servicioDAO = new ServicioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null || session.getAttribute("rol") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        String rol = (String) session.getAttribute("rol");
        String action = req.getParameter("action");

        if ("listar".equals(action) || action == null) {
            if ("Empleado".equals(rol)) {
                int idAsesor = (int) session.getAttribute("usuario");
                List<VentaDTO> ventas = ventaDAO.getByAsesor(idAsesor);
                req.setAttribute("ventas", ventas);
            } else if ("Dueno".equals(rol)) {
                List<VentaDTO> ventas = ventaDAO.getAll();
                req.setAttribute("ventas", ventas);
            } else {
                int idCliente = (int) session.getAttribute("usuario");
                List<VentaDTO> ventas = ventaDAO.getByCliente(idCliente);
                req.setAttribute("ventas", ventas);
            }
            req.getRequestDispatcher("ventas.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        String action = req.getParameter("action");
        if ("registrar".equals(action)) {
            int idAsesor = (int) session.getAttribute("usuario");
            int idCliente = Integer.parseInt(req.getParameter("idCliente"));
            String matricula = req.getParameter("matricula");
            String tipoAdquisicion = req.getParameter("tipoAdquisicion");
            String estatusPago = req.getParameter("estatusPago");
            double precio = Double.parseDouble(req.getParameter("precio"));

            if (!"Sucursal".equals(tipoAdquisicion) && !"Linea".equals(tipoAdquisicion)) {
                resp.sendRedirect("VentaServlet?error=Tipo de adquisicion invalido");
                return;
            }
            if (!"Completado".equals(estatusPago) && !"En espera de recepcion/aplicacion".equals(estatusPago)) {
                resp.sendRedirect("VentaServlet?error=Estatus de pago invalido");
                return;
            }

            // Obtener datos del auto para el precio
            AutomovilDTO auto = autoDAO.getById(matricula);
            if (auto == null || auto.isVendido()) {
                resp.sendRedirect("VentaServlet?error=Auto no disponible");
                return;
            }

            // Crear la venta
            VentaDTO venta = new VentaDTO();
            venta.setIdCliente(idCliente);
            venta.setIdAsesorHistorico(idAsesor);
            venta.setTipoAdquisicion(tipoAdquisicion);
            venta.setEstatusPago(estatusPago);
            venta.setTotal(precio);

            int idVenta = ventaDAO.createReturnId(venta);
            if (idVenta > 0) {
                // Crear detalle
                DetalleVentaDTO detalle = new DetalleVentaDTO();
                detalle.setIdVenta(idVenta);
                detalle.setMatriculaAuto(matricula);
                detalle.setPrecioVenta(precio);
                detalleDAO.create(detalle);

                // Marcar auto como vendido
                autoDAO.marcarVendido(matricula);

                // Si hay servicios adicionales
                String serviciosExtra = req.getParameter("serviciosExtra");
                if (serviciosExtra != null && !serviciosExtra.isEmpty()) {
                    String[] ids = serviciosExtra.split(",");
                    for (String idStr : ids) {
                        int idServicio = Integer.parseInt(idStr);
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
                            
                            contratacion.setEstatusServicio("Pendiente_Aplicacion");
                            contratacionDAO.create(contratacion);
                        }
                    }
                }

                resp.sendRedirect("VentaServlet?mensaje=Venta registrada correctamente");
            } else {
                resp.sendRedirect("VentaServlet?error=Error al registrar venta");
            }
        }
    }

}
