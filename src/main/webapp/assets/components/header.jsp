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
            %>
            <div class="dropdown">
                <button type="button" class="btn p-0 border-0 bg-transparent text-dark d-flex align-items-center" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-person" style="font-size: 2.2rem;"></i>
                </button>
                <ul class="dropdown-menu dropdown-menu-end shadow border-0 mt-2 p-0" aria-labelledby="userDropdown" style="min-width: 170px; border-radius: 4px;">
                    <% if (nombreUsuario != null) { %>
                    <li>
                        <a class="dropdown-item py-3 font-serif d-flex justify-content-between align-items-center" href="${pageContext.request.contextPath}/perfil.jsp" style="font-size: 1.1rem; color: #1a4f76;">
                            <span>Ver perfil</span> <i class="bi bi-eye text-secondary fs-5"></i>
                        </a>
                    </li>
                    <li><hr class="dropdown-divider m-0"></li>
                    <li>
                        <a class="dropdown-item py-3 text-danger font-serif d-flex justify-content-between align-items-center" href="${pageContext.request.contextPath}/logout" style="font-size: 1.1rem;">
                            <span>Cerrar sesión</span> <i class="bi bi-box-arrow-right fs-5"></i>
                        </a>
                    </li>
                    <% } else { %>
                    <li>
                        <a class="dropdown-item py-3 font-serif d-flex justify-content-between align-items-center" href="${pageContext.request.contextPath}/login.jsp" style="font-size: 1.1rem; color: #1a4f76;">
                            <span>Iniciar sesión</span> <i class="bi bi-box-arrow-in-right text-secondary fs-5"></i>
                        </a>
                    </li>
                    <% } %>
                </ul>
            </div>
        </div>

    </div>
</header>