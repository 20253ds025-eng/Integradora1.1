<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle del Vehículo - Click & Drive</title>

    <!-- Google Fonts exacto de la vista principal -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,400..900;1,400..900&display=swap" rel="stylesheet">

    <!-- Bootstrap & Bootstrap Icons -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        html, body {
            width: 100%;
            min-height: 100vh;
            margin: 0;
            padding: 0;
            background-color: #ffffff;
        }

        .title-serif {
            font-family: 'Playfair Display', Georgia, serif !important;
        }

        /* ESTILOS EXACTOS DE BOTONES IGUAL A LA VISTA PRINCIPAL */
        .btn-brand-blue {
            background-color: #003865 !important;
            color: #ffffff !important;
            border: none !important;
            border-radius: 6px;
            font-weight: 600;
            font-size: 0.9rem;
            padding: 0.6rem 1.2rem;
            transition: background-color 0.2s ease;
        }
        .btn-brand-blue:hover {
            background-color: #002544 !important;
            color: #ffffff !important;
        }

        .btn-brand-dark {
            background-color: #050C1A !important;
            color: #ffffff !important;
            border: none !important;
            border-radius: 6px;
            font-weight: 600;
            font-size: 0.9rem;
            padding: 0.6rem 1.2rem;
            transition: background-color 0.2s ease;
        }
        .btn-brand-dark:hover {
            background-color: #000000 !important;
            color: #ffffff !important;
        }

        /* EFECTO HOVER MENÚ HAMBURGUESA */
        .menu-item-hover {
            transition: all 0.25s ease-in-out;
            color: #333333 !important;
            border-radius: 6px;
        }
        .menu-item-hover:hover {
            background-color: #003865 !important;
            color: #ffffff !important;
        }

        .thumb-card {
            cursor: pointer;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            transition: border-color 0.2s ease, transform 0.2s ease;
            background: #fff;
        }
        .thumb-card:hover {
            border-color: #003865 !important;
            transform: translateY(-2px);
        }

        .main-img-box {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            background-color: #ffffff;
            min-height: 320px;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1.5rem;
        }
    </style>
</head>
<body>

<!-- ==================== MENÚ LATERAL (OFFCANVAS) ==================== -->
<div class="offcanvas offcanvas-start" tabindex="-1" id="menuLateral" style="width: 280px;">
    <div class="offcanvas-header d-flex flex-column align-items-center pt-4 pb-2 position-relative">
        <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="offcanvas" aria-label="Close"></button>

        <div class="text-center my-2">
            <span class="logo-c d-block fs-1 fw-bold title-serif" style="line-height: 1;">C</span>
            <span class="fw-bold d-block text-uppercase mt-1" style="font-size: 0.65rem; letter-spacing: 2px;">CLICK & DRIVE</span>
        </div>
    </div>

    <div class="offcanvas-body px-3 pt-3">
        <nav class="nav flex-column gap-2">
            <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link menu-item-hover d-flex align-items-center gap-3 px-3 py-2 fs-6">
                <i class="bi bi-house fs-5"></i>
                <span>Inicio</span>
            </a>
            <a href="${pageContext.request.contextPath}/index.jsp#autos" class="nav-link menu-item-hover d-flex align-items-center gap-3 px-3 py-2 fs-6">
                <i class="bi bi-car-front fs-5"></i>
                <span>Vehículos</span>
            </a>
            <a href="${pageContext.request.contextPath}/index.jsp#servicios" class="nav-link menu-item-hover d-flex align-items-center gap-3 px-3 py-2 fs-6">
                <i class="bi bi-tools fs-5"></i>
                <span>Servicios</span>
            </a>
        </nav>
    </div>
</div>

<!-- ==================== HEADER (IDÉNTICO A VISTA PRINCIPAL) ==================== -->
<header class="py-3 border-bottom mb-4">
    <div class="container-fluid px-4 px-md-5 d-flex justify-content-between align-items-center" style="max-width: 1200px;">

        <!-- Hamburguesa Izquierda -->
        <button class="btn p-0 border-0 fs-3 text-dark" type="button" data-bs-toggle="offcanvas" data-bs-target="#menuLateral">
            <i class="bi bi-list"></i>
        </button>

        <!-- Logo Centro Absoluto -->
        <a href="${pageContext.request.contextPath}/index.jsp" class="text-center text-decoration-none text-dark">
            <span class="logo-c d-block fs-2 fw-bold title-serif" style="line-height: 1;">C</span>
            <span class="fw-bold d-block text-uppercase" style="font-size: 0.65rem; letter-spacing: 2px;">CLICK & DRIVE</span>
        </a>

        <!-- Usuario Derecha -->
        <a href="${pageContext.request.contextPath}/login.jsp" class="text-dark fs-4">
            <i class="bi bi-person"></i>
        </a>
    </div>
</header>

<!-- ==================== CONTENIDO PRINCIPAL ==================== -->
<main class="container-fluid px-4 px-md-5 pb-5" style="max-width: 1140px; margin: 0 auto;">

    <!-- Regresar -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/index.jsp" class="text-dark text-decoration-none d-inline-flex flex-column align-items-center">
            <i class="bi bi-arrow-left fs-4"></i>
            <span style="font-size: 0.75rem;">Regresar</span>
        </a>
    </div>

    <!-- Estructura Principal Grid -->
    <div class="row gy-4 gx-lg-5 align-items-start">

        <!-- COLUMNA IZQUIERDA: FOTOS -->
        <div class="col-12 col-md-6">
            <!-- Imagen Principal -->
            <div class="main-img-box mb-3 shadow-sm">
                <img id="imgPrincipal"
                     src="${pageContext.request.contextPath}/assets/images/corolla1.png"
                     class="img-fluid"
                     style="max-height: 280px; width: auto; object-fit: contain;"
                     alt="Toyota Corolla"
                     onerror="this.onerror=null; this.src='https://via.placeholder.com/400x250?text=Toyota+Corolla';">
            </div>

            <!-- Miniaturas -->
            <div class="row g-2">
                <div class="col-4">
                    <div class="p-2 text-center thumb-card" onclick="cambiarImagen(this.querySelector('img').src)">
                        <img src="${pageContext.request.contextPath}/assets/images/corolla1.png" class="img-fluid" style="height: 55px; object-fit: contain;" onerror="this.src='https://via.placeholder.com/120x80?text=Vista+1'">
                    </div>
                </div>
                <div class="col-4">
                    <div class="p-2 text-center thumb-card" onclick="cambiarImagen(this.querySelector('img').src)">
                        <img src="${pageContext.request.contextPath}/assets/images/corolla2.png" class="img-fluid" style="height: 55px; object-fit: contain;" onerror="this.src='https://via.placeholder.com/120x80?text=Vista+2'">
                    </div>
                </div>
                <div class="col-4">
                    <div class="p-2 text-center thumb-card" onclick="cambiarImagen(this.querySelector('img').src)">
                        <img src="${pageContext.request.contextPath}/assets/images/corolla3.png" class="img-fluid" style="height: 55px; object-fit: contain;" onerror="this.src='https://via.placeholder.com/120x80?text=Vista+3'">
                    </div>
                </div>
            </div>
        </div>

        <!-- COLUMNA DERECHA: TEXTO Y BOTONES ALINEADOS EN BASE -->
        <div class="col-12 col-md-6 d-flex flex-column justify-content-between" style="min-height: 380px;">
            <div>
                <h1 class="title-serif fw-bold mb-2 fs-2">Toyota Corolla 2024</h1>
                <h3 class="title-serif fw-bold mb-4 fs-4 text-dark">$000,000 MXN</h3>

                <ul class="list-unstyled text-secondary mb-4" style="line-height: 1.9; font-size: 0.9rem;">
                    <li><strong>Año:</strong> 2024</li>
                    <li><strong>Transmisión:</strong> Automática</li>
                    <li><strong>Combustible:</strong> Gasolina</li>
                    <li><strong>Color:</strong> Gris</li>
                    <li><strong>Kilometraje:</strong> 10,00 km</li>
                </ul>

                <div class="mb-4">
                    <h5 class="title-serif fw-bold mb-2 fs-5">Descripción:</h5>
                    <p class="text-muted mb-1" style="letter-spacing: 2px;">----------------------------------</p>
                    <p class="text-muted mb-1" style="letter-spacing: 2px;">----------------------------------</p>
                    <p class="text-muted" style="letter-spacing: 2px;">------------------</p>
                </div>
            </div>

            <!-- BOTONES: Mismo tamaño estandarizado e integración abajo a la derecha -->
            <div class="row g-3 pt-2">
                <div class="col-12 col-sm-6">
                    <button class="btn btn-brand-blue w-100 shadow-sm">
                        Agregar al carrito
                    </button>
                </div>
                <div class="col-12 col-sm-6">
                    <button class="btn btn-brand-dark w-100 shadow-sm">
                        Agregar servicio
                    </button>
                </div>
            </div>

        </div>

    </div>

</main>

<!-- JS Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function cambiarImagen(src) {
        document.getElementById('imgPrincipal').src = src;
    }
</script>

</body>
</html>