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



<!-- COMPONENTE MENU LATERAL (OFFCANVAS) -->
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
            <a href="#autos" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none" data-bs-dismiss="offcanvas">
                <i class="bi bi-car-front fs-5"></i>
                <span>Vehículos</span>
            </a>

            <!-- Servicios -->
            <a href="#servicios" class="nav-menu-link d-flex align-items-center gap-3 px-3 py-2 rounded text-decoration-none" data-bs-dismiss="offcanvas">
                <i class="bi bi-tools fs-5"></i>
                <span>Servicios</span>
            </a>

        </nav>
    </div>
</div>

<main class="container py-4" style="max-width: 1150px;">

    <!-- HERO SECTION -->
    <section class="row align-items-center my-4 gy-4">
        <div class="col-lg-6">
            <div class="card p-3 shadow-sm border text-center">
                <img src="${pageContext.request.contextPath}/assets/images/inicial.png" class="img-fluid rounded" alt="Auto Principal">
            </div>
        </div>
        <div class="col-lg-6 text-center px-lg-4">
            <h1 class="fw-bold fs-2 text-uppercase mb-3">ENCUENTRA EL AUTO IDEAL PARA TI</h1>
            <p class="font-sans text-muted mb-4 fs-6">Calidad, confianza y el mejor servicio en un solo lugar.</p>
            <a href="#autos" class="btn btn-navy font-sans px-4 py-2">Ver catálogo</a>
        </div>
    </section>

    <!-- AUTOS DESTACADOS -->
    <section id="autos" class="pt-4 mb-5">
        <h2 class="fw-bold fs-4 mb-4 text-uppercase">AUTOS DESTACADOS</h2>

        <!-- Grid de Bootstrap (4 columnas) -->
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3">

            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded p-2 mb-2 text-center bg-white d-flex align-items-center justify-content-center" style="height: 140px; overflow: hidden;">
                        <img src="${pageContext.request.contextPath}/assets/images/VKjetta.jpg" class="img-fluid"
                             style="max-height: 100px; width: 85%; object-fit: contain;"
                             alt="Auto" >
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Volkswagen Jetta</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$430,000 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Tarjeta 2 -->
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded p-2 mb-2 text-center bg-white d-flex align-items-center justify-content-center" style="height: 140px; overflow: hidden;"">
                        <img src="${pageContext.request.contextPath}/assets/images/bmw.png" class="img-fluid" style="max-height: 100px; width: 85%; object-fit: contain;"
                             alt="Auto">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">BMW Serie 1</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$600,000 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-navy btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tarjeta 3 -->
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded p-2 mb-2 text-center bg-white">
                        <img src="https://via.placeholder.com/250x140/ffffff/000000?text=Volkswagen+Jetta" class="img-fluid" alt="img">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Volkswagen Jetta</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$430,000 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-dark btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tarjeta 4 -->
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded p-2 mb-2 text-center bg-white">
                        <img src="https://via.placeholder.com/250x140/ffffff/000000?text=Volkswagen+Jetta" class="img-fluid" alt="Volkswagen Jetta">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Volkswagen Jetta</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$430,000 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-dark btn-sm rounded-2 px-2 py-1" title="Ver detalles">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>

    <!-- SECCIÓN SERVICIOS -->
    <section id="servicios" class="pt-4 mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold fs-4 text-uppercase mb-0">SERVICIOS</h2>
            <a href="#" class="btn btn-dark font-sans px-3 py-1">Ver más</a>
        </div>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3">

            <!-- Servicio 1 -->
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 110px;">
                        <img src="https://via.placeholder.com/250x110/555555/ffffff?text=Servicio" class="w-100 h-100 object-fit-cover" alt="Servicio">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Lavado premium</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,200 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-dark btn-sm rounded-2 px-2 py-1">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Servicio 2 -->
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 110px;">
                        <img src="https://via.placeholder.com/250x110/555555/ffffff?text=Servicio" class="w-100 h-100 object-fit-cover" alt="Servicio">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Cambio de bujías</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,500 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-dark btn-sm rounded-2 px-2 py-1">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Servicio 3 (Borde azul para simular elemento seleccionado) -->
            <div class="col">
                <div class="card h-100 shadow-sm border border-primary border-2 p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 110px;">
                        <img src="https://via.placeholder.com/250x110/555555/ffffff?text=Servicio" class="w-100 h-100 object-fit-cover" alt="Servicio">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Rotación de llantas</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$600 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-dark btn-sm rounded-2 px-2 py-1">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Servicio 4 -->
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 110px;">
                        <img src="https://via.placeholder.com/250x110/555555/ffffff?text=Servicio" class="w-100 h-100 object-fit-cover" alt="Servicio">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Cambio de aceite</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$800 MXN</p>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                            <a href="#" class="btn btn-dark btn-sm rounded-2 px-2 py-1">
                                <i class="bi bi-eye-fill fs-6"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>

</main>

<!-- JS oficial de Bootstrap para que abra el sidebar/offcanvas al hacer clic -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>