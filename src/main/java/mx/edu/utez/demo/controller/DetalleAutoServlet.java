package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.model.dao.AutomovilDAO;

import java.io.IOException;
@WebServlet(name = "DetalleAutoServlet", value = "/DetalleAutoServlet")
public class DetalleAutoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener la matrícula (ID) desde la URL (ej. ?id=AUC-001)
        String matricula = request.getParameter("id");

        if (matricula != null && !matricula.trim().isEmpty()) {

            // 2. Usar el DAO para buscar el coche en Oracle
            AutomovilDAO dao = new AutomovilDAO();
            AutomovilDTO vehiculo = dao.getById(matricula);

            // 3. Si el coche existe, lo mandamos a la vista
            if (vehiculo != null) {
                request.setAttribute("vehiculo", vehiculo);
                request.getRequestDispatcher("/Cliente_Detalle_Coche.jsp").forward(request, response);
                return; // Terminamos la ejecución aquí
            }
        }

        // 4. Si alguien manipula la URL y pone un ID falso, lo regresamos al catálogo
        response.sendRedirect(request.getContextPath() + "/CatalogoCliente");
    }
}