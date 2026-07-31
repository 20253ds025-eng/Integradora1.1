<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Click & Drive</title>

  <!-- 1. CSS de Bootstrap local -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">

  <!-- 2. Bootstrap Icons oficial (para que se carguen el menú, ojo y usuario) -->
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

    .logo-text {
      font-family: system-ui, -apple-system, sans-serif;
      font-size: 0.65rem;
      letter-spacing: 2px;
      font-weight: 700;
    }

    .font-sans {
      font-family: system-ui, -apple-system, sans-serif;
    }
    /* Estilos del menú flotante */
    .nav-menu-link {
      font-family: system-ui, -apple-system, sans-serif;
      font-weight: 500;
      color: #333333 !important;
      transition: all 0.2s ease-in-out;
    }

    /* Cambio de color a Azul #001E50 en hover */
    .nav-menu-link:hover {
      background-color: #001E50 !important;
      color: #ffffff !important;
    }

    .nav-menu-link:hover i {
      color: #ffffff !important;
    }

    .logo-c {
      font-family: 'Playfair Display', Georgia, serif;
    }
  </style>
</head>



<body>
<!-- HEADER CON BOTÓN HAMBURGUESA + LOGO JUNTO -->
<header class="border-bottom py-3 sticky-top bg-white">
  <div class="container-fluid d-flex justify-content-between align-items-center px-4" style="max-width: 1200px;">

    <!-- LADO IZQUIERDO: Hamburguesa y Logo agrupados juntos -->
    <div class="d-flex align-items-center gap-3">
      <!-- Botón Hamburguesa -->
      <button class="btn p-0 border-0 fs-2 text-dark" type="button" data-bs-toggle="offcanvas" data-bs-target="#menuLateral" aria-controls="menuLateral">
        <i class="bi bi-list"></i>
      </button>

      <div class="text-center my-2">
        <span class="logo-c d-block fs-1 fw-bold leading-none" style="font-size: 3rem !important; line-height: 1;">C</span>
        <span class="fw-bold d-block text-uppercase mt-1" style="font-size: 0.7rem; letter-spacing: 2px; font-family: system-ui;">CLICK & DRIVE</span>
      </div>
    </div>

    <!-- LADO DERECHO: Icono de Usuario -->
    <a href="${pageContext.request.contextPath}/login.jsp" class="text-dark fs-3 text-decoration-none">
      <i class="bi bi-person"></i>
    </a>

  </div>
</header>

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

      <!-- Inicio -->
      <a href="${pageContext.request.contextPath}/index.jsp" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-house fs-5"></i>
        <span>Inicio</span>
      </a>

      <!-- Vehículos -->
      <a href="${pageContext.request.contextPath}/catalCOCHES_pub.jsp" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-car-front fs-5"></i>
        <span>Vehículos</span>
      </a>

      <!-- Servicios -->
      <a href="${pageContext.request.contextPath}/catalSERVICIOS_pub.jsp" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none">
        <i class="bi bi-tools fs-5"></i>
        <span>Servicios</span>
      </a>

    </nav>
  </div>
</div>



<main class="container py-4" style="max-width: 1150px;">



  <!-- CATALOGO DE AUTOS -->
  <section id="autos" class="pt-4 mb-5">
    <div class="d-flex align-items-center gap-3 mb-4">
      <div class="mb-3">
        <a href="${pageContext.request.contextPath}/index.jsp" class="text-dark text-decoration-none d-inline-flex flex-column align-items-center">
          <i class="bi bi-arrow-left fs-4"></i>
          <span style="font-size: 0.75rem;">Regresar</span>
        </a>
      </div>
      <h2 class="fw-bold fs-4 text-uppercase mb-0">CATALOGO DE AUTOS</h2>
    </div>

    <%--    BARRA DE BUSQUEDA--%>

    <nav class="navbar bg-body-tertiary">
      <div class="container-fluid">
        <form class="d-flex w-100" role="search" onsubmit="return false;">
          <input class="form-control me-2" type="search" id="buscarAuto"
                 placeholder="Buscar" aria-label="Buscar" onkeyup="filtrarAutos()"/>
          <!-- Se cambió 'btn-navy' por 'btn-link' para eliminar el fondo azul -->
          <button class="btn btn-link btn-sm rounded-2 px-2 py-1" type="submit">
            <img src="assets/images/lupa.png" alt="Buscar" width="24" height="24">
          </button>
        </form>
      </div>
    </nav>



    <!-- Grid de Bootstrap (4 columnas) -->
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mt-4 tarjetas-autos">

      <%--      TARJETADE COCHE 1--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/bmw.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="BMW Serie 1">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">BMW Serie 1</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$799,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=bmw" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>


      <%--      TARJETADE COCHE 2--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/4runner.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Toyota 4Runner">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Toyota 4Runner</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,0000,000,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=runner4" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>


      <%--      TARJETADE COCHE 3--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/mazda3.jpg"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Mazda3">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Mazda3</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$403,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=mazda3" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      <%--      TARJETADE COCHE 4--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/HondaCR-V.jpg"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Honda CR-V">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Honda CR-V</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$822,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=hondacrv" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

    </div>

    <%--FILA 2--%>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mt-2 tarjetas-autos">

      <%-- TARJETADE COCHE 1--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/NissanFrontier.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Nissan Frontier">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Nissan Frontier </h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$501,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=frontier" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>


      <%--      TARJETADE COCHE 2--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/KiaSportage.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Kia Sportage">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Kia Sportage</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$647,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=kia" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>


      <%--      TARJETADE COCHE 3--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/NissanKicks.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Nissan Kicks">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Nissan Kicks</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$633,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=kicks" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      <%--      TARJETADE COCHE 4--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/ChevroletTracker.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Chevrolet Tracker">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Chevrolet Tracker</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$468,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=tracker" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <%--    FILA 3--%>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mt-2 tarjetas-autos">

      <%-- TARJETADE COCHE 1--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Volkswagen Jetta">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Volkswagen Jetta </h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$430,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=jetta" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>


      <%--      TARJETADE COCHE 2--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/Priustoyota.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Prius Toyota">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Prius Toyota</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$600,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=prius" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      <%--      TARJETADE COCHE 3--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/tcorolla.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Toyota Corollaa">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Toyota Corolla</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$428,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=corolla" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      <%--      TARJETADE COCHE 4--%>
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/HondaPilot.png"
                 class="w-100 h-100"
                 style="object-fit: cover;"
                 alt="Honda Pilot">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Honda Pilot</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,240,900 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/detalleauto.jsp?id=pilot" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

    </div>

  </section>
</main>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<script>
  function filtrarAutos() {
    const texto = document.getElementById('buscarAuto').value.toLowerCase();
    const tarjetas = document.querySelectorAll('.tarjetas-autos .col');

    tarjetas.forEach(function (tarjeta) {
      const nombre = tarjeta.querySelector('.card-title').textContent.toLowerCase();
      if (nombre.includes(texto)) {
        tarjeta.style.display = '';
      } else {
        tarjeta.style.display = 'none';
      }
    });
  }
</script>
</body>
</html>
