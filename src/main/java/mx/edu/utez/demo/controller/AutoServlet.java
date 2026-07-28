package mx.edu.utez.demo.controller;


import mx.edu.utez.demo.model.dao.AutomovilDAO;
import mx.edu.utez.demo.model.AutomovilDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AutoServlet")
public class AutoServlet extends HttpServlet {

    private AutomovilDAO autoDAO;

    @Override
    public void init() {
        autoDAO = new AutomovilDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            // Listar autos disponibles
            List<AutomovilDTO> autos = autoDAO.getDisponibles();
            req.setAttribute("autos", autos);
            req.getRequestDispatcher("autos.jsp").forward(req, resp);
        } else if ("ver".equals(action)) {
            String matricula = req.getParameter("matricula");
            AutomovilDTO auto = autoDAO.getById(matricula);
            req.setAttribute("auto", auto);
            req.getRequestDispatcher("detalle-auto.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("registrar".equals(action)) {
            // Registrar nuevo auto (solo Dueño)
            String matricula = req.getParameter("matricula");
            String numeroSerie = req.getParameter("numeroSerie");
            String marca = req.getParameter("marca");
            String modelo = req.getParameter("modelo");
            int anio = Integer.parseInt(req.getParameter("anio"));
            String tipoOrigen = req.getParameter("tipoOrigen");
            double precio = Double.parseDouble(req.getParameter("precio"));
            String descripcion = req.getParameter("descripcion");

            AutomovilDTO auto = new AutomovilDTO();
            auto.setMatricula(matricula);
            auto.setNumeroSerie(numeroSerie);
            auto.setMarca(marca);
            auto.setModelo(modelo);
            auto.setAnio(anio);
            auto.setTipoOrigen(tipoOrigen);
            auto.setPrecio(precio);
            auto.setDescripcion(descripcion);

            boolean creado = autoDAO.create(auto);
            if (creado) {
                resp.sendRedirect("AutoServlet?mensaje=Auto registrado correctamente");
            } else {
                resp.sendRedirect("AutoServlet?error=Error al registrar auto");
            }
        }
    }
}
