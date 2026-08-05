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
    <button onclick="abrirModalSeleccionAuto()" class="btn btn-navy font-sans px-5 py-2 rounded-2 shadow" style="font-size: 1rem;">
      Agregar al carrito
    </button>
  </div>

  <!-- MODAL SELECCIONAR AUTO -->
  <div class="modal fade" id="modalSeleccionAuto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" style="max-width: 420px;">
      <div class="modal-content border-0 shadow-lg rounded-3">
        <div class="modal-header border-0 pb-0">
          <h5 class="modal-title font-serif fw-bold text-dark w-100 text-center" style="font-family: 'Playfair Display', serif;">
            Selecciona el vehiculo
          </h5>
          <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body p-4">
          <p class="text-muted font-sans small mb-3">A que vehiculo le quieres aplicar este servicio?</p>
          <select id="selectAutoServicio" class="form-select font-sans py-2" required>
            <option value="" selected disabled>-- Selecciona un auto --</option>
          </select>
          <div id="msgSinAutos" class="text-muted font-sans small mt-2 d-none">
            No tienes autos registrados. <a href="${pageContext.request.contextPath}/Cliente_Catalogo_Coches.jsp">Registra uno primero</a>.
          </div>
        </div>
        <div class="modal-footer border-0 justify-content-center pb-4">
          <button type="button" class="btn btn-secondary font-sans px-3 rounded-1" data-bs-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-navy font-sans px-4 rounded-1" onclick="confirmarAgregarServicio()">Agregar</button>
        </div>
      </div>
    </div>
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
  const servicioData = {
    id: 'SRV-${servicio.idServicio}',
    nombre: '${servicio.nombreServicio}',
    precio: parseFloat('${servicio.costo}') || 0,
    imagen: '${pageContext.request.contextPath}/assets/images/rotar-las-llantas.jpg',
    tipo: 'Servicio',
    cantidad: 1,
    descripcion: '${servicio.descripcion}'
  };

  async function abrirModalSeleccionAuto() {
    const select = document.getElementById('selectAutoServicio');
    const msgSinAutos = document.getElementById('msgSinAutos');
    select.innerHTML = '<option value="" selected disabled>-- Cargando autos... --</option>';
    msgSinAutos.classList.add('d-none');

    try {
      const resp = await fetch('${pageContext.request.contextPath}/CatalogoCliente?ajax=1');
      const autos = await resp.json();

      select.innerHTML = '<option value="" selected disabled>-- Selecciona un auto --</option>';

      if (!autos || autos.length === 0) {
        msgSinAutos.classList.remove('d-none');
        return;
      }

      autos.forEach(function(auto) {
        const opt = document.createElement('option');
        opt.value = auto.matricula;
        opt.textContent = auto.marca + ' ' + auto.modelo + ' - ' + auto.matricula;
        select.appendChild(opt);
      });

      new bootstrap.Modal(document.getElementById('modalSeleccionAuto')).show();
    } catch(e) {
      alert('Error al cargar los autos disponibles');
    }
  }

  function confirmarAgregarServicio() {
    const select = document.getElementById('selectAutoServicio');
    const matricula = select.value;

    if (!matricula) {
      alert('Selecciona un vehiculo primero');
      return;
    }

    const item = Object.assign({}, servicioData, { matricula: matricula });

    let cart = JSON.parse(localStorage.getItem('cart_items')) || [];
    const existe = cart.findIndex(function(c){ return c.id === item.id && c.matricula === item.matricula; });
    if (existe === -1) {
      cart.push(item);
    } else {
      cart[existe].cantidad++;
    }
    localStorage.setItem('cart_items', JSON.stringify(cart));

    bootstrap.Modal.getInstance(document.getElementById('modalSeleccionAuto')).hide();
    window.location.href = '${pageContext.request.contextPath}/carrito.jsp';
  }
</script>
</body>
</html>