<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Click & Drive - Iniciar Sesión</title>
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/styles.css" rel="stylesheet">
    <!-- Agregamos los iconos de Bootstrap si no los tienes en tu styles.css -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        /* Aseguramos la posición relativa en el contenedor */
        .card-auth {
            position: relative !important;
        }

        /* Estilo para el botón de cerrar (X) */
        .btn-close-custom {
            position: absolute;
            top: 15px;
            right: 20px;
            color: #6c757d;
            font-size: 1.25rem;
            text-decoration: none;
            line-height: 1;
            transition: color 0.2s ease, transform 0.2s ease;
            z-index: 10;
        }

        .btn-close-custom:hover {
            color: #000000;
            transform: scale(1.15);
        }
    </style>
</head>
<body>

<!-- Agregamos 'position-relative' al div contenedor -->
<div class="card-auth position-relative" style="margin: 40px auto;">

    <!-- AQUÍ SE COLOCA LA 'X' QUE REDIRIGE A INDEX -->
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn-close-custom" aria-label="Cerrar">
        <i class="bi bi-x-lg"></i>
    </a>

    <!-- Logo -->
    <div class="text-center mb-4">
        <img src="assets/images/logo.png" alt="Click & Drive" class="logo-img" style="max-width: 180px; height: auto;">
        <p class="logo-subtitle">Tu agencia de confianza</p>
    </div>

    <!-- Título -->
    <h5 class="section-title">Iniciar Sesión</h5>
    <p class="section-subtitle">Accede a tu cuenta para continuar</p>

    <!-- Mensajes -->
    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");
        if (error != null) {
    %>
    <div class="alert alert-danger alert-custom" role="alert">
        <span><%= error %></span>
    </div>
    <% } %>
    <%
        if (success != null) {
    %>
    <div class="alert alert-success alert-custom" role="alert">
        <span><%= success %></span>
    </div>
    <% } %>

    <!-- Formulario -->
    <form action="LoginServlet" method="post">
        <div class="mb-3">
            <label for="correo" class="form-label-custom">Correo electrónico</label>
            <input type="email" class="form-control form-control-custom" id="correo" name="correo"
                   placeholder="Ej: carlosalberto@gmail.com" required>
        </div>

        <div class="mb-3">
            <label for="contrasena" class="form-label-custom">Contraseña</label>
            <div class="input-group input-group-custom">
                <input type="password" class="form-control form-control-custom" id="contrasena" name="contrasena"
                       placeholder="Ej: sakamoto123" required>
                <button class="btn btn-outline-secondary" type="button" onclick="togglePassword('contrasena')">
                    Mostrar
                </button>
            </div>
        </div>

        <button type="submit" class="btn btn-primary-custom w-100">
            Ingresar
        </button>
    </form>

    <!-- Footer -->
    <div class="footer-links">
        <a href="recuperarContra.jsp" class="d-block mb-2 small">¿Olvidaste tu contraseña?</a>
        <p class="text-muted small mb-0">
            ¿No tienes cuenta? <a href="registro.jsp">Regístrate aquí</a>
        </p>
    </div>
</div>

<script src="assets/js/scripts.js"></script>
<script>
    function togglePassword(inputId) {
        const input = document.getElementById(inputId);
        const btn = event.target;
        if (input.type === 'password') {
            input.type = 'text';
            btn.textContent = 'Ocultar';
        } else {
            input.type = 'password';
            btn.textContent = 'Mostrar';
        }
    }
</script>

</body>
</html>