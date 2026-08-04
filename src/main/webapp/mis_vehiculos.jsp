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

  <!-- BARRA DE BÚSQUEDA -->
  <div class="mb-4">
    <div class="position-relative" style="max-width: 500px;">
      <input type="text" class="form-control font-sans rounded-1 py-2" id="buscarAutos" placeholder="Buscar Autos ..." style="padding-right: 40px; border-color: #ced4da;">
      <i class="bi bi-search position-absolute top-50 end-0 translate-middle-y me-3 text-muted"></i>
    </div>
  </div>

  <!-- PESTAÑAS Y TABLA -->
  <ul class="nav nav-tabs font-serif fs-5 fw-bold custom-tabs" id="vehiculosTabs" role="tablist" style="border-bottom: 2px solid #497395;">
    <li class="nav-item" role="presentation">
      <button class="nav-link active rounded-0 border-0 px-5 py-3" id="cyD-tab" data-bs-toggle="tab" data-bs-target="#cyD" type="button" role="tab" aria-controls="cyD" aria-selected="true">Autos C&D</button>
    </li>
    <li class="nav-item" role="presentation">
      <button class="nav-link rounded-0 border-0 px-5 py-3" id="externo-tab" data-bs-toggle="tab" data-bs-target="#externo" type="button" role="tab" aria-controls="externo" aria-selected="false">Autos Externo</button>
    </li>
  </ul>
  
  <style>
    .custom-tabs .nav-link {
        color: #001E50 !important;
        background-color: transparent;
    }
    .custom-tabs .nav-link.active {
        color: white !important;
        background-color: #497395 !important;
    }
  </style>
  
  <div class="tab-content mb-4" id="vehiculosTabsContent">
    <!-- Autos C&D -->
    <div class="tab-pane fade show active" id="cyD" role="tabpanel" aria-labelledby="cyD-tab">
      <div class="table-responsive border" style="border-radius: 0 0 5px 5px; border-top: none!important;">
        <table class="table table-striped table-hover mb-0 font-sans align-middle">
          <thead style="background-color: #1a4f76; color: white;">
            <tr>
              <th scope="col" class="font-serif fw-normal fs-5 py-3 ps-4">ID</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3">Marca</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3">Modelo</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3">Precio</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3 text-center">Acciones</th>
            </tr>
          </thead>
          <tbody id="tbodyCyD">
            <!-- Dinamico -->
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- Autos Externo -->
    <div class="tab-pane fade" id="externo" role="tabpanel" aria-labelledby="externo-tab">
      <div class="table-responsive border" style="border-radius: 0 0 5px 5px; border-top: none!important;">
        <table class="table table-striped table-hover mb-0 font-sans align-middle">
          <thead style="background-color: #1a4f76; color: white;">
            <tr>
              <th scope="col" class="font-serif fw-normal fs-5 py-3 ps-4">ID</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3">Marca</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3">Modelo</th>
              <th scope="col" class="font-serif fw-normal fs-5 py-3 text-center">Acciones</th>
            </tr>
          </thead>
          <tbody id="tbodyExterno">
            <!-- Dinamico -->
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <!-- PAGINACIÓN -->
  <div class="d-flex justify-content-center mb-5">
    <nav aria-label="Page navigation">
      <ul class="pagination pagination-sm font-sans gap-1">
        <li class="page-item"><a class="page-link text-muted border-0 bg-light rounded" href="#"><i class="bi bi-chevron-bar-left"></i></a></li>
        <li class="page-item active"><a class="page-link border-0 rounded" href="#" style="background-color: #8da4be; color: white;">1</a></li>
        <li class="page-item"><a class="page-link text-dark border-0 bg-light rounded" href="#">2</a></li>
        <li class="page-item"><a class="page-link text-dark border-0 bg-light rounded" href="#">3</a></li>
        <li class="page-item"><a class="page-link text-dark border-0 bg-light rounded" href="#">4</a></li>
        <li class="page-item"><a class="page-link text-dark border-0 bg-light rounded" href="#">5</a></li>
        <li class="page-item"><a class="page-link text-dark border-0 bg-light rounded" href="#">6</a></li>
        <li class="page-item"><a class="page-link text-muted border-0 bg-light rounded" href="#"><i class="bi bi-chevron-bar-right"></i></a></li>
      </ul>
    </nav>
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

<!-- MODAL VER AUTO -->
<div class="modal fade" id="modalVerAuto" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content border-0 shadow-lg rounded-3">
      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title font-serif fw-bold text-dark w-100 text-center" style="font-family: 'Playfair Display', serif;">
          Detalles del Automóvil
        </h5>
        <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body p-4 text-center">
        <i class="bi bi-car-front-fill" style="font-size: 4rem; color: #1a4f76;"></i>
        <h4 class="mt-3 font-serif fw-bold" id="ver-marca"></h4>
        <h5 class="text-muted" id="ver-modelo"></h5>
        <div class="mt-4 font-sans text-start bg-light p-3 rounded">
            <p class="mb-2"><strong>ID / Matrícula:</strong> <span id="ver-id"></span></p>
            <p class="mb-2"><strong>Precio:</strong> <span id="ver-precio"></span></p>
            <p class="mb-0"><strong>Información Adicional:</strong> <span id="ver-extra"></span></p>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- MODAL EDITAR AUTO -->
<div class="modal fade" id="modalEditarAuto" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 450px;">
    <div class="modal-content border-0 shadow-lg rounded-3">
      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title font-serif fw-bold text-dark w-100 text-center" style="font-family: 'Playfair Display', serif;">
          Editar Automóvil Externo
        </h5>
        <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body p-4">
        <form id="formEditarAuto" onsubmit="guardarEdicionAuto(event)">
          <input type="hidden" id="edit-index">
          <div class="mb-3">
            <label class="form-label font-sans small fw-bold">Marca</label>
            <input type="text" class="form-control font-sans" id="edit-marca" required>
          </div>
          <div class="mb-3">
            <label class="form-label font-sans small fw-bold">Modelo</label>
            <input type="text" class="form-control font-sans" id="edit-modelo" required>
          </div>
          <div class="row mb-3">
            <div class="col-6">
              <label class="form-label font-sans small fw-bold">Año</label>
              <input type="number" class="form-control font-sans" id="edit-anio" required>
            </div>
            <div class="col-6">
              <label class="form-label font-sans small fw-bold">Matrícula</label>
              <input type="text" class="form-control font-sans text-uppercase" id="edit-matricula" required>
            </div>
          </div>
          <div class="mb-4">
            <label class="form-label font-sans small fw-bold">Número de Serie (VIN)</label>
            <input type="text" class="form-control font-sans text-uppercase" id="edit-numSerie" required>
          </div>
          <button type="submit" class="btn text-white font-sans py-2 w-100 rounded-1 shadow-sm" style="background-color: #5c6c84;">
            Guardar Cambios
          </button>
        </form>
      </div>
    </div>
  </div>
</div>


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
    const tbodyCyD = document.getElementById('tbodyCyD');
    const tbodyExterno = document.getElementById('tbodyExterno');
    
    // Autos por defecto (C&D)
    let cyDHtml = 
      '<tr>' +
        '<td class="ps-4 text-muted py-3">#ATE-0015</td>' +
        '<td class="text-muted">Nissan</td>' +
        '<td class="text-muted">Versa</td>' +
        '<td class="text-muted">$250,000</td>' +
        '<td class="text-center">' +
          '<button class="btn btn-sm btn-link text-success p-0" title="Ver detalles" onclick="verAutoCyD(\'#ATE-0015\', \'Nissan\', \'Versa\', \'$250,000\')"><i class="bi bi-eye fs-5"></i></button>' +
        '</td>' +
      '</tr>' +
      '<tr>' +
        '<td class="ps-4 text-muted py-3 bg-light">#ATE-0016</td>' +
        '<td class="text-muted bg-light">Nissan</td>' +
        '<td class="text-muted bg-light">March</td>' +
        '<td class="text-muted bg-light">$180,000</td>' +
        '<td class="text-center bg-light">' +
          '<button class="btn btn-sm btn-link text-success p-0" title="Ver detalles" onclick="verAutoCyD(\'#ATE-0016\', \'Nissan\', \'March\', \'$180,000\')"><i class="bi bi-eye fs-5"></i></button>' +
        '</td>' +
      '</tr>' +
      '<tr>' +
        '<td class="ps-4 text-muted py-3">#ATE-0017</td>' +
        '<td class="text-muted">Volkswagen</td>' +
        '<td class="text-muted">Golf</td>' +
        '<td class="text-muted">$320,000</td>' +
        '<td class="text-center">' +
          '<button class="btn btn-sm btn-link text-success p-0" title="Ver detalles" onclick="verAutoCyD(\'#ATE-0017\', \'Volkswagen\', \'Golf\', \'$320,000\')"><i class="bi bi-eye fs-5"></i></button>' +
        '</td>' +
      '</tr>';
      
    tbodyCyD.innerHTML = cyDHtml;

    // Autos externos (registrados por el usuario)
    let externoHtml = '';
    if (!vehiculos || vehiculos.length === 0) {
      externoHtml = '<tr><td colspan="4" class="text-center py-4 text-muted">No tienes autos externos registrados</td></tr>';
    } else {
      vehiculos.forEach(function(v, i) {
        let bgClass = (i % 2 !== 0) ? 'bg-light' : '';
        externoHtml +=
          '<tr>' +
            '<td class="ps-4 text-muted py-3 ' + bgClass + '">#' + (v.matricula || 'N/A') + '</td>' +
            '<td class="text-muted ' + bgClass + '">' + v.marca + '</td>' +
            '<td class="text-muted ' + bgClass + '">' + v.modelo + '</td>' +
            '<td class="text-center ' + bgClass + '">' +
              '<button class="btn btn-sm btn-link text-danger p-0 me-2" onclick="eliminarAutoExterno(' + i + ')" title="Eliminar"><i class="bi bi-trash fs-5"></i></button>' +
              '<button class="btn btn-sm btn-link text-warning p-0 me-2" onclick="editarAutoExterno(' + i + ')" title="Editar"><i class="bi bi-pencil fs-5"></i></button>' +
              '<button class="btn btn-sm btn-link text-success p-0" onclick="verAutoExterno(' + i + ')" title="Ver detalles"><i class="bi bi-eye fs-5"></i></button>' +
            '</td>' +
          '</tr>';
      });
    }
    
    tbodyExterno.innerHTML = externoHtml;
  }
  
  function eliminarAutoExterno(index) {
      const vehiculos = obtenerVehiculos();
      vehiculos.splice(index, 1);
      localStorage.setItem('mis_vehiculos', JSON.stringify(vehiculos));
      renderizarVehiculos();
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

  function verAutoCyD(id, marca, modelo, precio) {
      document.getElementById('ver-id').textContent = id;
      document.getElementById('ver-marca').textContent = marca;
      document.getElementById('ver-modelo').textContent = modelo;
      document.getElementById('ver-precio').textContent = precio;
      document.getElementById('ver-extra').textContent = 'Agencia Click & Drive';
      new bootstrap.Modal(document.getElementById('modalVerAuto')).show();
  }

  function verAutoExterno(index) {
      const v = obtenerVehiculos()[index];
      document.getElementById('ver-id').textContent = '#' + (v.matricula || 'N/A');
      document.getElementById('ver-marca').textContent = v.marca;
      document.getElementById('ver-modelo').textContent = v.modelo;
      document.getElementById('ver-precio').textContent = 'N/A (Externo)';
      document.getElementById('ver-extra').textContent = 'Año: ' + v.anio + ' | Serie: ' + (v.numSerie || 'N/A');
      new bootstrap.Modal(document.getElementById('modalVerAuto')).show();
  }

  function editarAutoExterno(index) {
      const v = obtenerVehiculos()[index];
      document.getElementById('edit-index').value = index;
      document.getElementById('edit-marca').value = v.marca;
      document.getElementById('edit-modelo').value = v.modelo;
      document.getElementById('edit-anio').value = v.anio;
      document.getElementById('edit-matricula').value = v.matricula;
      document.getElementById('edit-numSerie').value = v.numSerie || '';
      new bootstrap.Modal(document.getElementById('modalEditarAuto')).show();
  }

  function guardarEdicionAuto(event) {
      event.preventDefault();
      const index = document.getElementById('edit-index').value;
      const vehiculos = obtenerVehiculos();
      
      vehiculos[index].marca = document.getElementById('edit-marca').value.trim();
      vehiculos[index].modelo = document.getElementById('edit-modelo').value.trim();
      vehiculos[index].anio = document.getElementById('edit-anio').value.trim();
      vehiculos[index].matricula = document.getElementById('edit-matricula').value.trim();
      vehiculos[index].numSerie = document.getElementById('edit-numSerie').value.trim();
      
      localStorage.setItem('mis_vehiculos', JSON.stringify(vehiculos));
      
      bootstrap.Modal.getInstance(document.getElementById('modalEditarAuto')).hide();
      renderizarVehiculos();
  }

  document.addEventListener('DOMContentLoaded', renderizarVehiculos);
</script>
</body>
</html>
