<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Lógica para detectar si hay sesión y obtener nombre del usuario
  Object objUsuario = session.getAttribute("usuario");
  Object objNombre = session.getAttribute("nombre");
  boolean haySesion = (objUsuario != null || objNombre != null);

  String nombreUsuario = (objNombre != null) ? objNombre.toString() : "";
  String iniciales = "U";
  if (!nombreUsuario.trim().isEmpty()) {
    String[] partes = nombreUsuario.trim().split("\\s+");
    if (partes.length >= 2) {
      iniciales = ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
    } else if (partes.length == 1 && partes[0].length() > 0) {
      iniciales = ("" + partes[0].charAt(0)).toUpperCase();
    }
  }

  // Rol del usuario en sesión (Cliente / Empleado / Dueno)
  Object objRol = session.getAttribute("rol");
  String rol = (objRol != null) ? objRol.toString() : "";

  // Rutas según estado de sesión
  String rutaInicio = haySesion ? "/index_cliente.jsp" : "/index.jsp";
  String rutaVehiculos = haySesion ? "/mis_vehiculos.jsp" : "/catalCOCHES_pub.jsp";
  String rutaServicios = haySesion ? "/Cliente_Catalogo_Serv.jsp" : "/catalSERVICIOS_pub.jsp";
  String rutaCarrito = "/carrito.jsp";
  String rutaMisCompras = "/mis_compras.jsp";
%>

<!-- COMPONENTE MENU LATERAL (OFFCANVAS) -->
<div class="offcanvas offcanvas-start border-end" tabindex="-1" id="menuLateral" aria-labelledby="menuLateralLabel" style="width: 290px; background-color: #ffffff;">

  <!-- ENCABEZADO: Logo C / CLICK & DRIVE y botón hamburguesa para cerrar -->
  <div class="offcanvas-header d-flex flex-column align-items-center pt-4 pb-2 position-relative border-0">
    <button type="button" class="btn p-0 border-0 text-dark position-absolute top-0 end-0 m-3" data-bs-dismiss="offcanvas" aria-label="Cerrar">
      <i class="bi bi-list fs-3" style="line-height: 1;"></i>
    </button>

    <div class="text-center my-2">
      <span class="logo-c d-block fw-bold" style="font-family: 'Playfair Display', Georgia, serif; font-size: 3.2rem !important; line-height: 0.8; color: #1a1a1a;">C</span>
      <span class="fw-bold d-block text-uppercase mt-2" style="font-family: system-ui, -apple-system, sans-serif; font-size: 0.68rem; letter-spacing: 2px; color: #1a1a1a;">CLICK & DRIVE</span>
    </div>
  </div>

  <!-- CUERPO DEL MENÚ -->
  <div class="offcanvas-body px-4 pt-2">

    <% if (haySesion) { %>
    <!-- SI HAY SESIÓN: Tarjeta de Usuario con iniciales e información -->
    <div class="d-flex align-items-center gap-3 py-3 mb-4 border-bottom pb-3">
      <div class="user-avatar-circle flex-shrink-0 d-flex align-items-center justify-content-center rounded-circle text-dark fw-bold"
           style="width: 58px; height: 58px; background-color: #dcdcdc; font-size: 1.25rem; font-family: system-ui, -apple-system, sans-serif;">
        <%= iniciales %>
      </div>
      <div class="user-info-text overflow-hidden">
        <h6 class="mb-0 fw-semibold text-dark" style="font-family: 'Playfair Display', Georgia, serif; font-size: 1.05rem; line-height: 1.25;">
          <%= nombreUsuario %>
        </h6>
        <% if (!rol.isEmpty()) { %>
        <span class="font-sans text-muted" style="font-size: 0.78rem;"><%= rol %></span>
        <% } %>
      </div>
    </div>

    <!-- Navegación Sesión Iniciada -->
    <nav class="nav flex-column gap-3">

      <!-- Inicio (común a los 3 roles) -->
      <a href="${pageContext.request.contextPath}<%= rutaInicio %>" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-house fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Inicio</span>
      </a>

      <% if ("Cliente".equals(rol)) { %>

      <!-- Carrito -->
      <a href="${pageContext.request.contextPath}<%= rutaCarrito %>" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-cart fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Carrito</span>
      </a>

      <!-- Mis compras -->
      <a href="${pageContext.request.contextPath}<%= rutaMisCompras %>" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-cart-check fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Mis compras</span>
      </a>

      <!-- Mis vehículos -->
      <a href="${pageContext.request.contextPath}<%= rutaVehiculos %>" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-car-front fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Mis vehiculos</span>
      </a>

      <% } else if ("Empleado".equals(rol)) { %>

      <!-- Ventas -->
      <a href="${pageContext.request.contextPath}/VentaServlet" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-tag fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Ventas</span>
      </a>

      <!-- Registrar cliente -->
      <a href="${pageContext.request.contextPath}/RegistroServlet" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-person-plus fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Registrar cliente</span>
      </a>

      <!-- Vehículos -->
      <a href="${pageContext.request.contextPath}/AutoServlet" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-car-front fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Vehiculos</span>
      </a>

      <% } else if ("Dueno".equals(rol)) { %>

      <!-- Asesores (empleados) -->
      <a href="${pageContext.request.contextPath}/UsuarioServlet?action=listar&rol=Empleado" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-person fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Asesores</span>
      </a>

      <!-- Clientes -->
      <a href="${pageContext.request.contextPath}/UsuarioServlet?action=listar&rol=Cliente" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-person fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Clientes</span>
      </a>

      <!-- Ventas -->
      <a href="${pageContext.request.contextPath}/VentaServlet" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-tag fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Ventas</span>
      </a>

      <!-- Vehículos -->
      <a href="${pageContext.request.contextPath}/AutoServlet" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-car-front fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Vehiculos</span>
      </a>

      <% } %>

    </nav>
    <% } else { %>
    <!-- SI NO HAY SESIÓN: Navegación sencilla sin datos de usuario -->
    <nav class="nav flex-column gap-3 pt-3">

      <!-- Inicio -->
      <a href="${pageContext.request.contextPath}/index.jsp" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-house fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Inicio</span>
      </a>

      <!-- Vehículos -->
      <a href="${pageContext.request.contextPath}/catalCOCHES_pub.jsp" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-car-front fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Vehículos</span>
      </a>

      <!-- Servicios -->
      <a href="${pageContext.request.contextPath}/catalSERVICIOS_pub.jsp" class="sidebar-nav-link d-flex align-items-center gap-3 py-2 text-decoration-none text-dark">
        <i class="bi bi-tools fs-5" style="width: 24px; text-align: center;"></i>
        <span style="font-size: 0.95rem;">Servicios</span>
      </a>

    </nav>
    <% } %>

  </div>
</div>
