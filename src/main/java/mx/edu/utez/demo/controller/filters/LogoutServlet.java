package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.demo.model.dao.SesionActivaDAO;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si existe la cookie "recuérdame", invalidamos el token en la BD
        // y borramos la cookie del navegador; si no, el usuario volvería a
        // quedar logueado automáticamente en la siguiente petición.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            SesionActivaDAO sesionDAO = new SesionActivaDAO();
            for (Cookie c : cookies) {
                if ("remember_token".equals(c.getName())) {
                    sesionDAO.invalidarToken(c.getValue());

                    Cookie borrar = new Cookie("remember_token", "");
                    borrar.setMaxAge(0);
                    borrar.setPath(request.getContextPath() + "/");
                    response.addCookie(borrar);
                }
            }
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("usuario");
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}