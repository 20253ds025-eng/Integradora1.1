package mx.edu.utez.demo.controller;

import mx.edu.utez.demo.model.dao.*;
import mx.edu.utez.demo.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/ServiciosVehiculoServlet")
public class ServiciosVehiculoServlet extends HttpServlet {

    private ContratacionDAO contratacionDAO;

    @Override
    public void init() {
        contratacionDAO = new ContratacionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matricula = req.getParameter("matricula");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        if (matricula == null || matricula.isEmpty()) {
            out.write("[]");
            return;
        }

        try {
            List<ContratacionDTO> contrataciones = contratacionDAO.getByMatricula(matricula);
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (ContratacionDTO c : contrataciones) {
                if (!first) sb.append(",");
                first = false;
                sb.append("{");
                sb.append("\"id\":").append(c.getIdContratacion()).append(",");
                sb.append("\"servicio\":\"").append(esc(c.getNombreServicio())).append("\",");
                sb.append("\"costo\":").append(c.getCostoAplicado()).append(",");
                sb.append("\"estatus\":\"").append(esc(c.getEstatusServicio())).append("\",");
                sb.append("\"fecha\":\"").append(c.getFechaContratacion() != null ? c.getFechaContratacion().toString().substring(0, 10) : "N/A").append("\"");
                sb.append("}");
            }
            sb.append("]");
            out.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            out.write("[]");
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
