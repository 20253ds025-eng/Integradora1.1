package mx.edu.utez.demo.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.demo.model.UsuarioDTO;
import mx.edu.utez.demo.model.dao.SesionActivaDAO;
import mx.edu.utez.demo.model.dao.UsuarioDAO;

import java.io.IOException;

/**
 * Filtro de autenticación global (req. 1.1 / RNF de confiabilidad y seguridad).
 * Bloquea el acceso a cualquier recurso protegido si no hay una sesión activa.
 * La autorización fina por ROL (Dueño / Empleado / Cliente) se valida además
 * dentro de cada servlet, ya que cada acción tiene reglas distintas.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {


    // Recursos accesibles sin haber iniciado sesión.
    private static final String[] PUBLICOS = {
            "/login.jsp",
            "/LoginServlet",
            "/registro.jsp",
            "/registro-dueno.jsp",
            "/RegistroServlet",
            "/recuperarContra.jsp",
            "/verificarCodigo.jsp",
            "/nuevaContrasena.jsp",
            "/UsuarioServlet",
            "/index.jsp",
            "/catalCOCHES_pub.jsp",
            "/catalSERVICIOS_pub.jsp",
            "/CatalogoCliente",
            "/CatalogoServiciosCliente",
            "/DetalleAutoServlet",
            "/DetalleServicioServlet",
            "/Cliente_Catalogo_Coches.jsp",
            "/Cliente_Catalogo_Serv.jsp",
            "/Cliente_Detalle_Coche.jsp",
            "/Cliente_Detalle_Servicio.jsp",
            "/alertaAunPaso_1.jsp",
            "/alertaAunPaso_2.jsp",
            "/detalleauto.jsp",
            "/detalleServicio.jsp",
            "/Advertecialogin.jsp",
            "/assets/"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.isEmpty()) path = "/";

        if (esPublico(path, req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean autenticado = session != null && session.getAttribute("usuario") != null;

        // Si no hay sesión activa, intentamos reconstruirla con la cookie "recuérdame".
        if (!autenticado) {
            autenticado = intentarLoginPorCookie(req);
        }

        if (!autenticado) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Debes iniciar sesión para continuar");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Busca la cookie "remember_token", valida el token contra Sesiones_Activas
     * y, si es válido, reconstruye la sesión con los datos del usuario.
     * Devuelve true si logró autenticar al usuario por esta vía.
     */
    private boolean intentarLoginPorCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return false;

        String token = null;
        for (Cookie c : cookies) {
            if ("remember_token".equals(c.getName())) {
                token = c.getValue();
                break;
            }
        }
        if (token == null || token.isEmpty()) return false;

        SesionActivaDAO sesionDAO = new SesionActivaDAO();
        Integer idUsuario = sesionDAO.validarToken(token);
        if (idUsuario == null) return false;

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioDTO usuario = usuarioDAO.getById(idUsuario);
        if (usuario == null || !usuario.isActivo()) return false;

        // Token válido: reconstruimos la sesión igual que hace el LoginServlet.
        HttpSession session = req.getSession(true);
        session.setAttribute("usuario", usuario.getIdUsuario());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("correo", usuario.getCorreo());
        session.setAttribute("rol", usuario.getRol());

        return true;
    }

    private boolean esPublico(String path, String metodo) {
        if (path.equals("/") || path.equals("")) return true;
        for (String publico : PUBLICOS) {
            if (path.equals(publico) || path.startsWith(publico)) {
                return true;
            }
        }
        // El catálogo de autos y de servicios es de consulta pública (req. 2.2),
        // solo las lecturas (GET) son públicas; las acciones de escritura (POST)
        // requieren sesión, y además cada servlet valida el rol correspondiente.
        if ("GET".equalsIgnoreCase(metodo) &&
                (path.equals("/AutoServlet") || path.equals("/ServicioServlet"))) {
            return true;
        }
        return false;
    }
}
