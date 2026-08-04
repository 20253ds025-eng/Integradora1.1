<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Mis Vehículos - Click & Drive</title>

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

  <!-- ENCABEZADO: REGRESAR + TÍTULO + BOTÓN REGISTRAR AUTO EXTERNO -->
  <div class="d-flex justify-content-between align-items-center mb-4">
    <div class="d-flex align-items-center gap-3">
      <a href="${pageContext.request.contextPath}/index_cliente.jsp" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
        <i class="bi bi-arrow-left fs-4"></i>
        <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
      </a>
      <h2 class="mb-0 fs-3" style="font-family: 'Playfair Display', Georgia, serif; color: #1a2a4a; font-weight: 700;">Mis vehículos</h2>
    </div>

    <!-- Botón Registrar Auto externo -->
    <button type="button" class="btn btn-navy font-sans px-4 py-2 rounded-1 shadow-sm" data-bs-toggle="modal" data-bs-target="#modalRegistroAuto" style="font-size: 0.9rem;">
      Registrar auto externo
    </button>
  </div>

  <!-- GRID DE VEHÍCULOS REGISTRADOS -->
  <div class="row row-cols-1 row-cols-md-3 g-4 mb-5" id="contenedorVehiculos">
    <!-- Se puebla dinámicamente -->
  </div>

</main>

<!-- MODAL FORMULARIO REGISTRAR AUTO EXTERNO -->
<div class="modal fade" id="modalRegistroAuto" tabindex="-1" aria-labelledby="modalRegistroAutoLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 450px;">
    <div class="modal-content border-0 shadow-lg rounded-3">
      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title font-serif fw-bold text-dark w-100 text-center" id="modalRegistroAutoLabel" style="font-family: 'Playfair Display', serif;">
          Registrar Automóvil Externo
        </h5>
        <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body p-4">
        <form id="formRegistroAuto" onsubmit="guardarAutoExterno(event)">
          <div class="mb-3">
            <label for="marca" class="form-label font-sans small fw-bold">Marca</label>
            <input type="text" class="form-control font-sans" id="marca" required placeholder="Ej. Volkswagen">
          </div>
          <div class="mb-3">
            <label for="modelo" class="form-label font-sans small fw-bold">Modelo</label>
            <input type="text" class="form-control font-sans" id="modelo" required placeholder="Ej. Jetta">
          </div>
          <div class="row mb-3">
            <div class="col-6">
              <label for="anio" class="form-label font-sans small fw-bold">Año</label>
              <input type="number" class="form-control font-sans" id="anio" required placeholder="2023" min="1990" max="2026">
            </div>
            <div class="col-6">
              <label for="matricula" class="form-label font-sans small fw-bold">Matrícula</label>
              <input type="text" class="form-control font-sans text-uppercase" id="matricula" required placeholder="XYZ-123">
            </div>
          </div>
          <div class="mb-4">
            <label for="numSerie" class="form-label font-sans small fw-bold">Número de Serie (VIN)</label>
            <input type="text" class="form-control font-sans text-uppercase" id="numSerie" required placeholder="3VW123456789">
          </div>

          <button type="submit" class="btn btn-navy font-sans py-2 w-100 rounded-1 shadow-sm">
            Registrar
          </button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- MODAL DE CONFIRMACIÓN ÉXITO REGISTRO AUTO -->
<div class="modal fade" id="modalAutoExito" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 380px;">
    <div class="modal-content border-0 shadow-lg rounded-4 text-center p-4">
      <div class="modal-body p-3">
        <div class="mb-3">
          <i class="bi bi-check-circle-fill text-success" style="font-size: 4rem;"></i>
        </div>
        <h4 class="font-serif fw-bold text-dark mb-2" style="font-family: 'Playfair Display', serif;">
          ¡Auto registrado exitosamente!
        </h4>
        <p class="text-muted font-sans small mb-4">
          Tu vehículo ha sido agregado correctamente a tu lista de mis vehículos.
        </p>
        <button type="button" class="btn btn-navy font-sans px-4 py-2 w-100 rounded-2" data-bs-dismiss="modal">
          Aceptar
        </button>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
  const contextPath = "${pageContext.request.contextPath}";

  // Limpieza inicial si había datos de muestra almacenados previamente
  if (!localStorage.getItem('mis_vehiculos_limpio_v2')) {
    localStorage.removeItem('mis_vehiculos');
    localStorage.setItem('mis_vehiculos_limpio_v2', 'true');
  }

  function obtenerVehiculos() {
    const raw = localStorage.getItem('mis_vehiculos');
    if (!raw) return [];
    try {
      const parsed = JSON.parse(raw);
      return Array.isArray(parsed) ? parsed : [];
    } catch(e) {
      return [];
    }
  }

  function renderizarVehiculos() {
    const vehiculos = obtenerVehiculos();
    const contenedor = document.getElementById('contenedorVehiculos');

    if (!vehiculos || vehiculos.length === 0) {
      contenedor.innerHTML =
        '<div class="col-12 text-center py-5 border rounded-3 bg-light w-100 my-3">' +
          '<i class="bi bi-car-front text-muted mb-3 d-block" style="font-size: 3.5rem;"></i>' +
          '<h4 class="font-serif fw-bold text-dark mb-2" style="font-family: \'Playfair Display\', serif;">No tienes vehículos registrados</h4>' +
          '<p class="text-muted font-sans mb-3">Registra tu primer auto externo con el botón superior para agregarlo aquí.</p>' +
        '</div>';
      return;
    }

    let html = '';
    vehiculos.forEach(function(v) {
      html +=
        '<div class="col">' +
          '<div class="card h-100 shadow-sm border p-3 rounded-3">' +
            '<h5 class="card-title font-serif fw-bold text-dark mb-1" style="font-family: \'Playfair Display\', serif;">' + v.marca + ' ' + v.modelo + ' ' + v.anio + '</h5>' +
            '<div class="font-sans text-muted small mb-3">' +
              '<div><strong>Matrícula:</strong> ' + v.matricula + '</div>' +
              '<div><strong>No. Serie:</strong> ' + (v.numSerie || 'N/A') + '</div>' +
              '<div><span class="badge bg-navy mt-2" style="background-color: #001E50;">' + (v.tipo || 'Auto Externo') + '</span></div>' +
            '</div>' +
          '</div>' +
        '</div>';
    });

    contenedor.innerHTML = html;
  }

  function guardarAutoExterno(event) {
    event.preventDefault();
    const marca = document.getElementById('marca').value.trim();
    const modelo = document.getElementById('modelo').value.trim();
    const anio = document.getElementById('anio').value.trim();
    const matricula = document.getElementById('matricula').value.trim();
    const numSerie = document.getElementById('numSerie').value.trim();

    const nuevoAuto = {
      marca: marca,
      modelo: modelo,
      anio: anio,
      matricula: matricula,
      numSerie: numSerie,
      imagen: contextPath + "/assets/images/inicial.png",
      tipo: "Auto Externo"
    };

    const vehiculos = obtenerVehiculos();
    vehiculos.unshift(nuevoAuto);
    localStorage.setItem('mis_vehiculos', JSON.stringify(vehiculos));

    document.getElementById('formRegistroAuto').reset();

    const modalRegEl = document.getElementById('modalRegistroAuto');
    const modalReg = bootstrap.Modal.getInstance(modalRegEl);
    if (modalReg) modalReg.hide();

    renderizarVehiculos();

    const modalExito = new bootstrap.Modal(document.getElementById('modalAutoExito'));
    modalExito.show();
  }

  document.addEventListener('DOMContentLoaded', renderizarVehiculos);
</script>
</body>
</html>
