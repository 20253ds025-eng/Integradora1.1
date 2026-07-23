<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Click & Drive - Registro</title>
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/styles.css" rel="stylesheet">
</head>
<body>

<div class="card-auth card-auth-lg" style="margin: 40px auto;">
    <!-- Logo -->
    <div class="text-center mb-3">
        <img src="assets/images/logo.png" alt="Click & Drive" class="logo-img" style="max-width: 180px; height: auto;">
        <p class="logo-subtitle logo-subtitle-sm mt-1">Regístrate como cliente</p>
    </div>

    <!-- Título -->
    <h5 class="section-title section-title-sm">Registro</h5>
    <p class="section-subtitle section-subtitle-sm">Crea tu cuenta para empezar a comprar</p>

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
    <form action="RegistroServlet" method="post" id="registroForm">
        <div class="row g-3">
            <div class="col-md-6">
                <label for="nombre" class="form-label-custom">Nombre</label>
                <input type="text" class="form-control form-control-custom form-control-custom-sm"
                       id="nombre" name="nombre" placeholder="Carlos" required>
            </div>
            <div class="col-md-6">
                <label for="apellidoPaterno" class="form-label-custom">Apellido Paterno</label>
                <input type="text" class="form-control form-control-custom form-control-custom-sm"
                       id="apellidoPaterno" name="apellidoPaterno" placeholder="García" required>
            </div>
            <div class="col-md-6">
                <label for="apellidoMaterno" class="form-label-custom">Apellido Materno</label>
                <input type="text" class="form-control form-control-custom form-control-custom-sm"
                       id="apellidoMaterno" name="apellidoMaterno" placeholder="López">
            </div>
            <div class="col-md-6">
                <label for="correo" class="form-label-custom">Correo electrónico</label>
                <input type="email" class="form-control form-control-custom form-control-custom-sm"
                       id="correo" name="correo" placeholder="correo@email.com" required>
            </div>
            <div class="col-12">
                <label for="contrasena" class="form-label-custom">Contraseña</label>
                <div class="input-group input-group-custom">
                    <input type="password" class="form-control form-control-custom form-control-custom-sm"
                           id="contrasena" name="contrasena" placeholder="Ingresa tu contraseña" required>
                    <button class="btn btn-outline-secondary" type="button" onclick="togglePassword('contrasena')">
                        Mostrar
                    </button>
                </div>
                <div class="password-hint mt-1">
                    <span id="reqLength">- 8 caracteres</span>
                    <span id="reqUpper" class="ms-2">- Mayúscula</span>
                    <span id="reqLower" class="ms-2">- Minúscula</span>
                    <span id="reqNumber" class="ms-2">- Número</span>
                </div>
            </div>
            <div class="col-12">
                <label for="confirmarContrasena" class="form-label-custom">Confirmar contraseña</label>
                <div class="input-group input-group-custom">
                    <input type="password" class="form-control form-control-custom form-control-custom-sm"
                           id="confirmarContrasena" name="confirmarContrasena" placeholder="Confirma tu contraseña" required>
                    <button class="btn btn-outline-secondary" type="button" onclick="togglePassword('confirmarContrasena')">
                        Mostrar
                    </button>
                </div>
            </div>
        </div>

        <button type="submit" class="btn btn-primary-custom btn-primary-custom-sm w-100 mt-3">
            Registrarme
        </button>
    </form>

    <!-- Footer -->
    <div class="footer-links">
        <p class="text-muted small mb-0">
            ¿Ya tienes cuenta? <a href="login.jsp">Inicia Sesión</a>
        </p>
        <p class="text-muted small mt-1" style="font-size: 11px;">Tus datos están seguros</p>
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

    // Validación de contraseña
    document.getElementById('contrasena').addEventListener('input', function() {
        const p = this.value;
        const reqs = [
            { id: 'reqLength', test: p.length >= 8 },
            { id: 'reqUpper', test: /[A-Z]/.test(p) },
            { id: 'reqLower', test: /[a-z]/.test(p) },
            { id: 'reqNumber', test: /[0-9]/.test(p) }
        ];
        reqs.forEach(function(req) {
            const el = document.getElementById(req.id);
            if (el) {
                el.textContent = (req.test ? '[OK]' : '[ ]') + ' ' + el.textContent.substring(4);
                el.style.color = req.test ? '#16a34a' : '#6b7280';
            }
        });
    });

    document.getElementById('registroForm').addEventListener('submit', function(e) {
        const p = document.getElementById('contrasena').value;
        const c = document.getElementById('confirmarContrasena').value;
        if (p !== c) {
            e.preventDefault();
            alert('Las contraseñas no coinciden.');
            return false;
        }
        if (p.length < 8) {
            e.preventDefault();
            alert('La contraseña debe tener al menos 8 caracteres.');
            return false;
        }
    });
</script>

</body>
</html>