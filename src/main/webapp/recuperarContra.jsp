<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Click & Drive - Recuperar contraseña</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/styles.css" rel="stylesheet">
</head>
<body>

<div class="card-auth" style="margin:40px auto;">

    <!-- Logo -->
    <div class="text-center mb-4">
        <img src="assets/images/logo.png"
             alt="Click & Drive"
             class="logo-img"
             style="max-width:180px;height:auto;">

        <p class="logo-subtitle">
            Tu agencia de confianza
        </p>
    </div>

    <!-- Título -->
    <h5 class="section-title">
        Recuperar contraseña
    </h5>

    <p class="section-subtitle">
        Escribe el correo con el que registraste tu cuenta.
        Te enviaremos un código de verificación para continuar con el proceso.
    </p>

    <!-- Mensajes -->

    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");

        if(error != null){
    %>

    <div class="alert alert-danger">
        <%= error %>
    </div>

    <%
        }

        if(success != null){
    %>

    <div class="alert alert-success">
        <%= success %>
    </div>

    <%
        }
    %>

    <!-- Formulario -->

    <form action="UsuarioServlet" method="post">
        <input type="hidden" name="action" value="recuperarContrasena">

        <div class="mb-3">

            <label class="form-label-custom">
                Correo electrónico
            </label>

            <input
                    type="email"
                    class="form-control form-control-custom"
                    name="correo"
                    id="correo"
                    placeholder="Ej: usuario@correo.com"
                    required>

        </div>

        <button
                type="submit"
                class="btn btn-primary-custom w-100">

            Enviar código

        </button>

    </form>

    <hr>

    <div class="footer-links text-center">

        <a href="${pageContext.request.contextPath}/login.jsp"
           class="small">

            ← Volver al inicio de sesión

        </a>

    </div>

</div>

<script src="assets/js/scripts.js"></script>

</body>
</html>