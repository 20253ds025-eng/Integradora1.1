<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Mi Carrito - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
</head>
<body style="background-color: #ffffff;">

<!-- MÓDULOS GLOBALES -->
<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-4" style="max-width: 1000px; margin: 0 auto;">

  <!-- ENCABEZADO Y BOTÓN REGRESAR -->
  <div class="d-flex align-items-center mb-4 gap-3">
    <a href="${pageContext.request.contextPath}/index_cliente.jsp" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
      <i class="bi bi-arrow-left fs-4"></i>
      <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
    </a>
    <h2 class="mb-0 fs-3" style="font-family: 'Playfair Display', Georgia, serif; color: #1a2a4a; font-weight: 700;">Mi Carrito</h2>
  </div>

  <!-- BARRA DE BÚSQUEDA DE PRODUCTO (Se muestra si hay ítems) -->
  <div id="contenedorBuscador" class="position-relative mb-4 d-none" style="max-width: 420px;">
    <input type="text" class="form-control font-sans bg-white border rounded-2 pe-5 py-2" id="buscarProducto"
           placeholder="Buscar Producto ..." onkeyup="filtrarCarrito()" style="border-color: #ced4da;">
    <i class="bi bi-search position-absolute top-50 end-0 translate-middle-y me-3 text-muted"></i>
  </div>

  <!-- CONTENEDOR DE ITEMS DEL CARRITO -->
  <div id="contenedorCarrito">
    <!-- Se llena dinámicamente mediante JavaScript -->
  </div>

  <!-- SECCIÓN DE TOTAL Y BOTÓN PROCEDER A COMPRA -->
  <div id="seccionTotal" class="d-none mt-4 text-end pb-5">
    <h4 class="mb-3" style="font-family: 'Playfair Display', Georgia, serif; color: #1a1a1a;">
      Total: <span id="montoTotalText" class="fw-bold">$0.00 MXN</span>
    </h4>
    <button onclick="procederACompra()" class="btn btn-navy font-sans px-4 py-2 rounded-1 shadow-sm" style="font-size: 0.95rem; min-width: 200px;">
      Proceder a compra
    </button>
  </div>

</main>

<!-- MODAL VISTA RÁPIDA DE DETALLES -->
<div class="modal fade" id="modalDetalleProducto" tabindex="-1" aria-labelledby="modalDetalleLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 420px;">
    <div class="modal-content border-0 shadow-lg rounded-3">
      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title font-serif fw-bold text-dark w-100 text-center" id="modalDetalleLabel" style="font-family: 'Playfair Display', serif;">
          Detalles del Producto
        </h5>
        <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body text-center p-4">
        <div class="border rounded p-2 mb-3 bg-light d-flex align-items-center justify-content-center" style="height: 180px;">
          <img id="modalImg" src="" class="img-fluid rounded" style="max-height: 160px; object-fit: contain;" alt="Producto">
        </div>
        <h6 id="modalTitulo" class="fw-bold font-serif fs-5 text-dark mb-1" style="font-family: 'Playfair Display', serif;"></h6>
        <p id="modalId" class="text-muted font-sans small mb-2"></p>
        <p id="modalDescripcion" class="text-secondary font-sans small mb-3"></p>
        <h5 id="modalPrecio" class="fw-bold font-serif text-dark mb-0" style="font-family: 'Playfair Display', serif;"></h5>
      </div>
    </div>
  </div>
</div>

<!-- MODAL DE CONFIRMACIÓN DE COMPRA ÉXITO -->
<div class="modal fade" id="modalCompraExitosa" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 380px;">
    <div class="modal-content border-0 shadow-lg rounded-4 text-center p-4">
      <div class="modal-body p-3">
        <div class="mb-3">
          <i class="bi bi-check-circle-fill text-success" style="font-size: 4rem;"></i>
        </div>
        <h4 class="font-serif fw-bold text-dark mb-2" style="font-family: 'Playfair Display', serif;">
          ¡Compra realizada con éxito!
        </h4>
        <p class="text-muted font-sans small mb-4">
          Tu pedido ha sido procesado correctamente y registrado en tu sección de Mis Compras.
        </p>
        <a href="${pageContext.request.contextPath}/mis_compras.jsp" class="btn btn-navy font-sans px-4 py-2 w-100 rounded-2">
          Ver mis compras
        </a>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
  const contextPath = "${pageContext.request.contextPath}";

  function obtenerCarrito() {
    const raw = localStorage.getItem('cart_items');
    if (!raw) return [];
    try { return JSON.parse(raw); } catch(e) { return []; }
  }

  function guardarCarrito(items) {
    localStorage.setItem('cart_items', JSON.stringify(items));
  }

  function renderizarCarrito() {
    const items = obtenerCarrito();
    const contenedor = document.getElementById('contenedorCarrito');
    const seccionTotal = document.getElementById('seccionTotal');
    const contenedorBuscador = document.getElementById('contenedorBuscador');

    if (!items || items.length === 0) {
      // ESTADO VACÍO EXACTO A LA CAPTURA DEL USUARIO
      contenedor.innerHTML = 
        '<div class="text-center py-5 my-4 border rounded-3 bg-white shadow-sm" style="max-width: 550px; margin: 0 auto;">' +
          '<div class="mb-3">' +
            '<i class="bi bi-cart3" style="font-size: 5rem; color: #001E50;"></i>' +
          '</div>' +
          '<h3 class="font-serif fw-bold text-dark mb-1" style="font-family: \'Playfair Display\', serif; font-size: 1.8rem;">Tu carrito está</h3>' +
          '<h3 class="font-serif fw-bold text-dark mb-4" style="font-family: \'Playfair Display\', serif; font-size: 1.8rem; letter-spacing: 1px;">VACÍO.</h3>' +
          '<div class="d-flex justify-content-center gap-3 mt-3">' +
            '<a href="' + contextPath + '/CatalogoCliente" class="btn btn-navy font-sans px-4 py-2 rounded-1 shadow-sm" style="min-width: 140px; font-size: 0.9rem;">Ver autos</a>' +
            '<a href="' + contextPath + '/CatalogoServiciosCliente" class="btn btn-navy font-sans px-4 py-2 rounded-1 shadow-sm" style="min-width: 140px; font-size: 0.9rem;">Ver servicios</a>' +
          '</div>' +
        '</div>';

      seccionTotal.classList.add('d-none');
      contenedorBuscador.classList.add('d-none');
      return;
    }

    seccionTotal.classList.remove('d-none');
    contenedorBuscador.classList.remove('d-none');
    let html = '';
    let totalGeneral = 0;

    items.forEach(function(item, index) {
      const subtotal = item.precio * item.cantidad;
      totalGeneral += subtotal;
      const precioFormatted = subtotal.toLocaleString('es-MX', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
      const tituloColumna = (item.tipo === 'Servicio') ? 'Total' : 'Precio';

      html += 
        '<div class="card mb-4 border rounded-3 shadow-sm item-card" data-nombre="' + item.nombre.toLowerCase() + '">' +
          '<div class="card-header bg-white border-bottom py-3 px-4 d-flex justify-content-between align-items-center">' +
            '<div class="row w-100 align-items-center text-center font-serif fw-bold" style="font-family: \'Playfair Display\', serif; color: #1a1a1a;">' +
              '<div class="col-4 text-center">Producto</div>' +
              '<div class="col-2 text-center">Id</div>' +
              '<div class="col-3 text-center">Cantidad</div>' +
              '<div class="col-3 text-center">' + tituloColumna + '</div>' +
            '</div>' +
            '<button class="btn p-0 border-0 text-dark ms-2" onclick="eliminarItem(' + index + ')" title="Eliminar producto">' +
              '<i class="bi bi-x-lg fs-5"></i>' +
            '</button>' +
          '</div>' +
          '<div class="card-body p-4">' +
            '<div class="row align-items-center text-center">' +
              '<div class="col-4 d-flex justify-content-center">' +
                '<img src="' + item.imagen + '" class="img-fluid rounded border p-1" style="max-height: 100px; width: 130px; object-fit: cover;" alt="' + item.nombre + '">' +
              '</div>' +
              '<div class="col-2 font-sans fw-semibold text-dark" style="font-size: 0.9rem;">' +
                item.id +
              '</div>' +
              '<div class="col-3 d-flex justify-content-center">' +
                '<input type="number" class="form-control text-center rounded border" style="width: 70px;" value="' + item.cantidad + '" min="1" onchange="cambiarCantidad(' + index + ', this.value)">' +
              '</div>' +
              '<div class="col-3 d-flex justify-content-center align-items-center gap-3">' +
                '<span class="fw-bold text-dark" style="font-family: \'Playfair Display\', serif; font-size: 1rem;">$' + precioFormatted + ' MXN</span>' +
                '<button onclick="abrirModalDetalle(' + index + ')" class="btn btn-navy p-2 rounded-2 d-flex align-items-center justify-content-center" style="width: 36px; height: 36px;" title="Ver detalles">' +
                  '<i class="bi bi-eye-fill"></i>' +
                '</button>' +
              '</div>' +
            '</div>' +
          '</div>' +
        '</div>';
    });

    contenedor.innerHTML = html;
    document.getElementById('montoTotalText').innerText = '$' + totalGeneral.toLocaleString('es-MX', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) + ' MXN';
  }

  function abrirModalDetalle(index) {
    const items = obtenerCarrito();
    const item = items[index];
    if (!item) return;

    document.getElementById('modalDetalleLabel').innerText = (item.tipo === 'Servicio') ? 'Detalles del Servicio' : 'Detalles del Auto';
    document.getElementById('modalImg').src = item.imagen;
    document.getElementById('modalTitulo').innerText = item.nombre;
    document.getElementById('modalId').innerText = 'ID: ' + item.id;
    document.getElementById('modalDescripcion').innerText = item.descripcion || 'Detalle del producto en catálogo.';
    document.getElementById('modalPrecio').innerText = '$' + (item.precio * item.cantidad).toLocaleString('es-MX', { minimumFractionDigits: 2 }) + ' MXN';

    const modal = new bootstrap.Modal(document.getElementById('modalDetalleProducto'));
    modal.show();
  }

  function eliminarItem(index) {
    const items = obtenerCarrito();
    items.splice(index, 1);
    guardarCarrito(items);
    renderizarCarrito();
  }

  function cambiarCantidad(index, cantidad) {
    const items = obtenerCarrito();
    const cant = parseInt(cantidad);
    if (cant > 0) {
      items[index].cantidad = cant;
      guardarCarrito(items);
      renderizarCarrito();
    }
  }

  function filtrarCarrito() {
    const query = document.getElementById('buscarProducto').value.toLowerCase();
    const cards = document.querySelectorAll('.item-card');
    cards.forEach(function(card) {
      const nombre = card.getAttribute('data-nombre');
      if (nombre.includes(query)) {
        card.classList.remove('d-none');
      } else {
        card.classList.add('d-none');
      }
    });
  }

  async function procederACompra() {
    const items = obtenerCarrito();
    if (!items || items.length === 0) return;

    // Enviar al servlet para guardar en la BD
    const payload = { items: items };
    try {
      const resp = await fetch(contextPath + '/CarritoServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await resp.json();
      if (data.success) {
        localStorage.removeItem('cart_items');
        const modalExitosa = new bootstrap.Modal(document.getElementById('modalCompraExitosa'));
        modalExitosa.show();
      } else {
        alert('Error: ' + (data.error || 'No se pudo procesar la compra'));
      }
    } catch (e) {
      alert('Error de conexion con el servidor');
    }
  }

  document.addEventListener('DOMContentLoaded', renderizarCarrito);
</script>
</body>
</html>
