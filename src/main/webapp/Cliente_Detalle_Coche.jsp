<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Detalles del Vehículo - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
</head>
<body>

<!-- MÓDULOS GLOBALES -->
<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-4" style="max-width: 950px; margin: 0 auto;">

  <!-- ENCABEZADO Y BOTÓN REGRESAR -->
  <div class="d-flex align-items-center mb-4 gap-3">
    <a href="${pageContext.request.contextPath}/CatalogoCliente" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
      <i class="bi bi-arrow-left fs-4"></i>
      <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
    </a>
    <h2 class="mb-0 fs-3" style="font-family: 'Playfair Display', serif; color: #1a2a4a;">Detalles del Vehiculo</h2>
  </div>

  <!-- CONTENEDOR PRINCIPAL (2 Columnas) -->
  <div class="row gx-5 mt-4">

    <!-- COLUMNA IZQUIERDA: Imágenes -->
    <div class="col-md-7 mb-4">
      <!-- Imagen Principal -->
      <div class="card shadow-sm border p-2 mb-3 rounded-2">
        <div class="d-flex align-items-center justify-content-center" style="height: 320px; background: #fff;">
          <!-- Imagen por defecto genérica. En un proyecto real aquí iría la URL de la foto en BD -->
          <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg" class="w-100 h-100" style="object-fit: contain;" alt="${vehiculo.marca} ${vehiculo.modelo}">
        </div>
      </div>

      <!-- Miniaturas (3 columnas) -->
      <div class="row g-2">
        <div class="col-4">
          <div class="card shadow-sm border p-1 rounded-2">
            <div class="d-flex align-items-center justify-content-center" style="height: 80px; background: #fff;">
              <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg" class="w-100 h-100" style="object-fit: contain;" alt="Vista trasera">
            </div>
          </div>
        </div>
        <div class="col-4">
          <div class="card shadow-sm border p-1 rounded-2">
            <div class="d-flex align-items-center justify-content-center" style="height: 80px; background: #fff;">
              <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg" class="w-100 h-100" style="object-fit: contain;" alt="Vista lateral">
            </div>
          </div>
        </div>
        <div class="col-4">
          <div class="card shadow-sm border p-1 rounded-2">
            <div class="d-flex align-items-center justify-content-center" style="height: 80px; background: #fff;">
              <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg" class="w-100 h-100" style="object-fit: contain;" alt="Vista frontal">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- COLUMNA DERECHA: Información del vehículo (¡TODO ESTO AHORA ES DINÁMICO!) -->
    <div class="col-md-5 pt-2">
      <h3 class="mb-3" style="font-family: 'Playfair Display', serif; font-size: 1.8rem;">
        ${vehiculo.marca} ${vehiculo.modelo} ${vehiculo.anio}
      </h3>

      <h4 class="mb-4" style="font-family: 'Playfair Display', serif; color: #333; font-size: 1.4rem;">
        $${vehiculo.precio} MXN
      </h4>

      <div class="font-sans mb-4 text-dark" style="font-size: 0.95rem; line-height: 1.5;">
        <div><strong>Año:</strong> ${vehiculo.anio}</div>
        <div><strong>Origen:</strong> ${vehiculo.tipoOrigen}</div>
        <div><strong>Matrícula:</strong> ${vehiculo.matricula}</div>
        <div><strong>No. Serie:</strong> ${vehiculo.numeroSerie}</div>
      </div>

      <h5 class="mb-2" style="font-family: 'Playfair Display', serif; font-size: 1.2rem;">Descripción:</h5>
      <p class="font-sans text-muted" style="font-size: 0.9rem; line-height: 1.6;">
        ${vehiculo.descripcion}
      </p>
    </div>
  </div>

  <!-- BOTONES INFERIORES -->
  <div class="d-flex justify-content-center gap-4 mt-5 mb-5">
    <button onclick="agregarAlCarrito()" class="btn btn-navy font-sans px-4 py-2 rounded-1 shadow-sm" style="width: 220px; font-size: 0.9rem;">
      Agregar al carrito
    </button>
    <button onclick="window.location.href='${pageContext.request.contextPath}/Cliente_Catalogo_Serv.jsp'" class="btn font-sans px-4 py-2 rounded-1 shadow-sm text-white" style="background-color: #050a12; width: 220px; font-size: 0.9rem;">
      Agregar servicio
    </button>
  </div>

</main>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script>
    function agregarAlCarrito() {
        const item = {
            id: "${vehiculo.matricula}",
            nombre: "${vehiculo.marca} ${vehiculo.modelo} ${vehiculo.anio}",
            precio: ${vehiculo.precio != null ? vehiculo.precio : 0},
            imagen: "${pageContext.request.contextPath}/assets/images/VKjetta.jpg",
            tipo: "Auto",
            cantidad: 1,
            descripcion: "${vehiculo.descripcion}"
        };
        
        let cart = JSON.parse(localStorage.getItem('cart_items')) || [];
        const existing = cart.find(i => i.id === item.id);
        if (existing) {
            existing.cantidad++;
        } else {
            cart.push(item);
        }
        localStorage.setItem('cart_items', JSON.stringify(cart));
        window.location.href = "${pageContext.request.contextPath}/carrito.jsp";
    }
</script>
</body>
</html>