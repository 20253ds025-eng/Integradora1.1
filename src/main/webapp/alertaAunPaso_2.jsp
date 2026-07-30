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

    <!-- 2. Bootstrap Icons oficial -->
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

        .font-sans {
            font-family: system-ui, -apple-system, sans-serif;
        }

        /* Contenedor centrado en toda la pantalla */
        .aviso-wrapper {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1.5rem;
        }

        /* Tarjeta del aviso */
        .aviso-card {
            width: 100%;
            max-width: 520px;
            border: 1px solid #e0e0e0;
            border-radius: 14px;
            padding: 3rem 2.5rem;
            text-align: center;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
        }

        .aviso-icono {
            font-size: 4.5rem;
            color: #001E50;
            margin-bottom: 1.5rem;
        }

        .aviso-titulo {
            font-size: 1.6rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .aviso-texto {
            font-size: 1.15rem;
            line-height: 1.4;
            margin-bottom: 2rem;
        }

        .btn-navy {
            background-color: #001E50;
            color: #ffffff;
            border: none;
        }

        .btn-navy:hover {
            background-color: #00133a;
            color: #ffffff;
        }

        .btn-outline-navy {
            background-color: #001E50;
            color: #ffffff;
            border: 1px solid #001E50;
        }

        .btn-outline-navy:hover {
            background-color: #00133a;
            color: #ffffff;
        }
    </style>
</head>
<body>

<div class="aviso-wrapper">
    <div class="aviso-card">

        <!-- Icono de carrito -->
        <div class="aviso-icono">
            <i class="bi bi-cart3"></i>
        </div>

        <!-- Texto -->
        <p class="aviso-titulo mb-2">¡Estás a un paso!</p>
        <p class="aviso-texto font-sans">
            Inicia sesión o regístrate si eres nuevo usuario para poder agregar articulos al carrito.
        </p>

        <!-- Botones -->
        <div class="d-flex justify-content-center gap-3 flex-wrap">
            <a href="login.jsp" class="btn btn-navy px-4 py-2 rounded-2 font-sans">
                Iniciar sesión
            </a>
            <a href="registro.jsp" class="btn btn-outline-navy px-4 py-2 rounded-2 font-sans">
                Registrarme ahora
            </a>
        </div>

    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>
