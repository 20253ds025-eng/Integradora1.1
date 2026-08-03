<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Catálogo de Servicios - Click & Drive</title>

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
        <a href="${pageContext.request.contextPath}/index_cliente.jsp" class="text-dark text-decoration-none d-flex flex-column align-items-center" style="line-height: 1;">
            <i class="bi bi-arrow-left fs-4"></i>
            <span class="font-sans mt-1" style="font-size: 0.6rem; font-weight: 600;">Regresar</span>
        </a>
        <h2 class="mb-0 fs-3" style="font-family: 'Playfair Display', serif; color: #1a2a4a;">Catálogo de Servicios</h2>
    </div>

    <!-- BARRA DE BÚSQUEDA -->
    <div class="position-relative mb-4">
        <input type="text" class="form-control form-control-custom w-100 font-sans bg-white" id="buscarServicios"
               placeholder="Buscar Servicios ..." style="padding-right: 40px;" onkeyup="filtrarServicios()">
        <i class="bi bi-search position-absolute top-50 end-0 translate-middle-y me-3" style="color: #adb5bd;"></i>
    </div>

    <!-- GRID DE SERVICIOS DINÁMICO (3 Columnas) -->
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4 mb-5" id="contenedorServicios">

        <c:choose>
            <c:when test="${empty listaServicios}">
                <div class="col-12 text-center my-5 w-100">
                    <h4 class="text-muted font-sans">No hay servicios disponibles en este momento.</h4>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${listaServicios}" var="servicio">
                    <div class="col servicio-item">
                        <div class="card h-100 shadow-sm border p-2">
                            <div class="border rounded mb-2 overflow-hidden d-flex align-items-center justify-content-center" style="height: 150px; background: #fff;">
                                <img src="${pageContext.request.contextPath}/assets/images/lavado.png" class="w-100 h-100" style="object-fit: cover;" alt="${servicio.nombreServicio}">
                            </div>
                            <div class="card-body p-2 d-flex align-items-end justify-content-between">
                                <div>
                                    <h6 class="card-title mb-0 font-sans text-dark" style="font-size: 0.85rem;">${servicio.nombreServicio}</h6>
                                    <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$${servicio.costo} MXN</p>
                                </div>

                                <!-- Botón del ojito directo para Servicios -->
                                <a href="${pageContext.request.contextPath}/DetalleServicioServlet?id=${servicio.idServicio}"
                                   class="btn btn-navy btn-sm rounded-2 d-flex align-items-center justify-content-center"
                                   style="width: 32px; height: 32px;" title="Ver detalles">
                                    <i class="bi bi-eye-fill"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>

    </div>

</main>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script>
    function filtrarServicios() {
        const texto = document.getElementById('buscarServicios').value.toLowerCase();
        const tarjetas = document.querySelectorAll('.servicio-item');

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