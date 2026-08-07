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

      </div>
    </div>
  </div>

  <!-- SELECTOR DE VEHÍCULO PARA EL SERVICIO -->
  <c:if test="${not empty vehiculosCliente}">
  <div class="d-flex justify-content-center mb-3">
    <div style="min-width: 320px; max-width: 450px; width: 100%;">
      <label class="form-label font-sans fw-bold small text-dark mb-1">
        <i class="bi bi-car-front me-1"></i> Vehículo para este servicio:
      </label>
      <select id="selectVehiculo" class="form-select font-sans rounded-2 shadow-sm">
        <c:forEach items="${vehiculosCliente}" var="auto">
          <option value="${auto.matricula}"
                  data-nombre="${auto.marca} ${auto.modelo} ${auto.anio}"
                  data-precio="${auto.precio}"
                  data-imagen="${pageContext.request.contextPath}/assets/images/${auto.imagen}"
                  data-origen="${auto.tipoOrigen}"
                  data-vendido="${auto.vendido}"
                  <c:if test="${auto.matricula == matriculaPreseleccionada}">selected</c:if>>
            ${auto.marca} ${auto.modelo} ${auto.anio} — ${auto.matricula}
          </option>
        </c:forEach>
      </select>
    </div>
  </div>
  </c:if>

  <c:if test="${empty vehiculosCliente}">
  <div class="d-flex justify-content-center mb-3">
    <div style="min-width: 320px; max-width: 450px; width: 100%;">
      <label class="form-label font-sans fw-bold small text-dark mb-1">
        <i class="bi bi-car-front me-1"></i> Vehículo para este servicio:
      </label>
      <select id="selectVehiculo" class="form-select font-sans rounded-2 shadow-sm">
        <option value="" selected>— Sin vehículos registrados —</option>
      </select>
      <small class="text-muted font-sans">Registra un vehículo primero en <a href="${pageContext.request.contextPath}/mis_vehiculos.jsp">Mis vehículos</a></small>
    </div>
  </div>
  </c:if>

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

    // Obtener vehículo seleccionado del dropdown
    const selectVehiculo = document.getElementById('selectVehiculo');
    let matriculaAuto = '';
    let nombreAuto = '';
    let precioAuto = 0;
    let imagenAuto = '';
    let origenAuto = '';
    let vendidoAuto = false;

    if (selectVehiculo && selectVehiculo.value) {
      matriculaAuto = selectVehiculo.value;
      const selectedOption = selectVehiculo.options[selectVehiculo.selectedIndex];
      nombreAuto = selectedOption.getAttribute('data-nombre') || selectedOption.text;
      precioAuto = parseFloat(selectedOption.getAttribute('data-precio')) || 0;
      imagenAuto = selectedOption.getAttribute('data-imagen') || '';
      origenAuto = selectedOption.getAttribute('data-origen') || '';
      vendidoAuto = selectedOption.getAttribute('data-vendido') === 'true';
    }

    const raw  = localStorage.getItem('cart_items');
    const cart = raw ? JSON.parse(raw) : [];

    // Si el auto es de Agencia y aún no ha sido comprado (vendido=false),
    // agregamos automáticamente TAMBIÉN el automóvil al carrito.
    if (matriculaAuto && origenAuto === 'Agencia' && !vendidoAuto) {
      const existeAuto = cart.some(function(c) { return c.id === matriculaAuto; });
      if (!existeAuto) {
        const itemAuto = {
          id:          matriculaAuto,
          nombre:      nombreAuto,
          precio:      precioAuto,
          imagen:      imagenAuto || '${pageContext.request.contextPath}/assets/images/VKjetta.jpg',
          tipo:        'Auto',
          cantidad:    1,
          descripcion: 'Automóvil de agencia seleccionado junto con el servicio.'
        };
        cart.push(itemAuto);
      }
    }

    const item = {
      id:          'SRV-' + (id || Date.now()),
      nombre:      nombre || 'Servicio',
      precio:      parseFloat(precio) || 0,
      imagen:      '${pageContext.request.contextPath}/assets/images/${servicio.imagen}',
      tipo:        'Servicio',
      cantidad:    1,
      descripcion: '${servicio.descripcion}',
      matricula:   matriculaAuto,
      nombreAuto:  nombreAuto
    };

    const existeServicio = cart.findIndex(function(c){ return c.id === item.id; });
    if (existeServicio === -1) {
      cart.push(item);
    }
    localStorage.setItem('cart_items', JSON.stringify(cart));

    // Redirigir al carrito
    window.location.href = '${pageContext.request.contextPath}/carrito.jsp';
  }
</script>
</body>
</html>