<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Mis Compras - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">

  <style>
    .tab-custom {
      padding: 10px 32px;
      font-family: 'Playfair Display', Georgia, serif;
      font-size: 1.05rem;
      font-weight: 600;
      border: 1px solid #dee2e6;
      border-bottom: none;
      border-top-left-radius: 6px;
      border-top-right-radius: 6px;
      text-decoration: none;
      transition: all 0.2s ease-in-out;
    }
    .tab-custom.active {
      background-color: #3f6e98 !important;
      color: #ffffff !important;
      border-color: #3f6e98 !important;
    }
    .tab-custom.inactive {
      background-color: #ffffff !important;
      color: #1a1a1a !important;
    }
    .tabla-compras-header {
      background-color: #245580 !important;
      color: #ffffff !important;
      font-family: 'Playfair Display', Georgia, serif;
      font-size: 1.05rem;
    }
    .pagination-custom .btn-page {
      width: 32px;
      height: 32px;
      padding: 0;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      border: 1px solid #bcd0e4;
      background-color: #dbe8f5;
      color: #245580;
      font-weight: 600;
      font-size: 0.85rem;
      border-radius: 4px;
      margin: 0 2px;
      text-decoration: none;
    }
    .pagination-custom .btn-page.active {
      background-color: #8db1d2 !important;
      color: #ffffff !important;
      border-color: #8db1d2 !important;
    }
  </style>
</head>
<body style="background-color: #ffffff;">

<!-- MÓDULOS GLOBALES -->
<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-4" style="max-width: 1000px; margin: 0 auto;">

  <!-- ENCABEZADO: REGRESAR + TÍTULO + BOTÓN AGREGAR COMPRA -->
  <div class="d-flex justify-content-between align-items-center mb-5">
    <div class="d-flex align-items-center gap-3">
      <a href="${pageContext.request.contextPath}/index_cliente.jsp" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
        <i class="bi bi-arrow-left fs-4"></i>
        <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
      </a>
      <h2 class="mb-0 fs-3" style="font-family: 'Playfair Display', Georgia, serif; color: #1a2a4a; font-weight: 700;">Mis compras</h2>
    </div>

    <!-- Botón Agregar nueva compra -->
    <a href="${pageContext.request.contextPath}/CatalogoCliente" class="btn btn-navy font-sans px-4 py-2 rounded-1 shadow-sm" style="font-size: 0.9rem;">
      Agregar nueva compra
    </a>
  </div>

  <!-- PESTAÑAS (TABS): Autos C&D / Servicios -->
  <div class="d-flex align-items-center border-bottom mb-0">
    <a href="javascript:void(0)" onclick="cambiarTab('Auto')" id="tabAutos" class="tab-custom active me-2">Autos C&D</a>
    <a href="javascript:void(0)" onclick="cambiarTab('Servicio')" id="tabServicios" class="tab-custom inactive">Servicios</a>
  </div>

  <!-- TABLA DE COMPRAS -->
  <div class="table-responsive border border-top-0 rounded-bottom-2 mb-4">
    <table class="table table-hover align-middle text-center mb-0">
      <thead class="tabla-compras-header">
        <tr>
          <th class="py-3 fw-bold" style="width: 20%;">ID</th>
          <th class="py-3 fw-bold" style="width: 20%;">Tipo</th>
          <th class="py-3 fw-bold" style="width: 20%;">Total</th>
          <th class="py-3 fw-bold" style="width: 25%;">Estado</th>
          <th class="py-3 fw-bold" style="width: 15%;">Detalles</th>
        </tr>
      </thead>
      <tbody id="cuerpoTablaCompras" class="bg-light font-sans">
        <!-- Se genera dinámicamente -->
      </tbody>
    </table>
  </div>

  <!-- PAGINACIÓN (Se muestra únicamente cuando hay más de 5 registros) -->
  <div id="contenedorPaginacion" class="d-none justify-content-center align-items-center pagination-custom my-4 pb-5">
    <a href="#" class="btn-page"><i class="bi bi-chevron-bar-left"></i></a>
    <a href="#" class="btn-page active">1</a>
    <a href="#" class="btn-page"><i class="bi bi-chevron-bar-right"></i></a>
  </div>
  <!-- MODAL COMPROBANTE DE COMPRA (TICKET) -->
  <div class="modal fade" id="modalTicketDetalle" tabindex="-1" aria-labelledby="modalTicketDetalleLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" style="max-width: 420px;">
      <div class="modal-content rounded-3 shadow">
        <div class="modal-header border-bottom-0 pb-0">
          <h5 class="modal-title font-serif fw-bold text-dark w-100 text-center" id="modalTicketDetalleLabel" style="font-family: 'Playfair Display', serif;">Ticket de Compra</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body p-4">
          <div id="ticketDetalleContainer" class="bg-light p-3 rounded border font-sans" style="font-size: 0.9rem;">
            <!-- Se genera dinámicamente -->
          </div>
        </div>
        <div class="modal-footer border-top-0 pt-0 justify-content-center">
          <button type="button" class="btn btn-navy font-sans px-4 rounded-2" data-bs-dismiss="modal">Cerrar</button>
        </div>
      </div>
    </div>
  </div>

</main>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
  const contextPath = "${pageContext.request.contextPath}";

  let filtroActual = 'Auto';
  let comprasCache = [];

  async function cargarCompras(tipo) {
    try {
      const resp = await fetch(contextPath + '/MisComprasServlet?tipo=' + tipo);
      const data = await resp.json();
      if (Array.isArray(data)) {
        return data;
      }
      return [];
    } catch (e) {
      return [];
    }
  }

  async function renderizarTabla() {
    const compras = await cargarCompras(filtroActual);
    comprasCache = compras;
    const cuerpo = document.getElementById('cuerpoTablaCompras');
    const paginacion = document.getElementById('contenedorPaginacion');

    if (compras.length === 0) {
      cuerpo.innerHTML = 
        '<tr>' +
          '<td colspan="5" class="py-5 text-muted font-sans text-center">' +
            '<i class="bi bi-bag-x text-muted d-block mb-2" style="font-size: 2.5rem;"></i>' +
            '<span>No tienes compras registradas en esta seccion todavia.</span>' +
          '</td>' +
        '</tr>';
      paginacion.classList.add('d-none');
      paginacion.classList.remove('d-flex');
      return;
    }

    let html = '';
    compras.forEach(function(item, index) {
      let tipoCol = item.tipo;
      if (item.tipo === 'Servicio') {
        let srvNombre = item.nombreServicio ? item.nombreServicio : 'Servicio';
        let vehiculoText = item.autoInfo || (item.matriculaAuto ? 'Auto: ' + item.matriculaAuto : '');
        tipoCol = '<div class="text-start ps-3">' +
                    '<span class="d-block fw-semibold text-dark" style="font-size: 0.95rem;">' + srvNombre + '</span>' +
                    (vehiculoText ? '<span class="text-muted small"><i class="bi bi-car-front me-1"></i>' + vehiculoText + '</span>' : '') +
                  '</div>';
      }

      html += 
        '<tr style="border-bottom: 1px solid #e9ecef;">' +
          '<td class="py-3 text-muted fw-semibold">' + item.id + '</td>' +
          '<td class="py-3 text-dark">' + tipoCol + '</td>' +
          '<td class="py-3 text-dark fw-bold">' + item.total + '</td>' +
          '<td class="py-3 text-dark">' + item.estado + '</td>' +
          '<td class="py-3">' +
            '<button onclick="abrirTicketDetalle(' + index + ')" class="btn p-0 text-navy border-0 bg-transparent" style="color: #245580; font-size: 1.25rem;" title="Ver ticket">' +
              '<i class="bi bi-eye"></i>' +
            '</button>' +
          '</td>' +
        '</tr>';
    });

    cuerpo.innerHTML = html;

    if (compras.length > 5) {
      paginacion.classList.remove('d-none');
      paginacion.classList.add('d-flex');
    } else {
      paginacion.classList.add('d-none');
      paginacion.classList.remove('d-flex');
    }
  }

  function abrirTicketDetalle(index) {
    const item = comprasCache[index];
    if (!item) return;

    let html = '';
    html += '<div class="text-center mb-2 fw-bold" style="font-size: 1rem; letter-spacing: 1px;">CLICK & DRIVE</div>';
    html += '<div class="text-center text-muted mb-2" style="font-size: 0.75rem;">Ticket comprobante de ' + item.tipo + '</div>';
    html += '<hr class="my-2">';
    html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Folio / ID:</span><span class="fw-semibold small">' + item.id + '</span></div>';

    if (item.tipo === 'Servicio') {
      html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Servicio:</span><span class="fw-semibold small">' + (item.nombreServicio || 'Servicio') + '</span></div>';
      if (item.autoInfo || item.matriculaAuto) {
        html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Vehículo:</span><span class="fw-semibold small">' + (item.autoInfo || item.matriculaAuto) + '</span></div>';
      }
    } else {
      html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Adquisición:</span><span class="fw-semibold small">Compra de Automóvil</span></div>';
      if (item.fechaVenta) {
        html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Fecha:</span><span class="fw-semibold small">' + item.fechaVenta + '</span></div>';
      }
      if (item.nombreAsesor) {
        html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Asesor:</span><span class="fw-semibold small">' + item.nombreAsesor + '</span></div>';
      }
    }

    html += '<div class="d-flex justify-content-between mb-1"><span class="text-muted small">Estatus:</span><span class="badge bg-success">' + (item.estado || 'Completado') + '</span></div>';
    html += '<hr class="my-2">';
    html += '<div class="d-flex justify-content-between fw-bold" style="font-size: 1.1rem;">';
    html += '<span>TOTAL:</span>';
    html += '<span>' + item.total + '</span>';
    html += '</div>';

    document.getElementById('ticketDetalleContainer').innerHTML = html;
    new bootstrap.Modal(document.getElementById('modalTicketDetalle')).show();
  }

  function cambiarTab(filtro) {
    filtroActual = filtro;
    const tabAutos = document.getElementById('tabAutos');
    const tabServicios = document.getElementById('tabServicios');

    if (filtro === 'Servicio') {
      tabAutos.classList.remove('active');
      tabAutos.classList.add('inactive');
      tabServicios.classList.remove('inactive');
      tabServicios.classList.add('active');
    } else {
      tabServicios.classList.remove('active');
      tabServicios.classList.add('inactive');
      tabAutos.classList.remove('inactive');
      tabAutos.classList.add('active');
    }

    renderizarTabla();
  }

  document.addEventListener('DOMContentLoaded', renderizarTabla);
</script>
</body>
</html>
