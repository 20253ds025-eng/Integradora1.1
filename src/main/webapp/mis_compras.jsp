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

</main>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
  const contextPath = "${pageContext.request.contextPath}";
  // Clear any legacy sample data for Mis Compras
  if (!localStorage.getItem('mis_compras_limpio_v2')) {
    localStorage.removeItem('mis_compras');
    localStorage.setItem('mis_compras_limpio_v2', 'true');
  }

  let filtroActual = 'Auto'; // Por defecto muestra pestaña Autos C&D

  function obtenerCompras() {
    const raw = localStorage.getItem('mis_compras');
    if (!raw) {
      return [];
    }
    try {
      const parsed = JSON.parse(raw);
      return Array.isArray(parsed) ? parsed : [];
    } catch(e) {
      return [];
    }
  }

  function renderizarTabla() {
    const compras = obtenerCompras();
    const cuerpo = document.getElementById('cuerpoTablaCompras');
    const paginacion = document.getElementById('contenedorPaginacion');

    let filtradas = compras;
    if (filtroActual === 'Servicio') {
      filtradas = compras.filter(function(c) { return c.tipo.toLowerCase().includes('servicio'); });
    } else if (filtroActual === 'Auto') {
      filtradas = compras.filter(function(c) { return c.tipo.toLowerCase().includes('auto'); });
    }

    if (filtradas.length === 0) {
      cuerpo.innerHTML = 
        '<tr>' +
          '<td colspan="5" class="py-5 text-muted font-sans text-center">' +
            '<i class="bi bi-bag-x text-muted d-block mb-2" style="font-size: 2.5rem;"></i>' +
            '<span>No tienes compras registradas en esta sección todavía.</span>' +
          '</td>' +
        '</tr>';
      paginacion.classList.add('d-none');
      paginacion.classList.remove('d-flex');
      return;
    }

    let html = '';
    filtradas.forEach(function(item) {
      const urlDet = item.detalleUrl || item.url || '#';
      html += 
        '<tr style="border-bottom: 1px solid #e9ecef;">' +
          '<td class="py-3 text-muted fw-semibold">' + item.id + '</td>' +
          '<td class="py-3 text-dark">' + item.tipo + '</td>' +
          '<td class="py-3 text-dark fw-bold">' + item.total + '</td>' +
          '<td class="py-3 text-dark">' + item.estado + '</td>' +
          '<td class="py-3">' +
            '<a href="' + urlDet + '" class="text-navy" style="color: #245580; font-size: 1.25rem;">' +
              '<i class="bi bi-eye"></i>' +
            '</a>' +
          '</td>' +
        '</tr>';
    });

    cuerpo.innerHTML = html;

    if (filtradas.length > 5) {
      paginacion.classList.remove('d-none');
      paginacion.classList.add('d-flex');
    } else {
      paginacion.classList.add('d-none');
      paginacion.classList.remove('d-flex');
    }
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
