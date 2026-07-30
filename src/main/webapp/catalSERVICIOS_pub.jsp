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



    <!-- CATALOGO DE SERVICIOS -->
    <section id="autos" class="pt-4 mb-5">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="mb-3">
                <a href="${pageContext.request.contextPath}/index.jsp" class="text-dark text-decoration-none d-inline-flex flex-column align-items-center">
                    <i class="bi bi-arrow-left fs-4"></i>
                    <span style="font-size: 0.75rem;">Regresar</span>
                </a>
            </div>
            <h2 class="fw-bold fs-4 text-uppercase mb-0">CATALOGO DE SERVICIOS</h2>
        </div>

        <%--    BARRA DE BUSQUEDA--%>

        <nav class="navbar bg-body-tertiary">
            <div class="container-fluid">
                <form class="d-flex w-100" role="search" onsubmit="return false;">
                    <input class="form-control me-2" type="search" id="buscarServicio"
                           placeholder="Buscar" aria-label="Buscar" onkeyup="filtrarServicios()"/>
                    <button class="btn btn-link btn-sm rounded-2 px-2 py-1" type="submit">
                        <img src="assets/images/lupa.png" alt="Buscar" width="24" height="24">
                    </button>
                </form>
            </div>
        </nav>



        <!-- Grid de Bootstrap (4 columnas) -->
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mt-4 tarjetas-servicios">

            <%--      TARJETA 1--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/lavado.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Lavado premium">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Lavado premium</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,200 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <%--      TARJETAD 2--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/bujiaas.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Cambio de bujías">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Cambio de bujías</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,500 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <%--      TARJETADE COCHE 3--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/rotar-las-llantas.jpg"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Rotación de llantas">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Rotación de llantas</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$600 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <%--      TARJETADE COCHE 4--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/afinacion.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Afinación">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Afinación</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$800 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>





        <%--FILA 2--%>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mt-2 tarjetas-servicios">

            <%-- TARJETA 1--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/cambiodeaceiteyfiltro.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Cambio de aceite y filtro">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Cambio de aceite y filtro</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$900 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <%--  TARJETA 2--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/pulidoyencerado.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Pulido y encerado">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Pulido y encerado</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,600 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <%-- TARJETA 3--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/alineacionybalanceo.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Alineación y balanceo">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Alineación y balanceo</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,100 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <%-- TARJETA 4--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/cambiodebalatas.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Cambio de balatas">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Cambio de balatas</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,600 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>





        <%--FILA 3--%>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 mt-2 tarjetas-servicios">

            <%-- TARJETA 1--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/detalladodeinteriores.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Detallado de interiores">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Detallado de interiores</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$949 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <%--  TARJETA 2--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/aireacondicionado.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Servicio al aire acondicionado">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Servicio al aire acondicionado</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$1,400 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="#" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>


            <%-- TARJETA 3--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/hojalateriaypintura.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Hojalatería y pintura">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Hojalatería y pintura</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$2,900 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <%-- TARJETA 4--%>
            <div class="col">
                <div class="card h-100 shadow-sm border p-2">
                    <div class="border rounded mb-2 overflow-hidden" style="height: 140px;">
                        <img src="${pageContext.request.contextPath}/assets/images/purgadoycambiodeaceiete.png"
                             class="w-100 h-100"
                             style="object-fit: cover;"
                             alt="Purgado y cambio de anticongelante">
                    </div>
                    <div class="card-body p-2 d-flex flex-column justify-content-between">
                        <div>
                            <h6 class="card-title mb-1 fs-6 fw-semibold">Purgado y cambio de anticongelante</h6>
                            <p class="card-text font-sans fw-bold text-dark mb-0" style="font-size: 0.85rem;">$945 MXN</p>
                        </div>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/alertaAunPaso_1.jsp" class="btn btn-navy w-100 rounded-3 py-2 font-sans fw-normal">
                                Contratar
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
    function filtrarServicios() {
        const texto = document.getElementById('buscarServicio').value.toLowerCase();
        const tarjetas = document.querySelectorAll('.tarjetas-servicios .col');

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
