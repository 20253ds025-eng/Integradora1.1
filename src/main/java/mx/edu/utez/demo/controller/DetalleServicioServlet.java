package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.demo.model.ServicioDTO;
import mx.edu.utez.demo.model.dao.ServicioDAO;

import java.io.IOException;

@WebServlet(name = "DetalleServicioServlet", value = "/DetalleServicioServlet")
public class DetalleServicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                // Convertimos el ID de String a Integer
                Integer id = Integer.parseInt(idParam);

                // Buscamos el servicio en Oracle
                ServicioDAO dao = new ServicioDAO();
                ServicioDTO servicio = dao.getById(id);

                if (servicio != null) {
                    // Si existe, lo mandamos a la nueva vista dinámica
                    request.setAttribute("servicio", servicio);
                    request.getRequestDispatcher("/Cliente_Detalle_Servicio.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Si alguien pone letras en la URL en lugar de números
                e.printStackTrace();
            }
        }

        // Si hay error, regresamos al catálogo de servicios
        response.sendRedirect(request.getContextPath() + "/CatalogoServiciosCliente");
    }
}