package mx.edu.utez.demo.controller;

import mx.edu.utez.demo.model.UsuarioDTO;
import mx.edu.utez.demo.model.ClienteDTO;
import mx.edu.utez.demo.model.dao.UsuarioDAO;
import mx.edu.utez.demo.model.dao.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        if (usuarioDAO.existeCorreo(correo)) {
            request.setAttribute("error", "El correo ya está registrado.");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
            return;
        }

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(nombre + " " + apellidoPaterno);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setRol("Cliente");

        boolean registroExitoso = false;

        // 1. Guardar en la tabla Usuarios
        if (usuarioDAO.create(usuario) && usuario.getIdUsuario() > 0) {

            // 2. Guardar en la tabla Clientes
            ClienteDTO cliente = new ClienteDTO();
            cliente.setIdCliente(usuario.getIdUsuario());
            // Al no hacer set de idAsesor, Java lo deja en 0 por defecto.
            // Tu ClienteDAO ahora detectará ese 0 y lo insertará como NULL en la base de datos.

            registroExitoso = clienteDAO.create(cliente);
        }

        if (registroExitoso) {
            request.setAttribute("success", "Cuenta creada exitosamente. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al crear la cuenta. Intenta de nuevo.");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
        }
    }
}