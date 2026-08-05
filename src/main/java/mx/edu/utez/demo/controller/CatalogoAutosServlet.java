package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.model.dao.AutomovilDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CatalogoAutosServlet", value = "/CatalogoCliente")
public class CatalogoAutosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AutomovilDAO dao = new AutomovilDAO();

        // Si es petición AJAX (para selección de auto en servicio), incluir externos
        if ("1".equals(request.getParameter("ajax"))) {
            List<AutomovilDTO> listaAutos = dao.getDisponiblesParaServicio();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (AutomovilDTO a : listaAutos) {
                if (!first) sb.append(",");
                first = false;
                sb.append("{");
                sb.append("\"matricula\":\"").append(esc(a.getMatricula())).append("\",");
                sb.append("\"marca\":\"").append(esc(a.getMarca())).append("\",");
                sb.append("\"modelo\":\"").append(esc(a.getModelo())).append("\",");
                sb.append("\"anio\":").append(a.getAnio()).append(",");
                sb.append("\"precio\":").append(a.getPrecio()).append(",");
                sb.append("\"tipoOrigen\":\"").append(esc(a.getTipoOrigen())).append("\",");
                sb.append("\"imagen\":\"").append(esc(a.getImagen())).append("\"");
                sb.append("}");
            }
            sb.append("]");
            out.write(sb.toString());
            return;
        }

        // Si no es AJAX, forward al JSP (solo agencia)
        List<AutomovilDTO> listaAutos = dao.getDisponibles();
        request.setAttribute("listaAutos", listaAutos);
        request.getRequestDispatcher("/Cliente_Catalogo_Coches.jsp").forward(request, response);
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}