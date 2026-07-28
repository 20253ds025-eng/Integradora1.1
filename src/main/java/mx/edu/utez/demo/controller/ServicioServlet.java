package mx.edu.utez.demo.controller;



import mx.edu.utez.demo.model.dao.ServicioDAO;
import mx.edu.utez.demo.model.ServicioDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ServicioServlet")
public class ServicioServlet extends HttpServlet {

    private ServicioDAO servicioDAO;

    @Override
    public void init() {
        servicioDAO = new ServicioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            List<ServicioDTO> servicios = servicioDAO.getAll();
            req.setAttribute("servicios", servicios);
            req.getRequestDispatcher("servicios.jsp").forward(req, resp);
        } else if ("ver".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            ServicioDTO servicio = servicioDAO.getById(id);
            req.setAttribute("servicio", servicio);
            req.getRequestDispatcher("detalle-servicio.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("registrar".equals(action)) {
            String nombre = req.getParameter("nombre");
            String descripcion = req.getParameter("descripcion");
            double costo = Double.parseDouble(req.getParameter("costo"));
            String tipo = req.getParameter("tipoAplicacion");

            ServicioDTO servicio = new ServicioDTO();
            servicio.setNombreServicio(nombre);
            servicio.setDescripcion(descripcion);
            servicio.setCosto(costo);
            servicio.setTipoAplicacion(tipo);

            boolean creado = servicioDAO.create(servicio);
            if (creado) {
                resp.sendRedirect("ServicioServlet?mensaje=Servicio creado correctamente");
            } else {
                resp.sendRedirect("ServicioServlet?error=Error al crear servicio");
            }
        }
    }
}