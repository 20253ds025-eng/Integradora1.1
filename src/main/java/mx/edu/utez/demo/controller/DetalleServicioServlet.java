package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.model.ServicioDTO;
import mx.edu.utez.demo.model.dao.AutomovilDAO;
import mx.edu.utez.demo.model.dao.ServicioDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DetalleServicioServlet", value = "/DetalleServicioServlet")
public class DetalleServicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                Integer id = Integer.parseInt(idParam);

                ServicioDAO servicioDAO = new ServicioDAO();
                ServicioDTO servicio = servicioDAO.getById(id);

                if (servicio != null) {
                    request.setAttribute("servicio", servicio);

                    AutomovilDAO autoDAO = new AutomovilDAO();

                    // --- Vehículos que puede elegir el cliente: comprados en la
                    //     agencia + registrados como Externo por él mismo ---
                    HttpSession session = request.getSession(false);
                    Integer idCliente = (session != null) ? (Integer) session.getAttribute("usuario") : null;

                    List<AutomovilDTO> vehiculosCliente = new ArrayList<>();
                    if (idCliente != null) {
                        vehiculosCliente = autoDAO.getVehiculosDeCliente(idCliente);
                    }

                    // Si venimos desde "Agregar servicio" en el detalle de un auto que
                    // aún no se ha comprado, lo agregamos como opción preseleccionada.
                    String matriculaAuto = request.getParameter("matriculaAuto");
                    if (matriculaAuto != null && !matriculaAuto.isBlank()) {
                        boolean yaEsta = vehiculosCliente.stream()
                                .anyMatch(v -> v.getMatricula().equals(matriculaAuto));
                        if (!yaEsta) {
                            AutomovilDTO pendiente = autoDAO.getById(matriculaAuto);
                            if (pendiente != null) {
                                vehiculosCliente.add(0, pendiente);
                            }
                        }
                        request.setAttribute("matriculaPreseleccionada", matriculaAuto);
                    }

                    request.setAttribute("vehiculosCliente", vehiculosCliente);

                    request.getRequestDispatcher("/Cliente_Detalle_Servicio.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/CatalogoServiciosCliente");
    }
}
