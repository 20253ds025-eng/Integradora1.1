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
import jakarta.servlet.http.HttpSession;

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
        } else if ("recuperarContrasena".equals(action)) {
            recuperarContrasena(request, response);
        } else if ("verificarCodigo".equals(action)) {
            verificarCodigo(request, response);
        } else if ("actualizarContrasena".equals(action)) {
            actualizarContrasena(request, response);
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
    // ==========================================
    // RECUPERAR CONTRASEÑA - PASO 1: ENVIAR CÓDIGO
    // ==========================================
    private void recuperarContrasena(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        UsuarioDTO usuario = usuarioDAO.getByCorreo(correo);

        // No revelamos si el correo existe o no, por seguridad
        if (usuario == null) {
            request.setAttribute("success",
                    "Si el correo está registrado, recibirás un código de verificación.");
            request.getRequestDispatcher("/recuperarContra.jsp").forward(request, response);
            return;
        }

        String codigo = MySmart.generarCodigoVerificacion();

        if (usuarioDAO.guardarCodigoRecuperacion(correo, codigo)) {
            EmailSender.enviarCodigoRecuperacion(correo, usuario.getNombre(), codigo);

            HttpSession session = request.getSession();
            session.setAttribute("correoRecuperacion", correo);

            request.setAttribute("success", "Te enviamos un código de verificación a tu correo.");
            request.getRequestDispatcher("/verificarCodigo.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Ocurrió un error. Intenta nuevamente.");
            request.getRequestDispatcher("/recuperarContra.jsp").forward(request, response);
        }
    }

    // ==========================================
    // RECUPERAR CONTRASEÑA - PASO 2: VERIFICAR CÓDIGO
    // ==========================================
    private void verificarCodigo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String correo = (String) session.getAttribute("correoRecuperacion");
        String codigo = request.getParameter("codigo");

        if (correo == null) {
            request.setAttribute("error", "Tu sesión expiró. Solicita el código nuevamente.");
            request.getRequestDispatcher("/recuperarContra.jsp").forward(request, response);
            return;
        }

        UsuarioDTO usuario = usuarioDAO.validarCodigoRecuperacion(correo, codigo);

        if (usuario != null) {
            session.setAttribute("idUsuarioRecuperacion", usuario.getIdUsuario());
            request.setAttribute("success", "Código verificado correctamente.");
            request.getRequestDispatcher("/nuevaContrasena.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Código inválido o expirado.");
            request.getRequestDispatcher("/verificarCodigo.jsp").forward(request, response);
        }
    }

    // ==========================================
    // RECUPERAR CONTRASEÑA - PASO 3: ACTUALIZAR CONTRASEÑA
    // ==========================================
    private void actualizarContrasena(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuarioRecuperacion");

        if (idUsuario == null) {
            request.setAttribute("error", "Tu sesión expiró. Inicia el proceso nuevamente.");
            request.getRequestDispatcher("/recuperarContra.jsp").forward(request, response);
            return;
        }

        String password = request.getParameter("password");
        String confirmar = request.getParameter("confirmar");

        if (password == null || !password.equals(confirmar)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/nuevaContrasena.jsp").forward(request, response);
            return;
        }

        if (usuarioDAO.cambiarContrasena(idUsuario, password)) {
            usuarioDAO.limpiarCodigoRecuperacion(idUsuario);
            session.removeAttribute("correoRecuperacion");
            session.removeAttribute("idUsuarioRecuperacion");

            request.setAttribute("success", "Contraseña actualizada. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Ocurrió un error al actualizar la contraseña.");
            request.getRequestDispatcher("/nuevaContrasena.jsp").forward(request, response);
        }
    }
}