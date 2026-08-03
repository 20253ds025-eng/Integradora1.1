package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.demo.model.ServicioDTO;
import mx.edu.utez.demo.model.dao.ServicioDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CatalogoServiciosServlet", value = "/CatalogoServiciosCliente")
public class CatalogoServiciosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Instanciamos el DAO de servicios
        ServicioDAO dao = new ServicioDAO();

        // 2. Traemos todos los servicios activos
        List<ServicioDTO> listaServicios = dao.getAll();

        // 3. Guardamos la lista en la petición
        request.setAttribute("listaServicios", listaServicios);

        // 4. Redirigimos a la vista dinámica
        request.getRequestDispatcher("/Cliente_Catalogo_Serv.jsp").forward(request, response);
    }
}