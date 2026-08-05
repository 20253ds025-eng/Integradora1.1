<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Registrar Vehículo Externo - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
</head>
<body>

<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-4" style="max-width: 600px; margin: 0 auto;">

  <div class="d-flex align-items-center mb-4 gap-3">
    <a href="javascript:history.back()" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
      <i class="bi bi-arrow-left fs-4"></i>
      <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
    </a>
    <h2 class="mb-0 fs-3" style="font-family: 'Playfair Display', serif; color: #1a2a4a;">Registrar mi Vehículo</h2>
  </div>

  <p class="font-sans text-muted mb-4" style="font-size: 0.9rem;">
    Registra los datos básicos de tu vehículo (que no compraste en Click & Drive)
    para poder contratarle servicios de mantenimiento. No es necesario subir fotos.
  </p>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
    <div class="alert alert-danger alert-custom font-sans"><%= error %></div>
  <% } %>

  <form action="${pageContext.request.contextPath}/RegistrarAutoExterno" method="post">
    <input type="hidden" name="idServicioOrigen" value="${param.idServicioOrigen}">

    <div class="mb-3">
      <label for="marca" class="form-label-custom">Marca</label>
      <input type="text" class="form-control form-control-custom" id="marca" name="marca" placeholder="Ej: Nissan" required>
    </div>

    <div class="mb-3">
      <label for="modelo" class="form-label-custom">Modelo</label>
      <input type="text" class="form-control form-control-custom" id="modelo" name="modelo" placeholder="Ej: Sentra" required>
    </div>

    <div class="row">
      <div class="col-6 mb-3">
        <label for="anio" class="form-label-custom">Año</label>
        <input type="number" class="form-control form-control-custom" id="anio" name="anio" placeholder="Ej: 2019" min="1900" required>
      </div>
      <div class="col-6 mb-3">
        <label for="numeroSerie" class="form-label-custom">Número de serie (VIN)</label>
        <input type="text" class="form-control form-control-custom" id="numeroSerie" name="numeroSerie" placeholder="Ej: 3VWJETTA1234567" required>
      </div>
    </div>

    <button type="submit" class="btn btn-primary-custom w-100 mt-2">
      Registrar vehículo
    </button>
  </form>

</main>

<jsp:include page="/assets/components/footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>
