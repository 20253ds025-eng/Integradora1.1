package mx.edu.utez.demo.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
            "/login",
            "/registro.jsp",
            "/registro-dueno.jsp",
            "/RegistroServlet",
            "/index.jsp",
            "/catalogoPub.jsp",
            "/detalleA.jsp",
            "/detalleS.jsp",
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

        if (!autenticado) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Debes iniciar sesión para continuar");
            return;
        }

        chain.doFilter(request, response);
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
