<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Click & Drive - Cliente</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
</head>
<body>

<!-- IMPORTACIÓN DE MÓDULOS -->
<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-4" style="max-width: 1140px; margin: 0 auto;">

  <!-- HERO SECTION -->
  <section class="row align-items-center my-5 gy-4">
    <div class="col-lg-6">
      <div class="card p-3 shadow-sm border text-center">
        <img src="${pageContext.request.contextPath}/assets/images/inicial.png" class="img-fluid rounded" alt="Auto Principal">
      </div>
    </div>
    <div class="col-lg-6 text-center px-lg-4">
      <h1 class="fw-bold fs-2 text-uppercase mb-3">ENCUENTRA EL AUTO IDEAL PARA TI</h1>
      <p class="font-sans text-muted mb-4 fs-6">Calidad, confianza y el mejor servicio en un solo lugar.</p>
    </div>
  </section>

  <!-- AUTOS DESTACADOS -->
  <section id="autos" class="my-5 py-3">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="fw-bold fs-4 text-uppercase mb-0">AUTOS DESTACADOS</h2>
      <!-- RUTA CORREGIDA AL SERVLET -->
      <!-- CÁMBIALO A ESTO -->
      <a href="${pageContext.request.contextPath}/CatalogoCliente" class="btn btn-navy font-sans px-3 py-1">Ver cátalogo</a>
    </div>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3">

      <!-- Tarjeta 1 (Jetta AUC-001) -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg" class="w-100 h-100" style="object-fit: cover;" alt="Volkswagen Jetta">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Volkswagen Jetta 2023</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$430,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <!-- BOTÓN MODULAR -->
              <jsp:include page="/assets/components/boton_detalle.jsp">
                <jsp:param name="matricula" value="AUC-001" />
              </jsp:include>
            </div>
          </div>
        </div>
      </div>

      <!-- Tarjeta 2 (Prius AUC-002) -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/Priustoyota.png" class="w-100 h-100" style="object-fit: cover;" alt="Toyota Prius">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Toyota Prius 2025</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$600,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <!-- BOTÓN MODULAR -->
              <jsp:include page="/assets/components/boton_detalle.jsp">
                <jsp:param name="matricula" value="AUC-002" />
              </jsp:include>
            </div>
          </div>
        </div>
      </div>

      <!-- Tarjeta 3 (4Runner AUC-003) -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/4runner.png" class="w-100 h-100" style="object-fit: cover;" alt="Toyota 4Runner">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Toyota 4Runner 2026</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,000,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <!-- BOTÓN MODULAR -->
              <jsp:include page="/assets/components/boton_detalle.jsp">
                <jsp:param name="matricula" value="AUC-003" />
              </jsp:include>
            </div>
          </div>
        </div>
      </div>

      <!-- Tarjeta 4 (Corolla AUC-004) -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
            <img src="${pageContext.request.contextPath}/assets/images/tcorolla.png" class="w-100 h-100" style="object-fit: cover;" alt="Toyota Corolla">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Toyota Corolla 2024</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$428,000 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <!-- BOTÓN MODULAR -->
              <jsp:include page="/assets/components/boton_detalle.jsp">
                <jsp:param name="matricula" value="AUC-004" />
              </jsp:include>
            </div>
          </div>
        </div>
      </div>

    </div>
  </section>

  <!-- SECCIÓN SERVICIOS -->
  <section id="servicios" class="my-5 py-3">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="fw-bold fs-4 text-uppercase mb-0">SERVICIOS</h2>
      <a href="${pageContext.request.contextPath}/CatalogoServiciosCliente" class="btn btn-navy font-sans px-3 py-1">Ver cátalogo</a>
    </div>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3">
      <!-- Servicio 1 -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 180px;">
            <img src="${pageContext.request.contextPath}/assets/images/lavado.png" class="w-100 h-100" style="object-fit: cover;" alt="lavado">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Lavado premium</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,200 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/DetalleServicioServlet?id=1" class="btn btn-navy btn-sm rounded-2 px-2 py-1">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>
      <!-- Los otros 3 servicios de momento quedan igual hasta que implementemos su lógica dinámica -->

      <!-- Servicio 2 -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 180px;">
            <img src="${pageContext.request.contextPath}/assets/images/bujiaas.png" class="w-100 h-100" style="object-fit: cover;" alt="bujias">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Cambio de bujías</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,500 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/DetalleServicioServlet?id=2" class="btn btn-navy btn-sm rounded-2 px-2 py-1">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      <!-- Servicio 3 -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 180px;">
            <img src="${pageContext.request.contextPath}/assets/images/rotar-las-llantas.jpg" class="w-100 h-100" style="object-fit: cover;" alt="llantas">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Rotación de llantas</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$600 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/DetalleServicioServlet?id=3" class="btn btn-navy btn-sm rounded-2 px-2 py-1">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      <!-- Servicio 4 -->
      <div class="col">
        <div class="card h-100 shadow-sm border p-2">
          <div class="border rounded mb-2 overflow-hidden" style="height: 180px;">
            <img src="${pageContext.request.contextPath}/assets/images/afinacion.png" class="w-100 h-100" style="object-fit: cover;" alt="afinacion">
          </div>
          <div class="card-body p-2 d-flex flex-column justify-content-between">
            <div>
              <h6 class="card-title mb-1 fs-6 fw-semibold">Afinación</h6>
              <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$800 MXN</p>
            </div>
            <div class="d-flex justify-content-end mt-2">
              <a href="${pageContext.request.contextPath}/DetalleServicioServlet?id=4" class="btn btn-navy btn-sm rounded-2 px-2 py-1">
                <i class="bi bi-eye-fill fs-6"></i>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

</main>

<!-- JS oficial de Bootstrap y pie de página estático integrado directamente -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>