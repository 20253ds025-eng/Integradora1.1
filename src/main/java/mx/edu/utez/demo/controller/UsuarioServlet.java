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
import java.util.List;
import java.util.ArrayList;

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
        } else if ("editarUsuario".equals(action)) {
            editarUsuarioForm(request, response);
        } else if ("listar".equals(action)) {
            listarPorRol(request, response);
        } else {
            request.setAttribute("usuarios", usuarioDAO.getAll());
            request.getRequestDispatcher("/usuarios/listar.jsp").forward(request, response);
        }
    }

    // ==========================================
    // LISTAR USUARIOS POR ROL (con búsqueda y paginación)
    // ==========================================
    private void listarPorRol(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rol = request.getParameter("rol"); // "Empleado" o "Cliente"
        String buscar = request.getParameter("buscar");
        int pagina = 1;
        try {
            if (request.getParameter("pagina") != null) {
                pagina = Integer.parseInt(request.getParameter("pagina"));
            }
        } catch (NumberFormatException e) {
            pagina = 1;
        }

        List<UsuarioDTO> lista = usuarioDAO.getByRol(rol);

        // Filtro de búsqueda por nombre o correo (case-insensitive)
        if (buscar != null && !buscar.trim().isEmpty()) {
            String filtro = buscar.trim().toLowerCase();
            List<UsuarioDTO> filtrada = new ArrayList<>();
            for (UsuarioDTO u : lista) {
                if (u.getNombre().toLowerCase().contains(filtro)
                        || u.getCorreo().toLowerCase().contains(filtro)) {
                    filtrada.add(u);
                }
            }
            lista = filtrada;
        }

        int totalRegistros = lista.size();
        int porPagina = 10;
        int totalPaginas = (int) Math.ceil((double) totalRegistros / porPagina);
        if (totalPaginas == 0) totalPaginas = 1;
        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;

        int inicio = (pagina - 1) * porPagina;
        int fin = Math.min(inicio + porPagina, totalRegistros);
        List<UsuarioDTO> listaPagina = (inicio < fin) ? lista.subList(inicio, fin) : new ArrayList<>();

        request.setAttribute("usuarios", listaPagina);
        request.setAttribute("rol", rol);
        request.setAttribute("buscar", buscar);
        request.setAttribute("paginaActual", pagina);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("totalRegistros", totalRegistros);

        request.getRequestDispatcher("/listar_rol.jsp").forward(request, response);
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
<<<<<<< HEAD
        } else if ("actualizarPerfil".equals(action)) {
            actualizarPerfil(request, response);
=======
        } else if ("eliminarUsuario".equals(action)) {
            eliminarUsuario(request, response);
        } else if ("reactivarUsuario".equals(action)) {
            reactivarUsuario(request, response);
        } else if ("actualizarUsuario".equals(action)) {
            actualizarUsuario(request, response);
>>>>>>> 0f2f5ddebf34866252cc4ac63a18ea36397afd4b
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
<<<<<<< HEAD
}
=======
        // ==========================================
        // MOSTRAR FORMULARIO DE EDICIÓN
        // ==========================================
        private void editarUsuarioForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            UsuarioDTO usuario = usuarioDAO.getById(idUsuario);

            if (usuario == null) {
                request.setAttribute("error", "El usuario no existe.");
                response.sendRedirect(request.getContextPath() + "/index_cliente.jsp");
                return;
            }

            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/usuarios/editar_usuario.jsp").forward(request, response);
        }

        // ==========================================
        // GUARDAR EDICIÓN
        // ==========================================
        private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String rol = request.getParameter("rol");

            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setIdUsuario(idUsuario);
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);

            if (usuarioDAO.update(usuario)) {
                response.sendRedirect(request.getContextPath()
                        + "/UsuarioServlet?action=listar&rol=" + rol
                        + "&success=Usuario actualizado correctamente.");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/UsuarioServlet?action=editarUsuario&idUsuario=" + idUsuario
                        + "&error=No se pudo actualizar el usuario.");
            }
        }

        // ==========================================
        // ELIMINAR (DESACTIVAR) USUARIO
        // ==========================================
        private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String rol = request.getParameter("rol");

            usuarioDAO.delete(idUsuario);
            response.sendRedirect(request.getContextPath() + "/UsuarioServlet?action=listar&rol=" + rol);
        }

        // ==========================================
        // REACTIVAR USUARIO
        // ==========================================
        private void reactivarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String rol = request.getParameter("rol");

            usuarioDAO.reactivar(idUsuario);
            response.sendRedirect(request.getContextPath() + "/UsuarioServlet?action=listar&rol=" + rol);
        }
}
>>>>>>> 0f2f5ddebf34866252cc4ac63a18ea36397afd4b
