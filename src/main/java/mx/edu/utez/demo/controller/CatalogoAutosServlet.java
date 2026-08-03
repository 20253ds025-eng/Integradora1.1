package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.model.dao.AutomovilDAO;

import java.io.IOException;
import java.util.List;

// Fíjate bien en el value: "/CatalogoCliente"
@WebServlet(name = "CatalogoAutosServlet", value = "/CatalogoCliente")
public class CatalogoAutosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Instanciamos el DAO
        AutomovilDAO dao = new AutomovilDAO();

        // 2. Traemos solo los autos que no se han vendido y que son de Agencia
        List<AutomovilDTO> listaAutos = dao.getDisponibles();

        // 3. Guardamos la lista en la petición para que el JSP la intercepte
        request.setAttribute("listaAutos", listaAutos);

        // 4. Mandamos al usuario a la vista del catálogo dinámico
        request.getRequestDispatcher("/Cliente_Catalogo_Coches.jsp").forward(request, response);
    }
}