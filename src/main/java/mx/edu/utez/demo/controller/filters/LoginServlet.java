package mx.edu.utez.demo.controller.filters;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.demo.model.dao.UsuarioDAO;  // ← CORREGIDO: UsuarioDAO (con mayúscula)
import mx.edu.utez.demo.model.UsuarioDTO;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;  // ← CORREGIDO: UsuarioDAO

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();  // ← CORREGIDO: UsuarioDAO
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String correo = request.getParameter("correo");    // ← CORREGIDO: "correo"
        String contrasena = request.getParameter("contrasena");  // ← CORREGIDO: "contrasena"

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
                Integer idUsuario = (Integer) session.getAttribute("usuario");
                session.setAttribute("nombre", usuario.getNombre());
                session.setAttribute("correo", usuario.getCorreo());
                session.setAttribute("rol", usuario.getRol());

                // Redirigir según el rol
                String rol = usuario.getRol();
                String dashboard;

                switch (rol) {
                    case "Dueno":
                        dashboard = "/dashboard/dueno.jsp";
                        break;
                    case "Empleado":
                        dashboard = "/dashboard/empleado.jsp";
                        break;
                    case "Cliente":
                        dashboard = "/dashboard/cliente.jsp";
                        break;
                    default:
                        dashboard = "/dashboard/cliente.jsp";
                        break;
                }

                response.sendRedirect(request.getContextPath() + dashboard);

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