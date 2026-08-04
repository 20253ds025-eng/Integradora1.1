<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Click & Drive</title>

  <!-- 1. CSS de Bootstrap local -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">

  <!-- 2. Bootstrap Icons oficial -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

  <!-- 3. Fuente Playfair Display -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">

  <style>
    body {
      font-family: 'Playfair Display', Georgia, serif;
      background-color: #ffffff;
      color: #1a1a1a;
    }

    .logo-c {
      font-size: 2rem;
      font-weight: 700;
      line-height: 0.8;
      font-family: 'Playfair Display', Georgia, serif;
    }

    .font-sans {
      font-family: system-ui, -apple-system, sans-serif;
    }

    .nav-menu-link {
      font-family: system-ui, -apple-system, sans-serif;
      font-weight: 500;
      color: #333333 !important;
      transition: all 0.2s ease-in-out;
    }

    .nav-menu-link:hover {
      background-color: #001E50 !important;
      color: #ffffff !important;
    }

    .nav-menu-link:hover i {
      color: #ffffff !important;
    }

    .detalle-img-card {
      border: 1px solid #e0e0e0;
      border-radius: 10px;
      padding: 10px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
      height: 100%;
    }

    .detalle-img-card img {
      border-radius: 6px;
      width: 100%;
      height: 400px;
      object-fit: cover;
    }

    .detalle-info-card {
      border: 1px solid #e0e0e0;
      border-radius: 10px;
      padding: 2rem 2.2rem;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
      height: 100%;
    }

    .detalle-titulo {
      font-size: 2rem;
      font-weight: 700;
    }

    .detalle-precio {
      font-size: 1.8rem;
      font-weight: 600;
    }

    .detalle-incluye-titulo {
      font-size: 1.1rem;
      font-weight: 700;
    }

    .detalle-lista {
      font-size: 1rem;
      line-height: 1.7;
      padding-left: 1.2rem;
    }

    .btn-navy {
      background-color: #001E50;
      color: #ffffff;
      border: none;
    }

    .btn-navy:hover {
      background-color: #00133a;
      color: #ffffff;
    }
  </style>
</head>



<%--
    ==========================================================
    LOGICA DEL SERVICIO
    ==========================================================
    Se recibe el id por parámetro: detalleServicio.jsp?id=afinacion
    Solo maneja los 4 servicios destacados del index:
    lavado, bujias, rotacion, afinacion.

    Para agregar otro servicio nuevo:
    1. Copia uno de los bloques "servicios.put(...)" de abajo.
    2. Cambia la clave (ej. "pulido") por el id que quieras usar en el link.
    3. Cambia titulo, precio, imagen y la lista de incluye.
--%>
<%
  Map<String, Object[]> servicios = new HashMap<>();

  servicios.put("lavado", new Object[]{
          "Lavado premium",
          "$1,200 MXN",
          "lavado.png",
          new String[]{
                  "Lavado exterior con espuma activa.",
                  "Aspirado completo de interiores.",
                  "Limpieza de tapetes.",
                  "Aromatizante incluido."
          }
  });

  servicios.put("bujias", new Object[]{
          "Cambio de bujías",
          "$1,500 MXN",
          "bujiaas.png",
          new String[]{
                  "Revisión del sistema de encendido.",
                  "Cambio de bujías originales o equivalentes.",
                  "Prueba de encendido y funcionamiento."
          }
  });

  servicios.put("rotacion", new Object[]{
          "Rotación de llantas",
          "$600 MXN",
          "rotar-las-llantas.jpg",
          new String[]{
                  "Rotación de las 4 llantas.",
                  "Revisión de presión de aire.",
                  "Inspección visual de desgaste."
          }
  });

  servicios.put("afinacion", new Object[]{
          "Afinación mayor",
          "$800 MXN",
          "afinacion.png",
          new String[]{
                  "Cambio de aceite.",
                  "Reemplazo de filtros.",
                  "Cambio de bujías.",
                  "Revisión de frenos.",
                  "Diagnóstico general del motor."
          }
  });

  // Se obtiene el id de la URL, ej: detalleServicio.jsp?id=afinacion
  String id = request.getParameter("id");
  Object[] datos = servicios.get(id);

  // Si no se encuentra el servicio, se muestra uno por defecto para evitar errores
  if (datos == null) {
    datos = new Object[]{"Servicio no encontrado", "", "afinacion.png", new String[]{}};
  }

  String titulo = (String) datos[0];
  String precio = (String) datos[1];
  String imagen = (String) datos[2];
  String[] incluye = (String[]) datos[3];
%>



<body>
<!-- HEADER CON BOTÓN HAMBURGUESA + LOGO JUNTO -->
<header class="border-bottom py-3 sticky-top bg-white">
  <div class="container-fluid d-flex justify-content-between align-items-center px-4" style="max-width: 1200px;">

    <div class="d-flex align-items-center gap-3">
      <button class="btn p-0 border-0 fs-2 text-dark" type="button" data-bs-toggle="offcanvas" data-bs-target="#menuLateral" aria-controls="menuLateral">
        <i class="bi bi-list"></i>
      </button>

      <div class="text-center my-2">
        <span class="logo-c d-block fs-1 fw-bold leading-none" style="font-size: 3rem !important; line-height: 1;">C</span>
        <span class="fw-bold d-block text-uppercase mt-1" style="font-size: 0.7rem; letter-spacing: 2px; font-family: system-ui;">CLICK & DRIVE</span>
      </div>
    </div>

    <a href="${pageContext.request.contextPath}/login.jsp" class="text-dark fs-3 text-decoration-none">
      <i class="bi bi-person"></i>
    </a>

  </div>
</header>

<div class="offcanvas offcanvas-start" tabindex="-1" id="menuLateral" aria-labelledby="menuLateralLabel" style="width: 280px;">
  <div class="offcanvas-header d-flex flex-column align-items-center pt-4 pb-2 position-relative">
    <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="offcanvas" aria-controls="menuLateral"></button>
    <div class="text-center my-2">
      <span class="logo-c d-block fs-1 fw-bold leading-none" style="font-size: 3rem !important; line-height: 1;">C</span>
      <span class="fw-bold d-block text-uppercase mt-1" style="font-size: 0.7rem; letter-spacing: 2px; font-family: system-ui;">CLICK & DRIVE</span>
    </div>
  </div>
  <div class="offcanvas-body px-3 pt-4">
    <nav class="nav flex-column gap-2">
      <a href="${pageContext.request.contextPath}/index.jsp" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-house fs-5"></i>
        <span>Inicio</span>
      </a>
      <a href="${pageContext.request.contextPath}/catalCOCHES_pub.jsp" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-car-front fs-5"></i>
        <span>Vehículos</span>
      </a>
      <a href="${pageContext.request.contextPath}/catalSERVICIOS_pub.jsp" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-tools fs-5"></i>
        <span>Servicios</span>
      </a>
    </nav>
  </div>
</div>



<main class="container py-5" style="max-width: 1150px;">

  <div class="d-flex align-items-center gap-3 mb-4">
    <a href="${pageContext.request.contextPath}/index.jsp" class="text-dark text-decoration-none d-inline-flex flex-column align-items-center">
      <i class="bi bi-arrow-left fs-4"></i>
      <span style="font-size: 0.75rem;">Regresar</span>
    </a>
  </div>

  <div class="row g-4">

    <!-- TARJETA IMAGEN -->
    <div class="col-md-5">
      <div class="detalle-img-card">
        <img src="${pageContext.request.contextPath}/assets/images/<%= imagen %>" alt="<%= titulo %>">
      </div>
    </div>

    <!-- TARJETA INFORMACION -->
    <div class="col-md-7">
      <div class="detalle-info-card d-flex flex-column justify-content-between h-100">
        <div>
          <h2 class="detalle-titulo mb-4"><%= titulo %></h2>
          <p class="detalle-precio mb-4"><%= precio %></p>

          <p class="detalle-incluye-titulo font-sans mb-2">Incluye:</p>
          <ul class="detalle-lista font-sans">
            <% for (String item : incluye) { %>
            <li><%= item %></li>
            <% } %>
          </ul>
        </div>
      </div>
    </div>

  </div>

  <!-- BOTÓN AGREGAR AL CARRITO -->
  <div class="d-flex justify-content-center mt-4">
    <button onclick="agregarServicioAlCarrito()" class="btn btn-navy px-5 py-3 rounded-3 font-sans fs-5">
      Agregar al carrito
    </button>
  </div>

  <!-- Toast de confirmación -->
  <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 9999">
    <div id="toastCarrito" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body font-sans">
          <i class="bi bi-check-circle me-2"></i>¡Servicio agregado al carrito!
        </div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>
  </div>

</main>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script>
  function agregarServicioAlCarrito() {
    const titulo  = '<%= titulo %>';
    const precio  = '<%= precio %>';
    const imagen  = '${pageContext.request.contextPath}/assets/images/<%= imagen %>';
    const id      = '<%= id %>';

    // Limpiar precio: "$1,500 MXN" -> 1500
    const numPrecio = parseFloat(precio.replace(/[^0-9.]/g, '')) || 0;

    const item = {
      id:          'SRV-' + (id || titulo),
      nombre:      titulo,
      precio:      numPrecio,
      imagen:      imagen,
      tipo:        'Servicio',
      cantidad:    1,
      descripcion: titulo
    };

    const raw  = localStorage.getItem('cart_items');
    const cart = raw ? JSON.parse(raw) : [];

    const existe = cart.findIndex(function(c){ return c.id === item.id; });
    if (existe === -1) {
      cart.push(item);
    }
    localStorage.setItem('cart_items', JSON.stringify(cart));

    // Redirigir al carrito
    window.location.href = '${pageContext.request.contextPath}/carrito.jsp';
  }
</script>
</body>
</html>
