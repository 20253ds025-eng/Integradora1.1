<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Lógica para detectar si hay sesión y asignar las rutas correctas
  boolean haySesion = (session.getAttribute("usuario") != null);

  // Si hay sesión lo manda a sus vistas, si no, a las públicas
  String rutaInicio = haySesion ? "/index_cliente.jsp" : "/index.jsp";
  String rutaVehiculos = haySesion ? "/Cliente_Catalogo_Coches.jsp" : "/catalCOCHES_pub.jsp"; // Ajusta el nombre si le pusiste distinto a coches
  String rutaServicios = haySesion ? "/Cliente_Catalogo_Serv.jsp" : "/catalSERVICIOS_pub.jsp";
%>

<!-- COMPONENTE MENU LATERAL (OFFCANVAS) -->
<div class="offcanvas offcanvas-start" tabindex="-1" id="menuLateral" aria-labelledby="menuLateralLabel" style="width: 280px;">

  <!-- Encabezado con Logo C / CLICK & DRIVE y botón de cerrar -->
  <div class="offcanvas-header d-flex flex-column align-items-center pt-4 pb-2 position-relative">
    <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="offcanvas" aria-controls="menuLateral"></button>

    <div class="text-center my-2">
      <span class="logo-c d-block fs-1 fw-bold leading-none" style="font-size: 3rem !important; line-height: 1;">C</span>
      <span class="fw-bold d-block text-uppercase mt-1" style="font-size: 0.7rem; letter-spacing: 2px; font-family: system-ui;">CLICK & DRIVE</span>
    </div>
  </div>

  <!-- Cuerpo del Menú con íconos alineados -->
  <div class="offcanvas-body px-3 pt-4">
    <nav class="nav flex-column gap-2">

      <!-- Inicio dinámico -->
      <a href="${pageContext.request.contextPath}<%= rutaInicio %>" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-house fs-5"></i>
        <span>Inicio</span>
      </a>

      <!-- Vehículos dinámico -->
      <a href="${pageContext.request.contextPath}<%= rutaVehiculos %>" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-car-front fs-5"></i>
        <span>Vehículos</span>
      </a>

      <!-- Servicios dinámico -->
      <a href="${pageContext.request.contextPath}<%= rutaServicios %>" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-tools fs-5"></i>
        <span>Servicios</span>
      </a>

    </nav>
  </div>
</div>