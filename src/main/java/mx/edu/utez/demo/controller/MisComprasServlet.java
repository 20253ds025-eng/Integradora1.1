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
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/MisComprasServlet")
public class MisComprasServlet extends HttpServlet {

    private VentaDAO ventaDAO;
    private DetalleVentaDAO detalleDAO;
    private ContratacionDAO contratacionDAO;

    @Override
    public void init() {
        ventaDAO = new VentaDAO();
        detalleDAO = new DetalleVentaDAO();
        contratacionDAO = new ContratacionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"No hay sesion activa\"}");
            return;
        }

        int idCliente = (int) session.getAttribute("usuario");
        String tipo = req.getParameter("tipo"); // "Auto" o "Servicio"

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            if ("Servicio".equals(tipo)) {
                // Obtener contrataciones de servicios del cliente
                List<ContratacionDTO> contrataciones = contratacionDAO.getByCliente(idCliente);
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (ContratacionDTO c : contrataciones) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{");
                    sb.append("\"id\":\"").append(c.getFolio() != null ? c.getFolio() : "CT-" + c.getIdContratacion()).append("\",");
                    sb.append("\"tipo\":\"Servicio\",");
                    sb.append("\"total\":\"$").append(String.format("%,.2f", c.getCostoAplicado())).append(" MXN\",");
                    sb.append("\"estado\":\"").append(c.getEstatusServicio() != null ? c.getEstatusServicio() : "Pendiente").append("\",");
                    sb.append("\"nombreServicio\":\"").append(escapeJson(c.getNombreServicio())).append("\",");
                    sb.append("\"matriculaAuto\":\"").append(escapeJson(c.getMatriculaAuto())).append("\"");
                    sb.append("}");
                }
                sb.append("]");
                out.write(sb.toString());
            } else {
                // Obtener ventas (autos) del cliente
                List<VentaDTO> ventas = ventaDAO.getByCliente(idCliente);
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (VentaDTO v : ventas) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{");
                    sb.append("\"id\":\"").append(v.getFolio() != null ? v.getFolio() : "VT-" + v.getIdVenta()).append("\",");
                    sb.append("\"tipo\":\"Auto\",");
                    sb.append("\"total\":\"$").append(String.format("%,.2f", v.getTotal())).append(" MXN\",");
                    sb.append("\"estado\":\"").append(v.getEstatusPago() != null ? v.getEstatusPago() : "Pendiente").append("\",");
                    sb.append("\"fechaVenta\":\"").append(v.getFechaVenta() != null ? v.getFechaVenta().toString().substring(0, 10) : "N/A").append("\",");
                    sb.append("\"nombreCliente\":\"").append(escapeJson(v.getNombreCliente())).append("\",");
                    sb.append("\"nombreAsesor\":\"").append(escapeJson(v.getNombreAsesor())).append("\"");
                    sb.append("}");
                }
                sb.append("]");
                out.write(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"error\":\"Error al cargar las compras\"}");
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
