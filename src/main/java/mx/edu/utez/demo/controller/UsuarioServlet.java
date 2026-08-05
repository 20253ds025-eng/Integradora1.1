package mx.edu.utez.demo.controller;

import mx.edu.utez.demo.model.dao.UsuarioDAO;
import mx.edu.utez.demo.model.dao.ClienteDAO;
import mx.edu.utez.demo.model.UsuarioDTO;
import mx.edu.utez.demo.model.ClienteDTO;
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
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        clienteDAO = new ClienteDAO();
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
        } else if ("actualizarPerfil".equals(action)) {
            actualizarPerfil(request, response);
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

        if (usuarioDAO.existeCorreo(correo)) {
            request.setAttribute("error", "El correo ya está registrado.");
            request.getRequestDispatcher("/usuarios/registrar_empleado.jsp").forward(request, response);
            return;
        }

        String contrasenaTemporal = MySmart.generarContrasenaTemporal();

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasenaTemporal);
        usuario.setRol("Empleado");

        if (usuarioDAO.create(usuario)) {
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

        if (usuarioDAO.create(usuario) && usuario.getIdUsuario() > 0) {
            ClienteDTO cliente = new ClienteDTO();
            cliente.setIdCliente(usuario.getIdUsuario());
            if (idAsesor > 0) {
                cliente.setIdAsesor(idAsesor);
            }
            clienteDAO.create(cliente);

            EmailSender.enviarCredenciales(correo, nombre, contrasenaTemporal);
            request.setAttribute("success", "Cliente registrado exitosamente. Se enviaron las credenciales al correo.");
            request.getRequestDispatcher("/usuarios/registrar_cliente.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al registrar el cliente.");
            request.getRequestDispatcher("/usuarios/registrar_cliente.jsp").forward(request, response);
        }
    }

    // ==========================================
    // ACTUALIZAR PERFIL
    // ==========================================
    private void actualizarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idUsuario = (int) session.getAttribute("usuario");
        String nombre = request.getParameter("nombre");
        String apellidoP = request.getParameter("apellidoP");
        String apellidoM = request.getParameter("apellidoM");
        String correo = request.getParameter("correo");

        String nombreCompleto = (nombre != null ? nombre : "") + " " +
                (apellidoP != null ? apellidoP : "") +
                (apellidoM != null && !apellidoM.isEmpty() ? " " + apellidoM : "");

        if (correo != null && !correo.isEmpty()) {
            UsuarioDTO existente = usuarioDAO.getByCorreo(correo);
            if (existente != null && existente.getIdUsuario() != idUsuario) {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"El correo ya esta en uso\"}");
                return;
            }
        }

        UsuarioDTO usuario = usuarioDAO.getById(idUsuario);
        if (usuario != null) {
            usuario.setNombre(nombreCompleto.trim());
            if (correo != null && !correo.isEmpty()) {
                usuario.setCorreo(correo);
            }
            if (usuarioDAO.update(usuario)) {
                session.setAttribute("nombre", nombreCompleto.trim());
                if (correo != null && !correo.isEmpty()) {
                    session.setAttribute("correo", correo);
                }
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":true}");
            } else {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Error al actualizar el perfil\"}");
            }
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
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
