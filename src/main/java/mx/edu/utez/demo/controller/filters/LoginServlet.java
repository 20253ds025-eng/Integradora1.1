package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.demo.model.dao.SesionActivaDAO;
import mx.edu.utez.demo.model.dao.UsuarioDAO;
import mx.edu.utez.demo.model.UsuarioDTO;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // Días que dura la sesión persistente ("Recuérdame") antes de expirar.
    private static final int DIAS_RECORDARME = 30;
    private static final String COOKIE_RECORDARME = "remember_token";

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        // Validar que no estén vacíos
        if (correo == null || correo.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // Autenticar usuario
            UsuarioDTO usuario = usuarioDAO.autenticar(correo, contrasena);

            if (usuario != null && usuario.isActivo()) {
                // Crear sesión
                HttpSession session = request.getSession(true);

                // CORRECCIÓN AQUÍ: Usamos setAttribute para guardar el ID en la sesión
                session.setAttribute("usuario", usuario.getIdUsuario());

                session.setAttribute("nombre", usuario.getNombre());
                session.setAttribute("correo", usuario.getCorreo());
                session.setAttribute("rol", usuario.getRol());

                // Si el usuario marcó "Recuérdame", generamos un token persistente,
                // lo guardamos en Sesiones_Activas y lo mandamos como cookie.
                String recordarme = request.getParameter("recordarme");
                if ("on".equals(recordarme)) {
                    String token = UUID.randomUUID().toString();
                    SesionActivaDAO sesionDAO = new SesionActivaDAO();
                    sesionDAO.crear(usuario.getIdUsuario(), token, request.getRemoteAddr(), DIAS_RECORDARME);

                    Cookie cookie = new Cookie(COOKIE_RECORDARME, token);
                    cookie.setMaxAge(DIAS_RECORDARME * 24 * 60 * 60); // en segundos
                    cookie.setPath(request.getContextPath() + "/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }

                // Redirigir según el rol
                String rol = usuario.getRol();
                String dashboard;

                switch (rol) {
                    case "Dueno":
                        dashboard = "/index_cliente.jsp";
                        break;
                    case "Empleado":
                        dashboard = "/index_cliente.jsp";
                        break;
                    case "Cliente":
                        dashboard = "/index_cliente.jsp";
                        break;
                    default:
                        dashboard = "/index_cliente.jsp";
                        break;
                }

                // Redirigir siempre a la pantalla de inicio (misma vista para los 3 roles)
                response.sendRedirect(request.getContextPath() + "/index_cliente.jsp");

            } else {
                // Credenciales incorrectas
                request.setAttribute("error", "Credenciales incorrectas. Inténtalo de nuevo.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Intenta de nuevo más tarde.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}