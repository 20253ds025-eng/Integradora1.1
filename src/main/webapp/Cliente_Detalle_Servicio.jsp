<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Detalles del Servicio - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
</head>
<body class="bg-light">

<!-- MÓDULOS GLOBALES -->
<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-5" style="max-width: 950px; margin: 0 auto;">

  <!-- ENCABEZADO Y BOTÓN REGRESAR -->
  <div class="d-flex align-items-center mb-4 gap-3">
    <a href="${pageContext.request.contextPath}/CatalogoServiciosCliente" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
      <i class="bi bi-arrow-left fs-4"></i>
      <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
    </a>
  </div>

  <!-- CONTENEDOR DE DETALLES DEL SERVICIO -->
  <div class="row align-items-stretch mb-5">

    <!-- Columna de Imagen -->
    <div class="col-md-6 mb-4 mb-md-0">
      <div class="card border-0 shadow-sm h-100 rounded-3 overflow-hidden">
        <!-- Usamos una imagen genérica por defecto ya que en BD no hay ruta de foto -->
        <img src="${pageContext.request.contextPath}/assets/images/${servicio.imagen}" class="w-100 h-100" style="object-fit: cover; min-height: 350px;" alt="${servicio.nombreServicio}">
      </div>
    </div>

    <!-- Columna de Información -->
    <div class="col-md-6">
      <div class="card border-0 shadow-sm h-100 rounded-3 p-4 p-lg-5">
        <h2 class="mb-4" style="font-family: 'Playfair Display', serif; font-size: 2rem; color: #1a2a4a;">
          ${servicio.nombreServicio}
        </h2>

        <h3 class="mb-4" style="font-family: 'Playfair Display', serif; color: #333; font-size: 1.5rem;">
          $${servicio.costo} MXN
        </h3>

        <h5 class="mb-3 font-sans fw-bold text-dark" style="font-size: 1.1rem;">Incluye:</h5>

        <p class="font-sans text-muted" style="font-size: 0.95rem; line-height: 1.6;">
          ${servicio.descripcion}
        </p>

        <div class="mt-auto pt-4">
          <span class="badge bg-secondary mb-2">Aplicación: ${servicio.tipoAplicacion}</span>
        </div>
      </div>
    </div>
  </div>

  <!-- BOTÓN DE ACCIÓN CENTRAL -->
  <div class="d-flex justify-content-center">
    <button onclick="agregarServicioAlCarrito()" class="btn btn-navy font-sans px-5 py-2 rounded-2 shadow" style="font-size: 1rem;">
      Agregar al carrito
    </button>
  </div>

  <!-- Toast de confirmación -->
  <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 9999">
    <div id="toastCarrito" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body font-sans">
          <i class="bi bi-check-circle me-2"></i>¡Redirigiendo al carrito...
        </div>
      </div>
    </div>
  </div>

</main>

<jsp:include page="/assets/components/footer.jsp" />

<script>
  function agregarServicioAlCarrito() {
    const nombre  = '${servicio.nombreServicio}';
    const precio  = '${servicio.costo}';
    const id      = '${servicio.idServicio}';

    const item = {
      id:          'SRV-' + (id || Date.now()),
      nombre:      nombre || 'Servicio',
      precio:      parseFloat(precio) || 0,
      imagen:      '${pageContext.request.contextPath}/assets/images/rotar-las-llantas.jpg',
      tipo:        'Servicio',
      cantidad:    1,
      descripcion: '${servicio.descripcion}'
    };

    const raw  = localStorage.getItem('cart_items');
    const cart = raw ? JSON.parse(raw) : [];

    // Evitar duplicados por id
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