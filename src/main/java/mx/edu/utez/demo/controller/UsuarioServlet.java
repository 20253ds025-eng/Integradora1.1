package mx.edu.utez.demo.controller;

import mx.edu.utez.demo.model.dao.UsuarioDAO;
import mx.edu.utez.demo.model.UsuarioDTO;
import mx.edu.utez.demo.utils.EmailSender;
import mx.edu.utez.demo.utils.MySmart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    // ==========================================
    // DO GET
    // ==========================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("registrarEmpleado".equals(action)) {
            request.getRequestDispatcher("/usuarios/registrar_empleado.jsp").forward(request, response);
        } else {
            // Listar usuarios
            request.setAttribute("usuarios", usuarioDAO.getAll());
            request.getRequestDispatcher("/usuarios/listar.jsp").forward(request, response);
        }
    }

    // ==========================================
    // DO POST
    // ==========================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("registrarEmpleado".equals(action)) {
            registrarEmpleado(request, response);
        } else if ("registrarCliente".equals(action)) {
            registrarCliente(request, response);
        } else if ("cambiarContrasena".equals(action)) {
            cambiarContrasena(request, response);
        } else {
            response.sendRedirect("UsuarioServlet");
        }
    }

    // ==========================================
    // REGISTRAR EMPLEADO
    // ==========================================
    private void registrarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");

        // Verificar si el correo ya existe
        if (usuarioDAO.existeCorreo(correo)) {
            request.setAttribute("error", "El correo ya está registrado.");
            request.getRequestDispatcher("/usuarios/registrar_empleado.jsp").forward(request, response);
            return;
        }

        // Generar contraseña temporal
        String contrasenaTemporal = MySmart.generarContrasenaTemporal();

        // Crear usuario
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasenaTemporal);
        usuario.setRol("Empleado");

        if (usuarioDAO.create(usuario)) {
            // Enviar credenciales por correo (Línea 75 corregida)
            EmailSender.enviarCredenciales(correo, nombre, contrasenaTemporal);
            request.setAttribute("success", "Empleado registrado exitosamente. Se enviaron las credenciales al correo.");
            request.getRequestDispatcher("/usuarios/registrar_empleado.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al registrar el empleado.");
            request.getRequestDispatcher("/usuarios/registrar_empleado.jsp").forward(request, response);
        }
    }

    // ==========================================
    // REGISTRAR CLIENTE
    // ==========================================
    private void registrarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        int idAsesor = Integer.parseInt(request.getParameter("idAsesor"));

        // Verificar si el correo ya existe (Línea 89 corregida)
        if (usuarioDAO.existeCorreo(correo)) {
            request.setAttribute("error", "El correo ya está registrado.");
            request.getRequestDispatcher("/usuarios/registrar_cliente.jsp").forward(request, response);
            return;
        }

        String contrasenaTemporal = MySmart.generarContrasenaTemporal();

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasenaTemporal);
        usuario.setRol("Cliente");

        if (usuarioDAO.create(usuario)) {
            // Registrar cliente (se necesita ClienteDAO)
            // Enviar credenciales por correo (Línea 111 corregida)
            EmailSender.enviarCredenciales(correo, nombre, contrasenaTemporal);
            request.setAttribute("success", "Cliente registrado exitosamente. Se enviaron las credenciales al correo.");
            request.getRequestDispatcher("/usuarios/registrar_cliente.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al registrar el cliente.");
            request.getRequestDispatcher("/usuarios/registrar_cliente.jsp").forward(request, response);
        }
    }

    // ==========================================
    // CAMBIAR CONTRASEÑA
    // ==========================================
    private void cambiarContrasena(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        String nuevaContrasena = request.getParameter("nuevaContrasena");

        if (usuarioDAO.cambiarContrasena(idUsuario, nuevaContrasena)) {
            // Invalidar sesión actual
            request.getSession().invalidate();
            request.setAttribute("success", "Contraseña cambiada exitosamente. Vuelve a iniciar sesión.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al cambiar la contraseña.");
            request.getRequestDispatcher("/perfil.jsp").forward(request, response);
        }
    }
}