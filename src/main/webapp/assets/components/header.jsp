<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- HEADER IDENTICO A LA IMAGEN ORIGINAL -->
<header class="bg-white sticky-top pt-3 pb-2" style="box-shadow: none;">
    <div class="container-fluid d-flex justify-content-between align-items-center px-4" style="max-width: 1200px;">

        <!-- LADO IZQUIERDO: Menú y Logo con más separación -->
        <div class="d-flex align-items-center gap-4">
            <!-- Botón Hamburguesa más grande -->
            <button class="btn p-0 border-0 text-dark" type="button" data-bs-toggle="offcanvas" data-bs-target="#menuLateral" aria-controls="menuLateral">
                <i class="bi bi-list" style="font-size: 2.5rem; line-height: 1;"></i>
            </button>

            <!-- Logo exacto -->
            <div class="text-center" style="margin-top: -5px;">
                <span class="d-block fw-bold" style="font-family: 'Playfair Display', Georgia, serif; font-size: 3.2rem; line-height: 0.8; color: #1a1a1a;">C</span>
                <span class="d-block fw-bold mt-1" style="font-family: system-ui, -apple-system, sans-serif; font-size: 0.7rem; letter-spacing: 2px; color: #1a1a1a;">CLICK & DRIVE</span>
            </div>
        </div>

        <!-- LADO DERECHO: Verificación de sesión e ícono de usuario exacto -->
        <div class="d-flex align-items-center gap-3">
            <%
                String nombreUsuario = (String) session.getAttribute("nombre");
                if (nombreUsuario != null) {
                    String primerNombre = nombreUsuario.split(" ")[0];
            %>
            <span class="fw-bold font-sans text-dark" style="font-size: 0.95rem;">Hola, <%= primerNombre %></span>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-danger btn-sm font-sans px-3 py-1 rounded-3">Salir</a>
            <% } else { %>
            <a href="${pageContext.request.contextPath}/login.jsp" class="text-dark text-decoration-none">
                <i class="bi bi-person" style="font-size: 2.2rem;"></i>
            </a>
            <% } %>
        </div>

    </div>
</header>