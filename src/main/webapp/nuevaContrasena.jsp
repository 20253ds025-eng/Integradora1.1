<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Click & Drive - Nueva contraseña</title>

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
        Crear nueva contraseña
    </h5>

    <p class="section-subtitle">
        Tu identidad fue verificada correctamente.
        Ahora crea una nueva contraseña para tu cuenta.
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

    <form action="UsuarioServlet" method="post">

        <input type="hidden"
               name="action"
               value="actualizarContrasena">

        <div class="mb-3">

            <label class="form-label-custom">
                Nueva contraseña
            </label>

            <div class="input-group">

                <input
                        type="password"
                        class="form-control form-control-custom"
                        id="password"
                        name="password"
                        minlength="8"
                        required>

                <button
                        class="btn btn-outline-secondary"
                        type="button"
                        onclick="mostrarPassword('password',this)">

                    Mostrar

                </button>

            </div>

        </div>

        <div class="mb-4">

            <label class="form-label-custom">
                Confirmar contraseña
            </label>

            <div class="input-group">

                <input
                        type="password"
                        class="form-control form-control-custom"
                        id="confirmar"
                        name="confirmar"
                        minlength="8"
                        required>

                <button
                        class="btn btn-outline-secondary"
                        type="button"
                        onclick="mostrarPassword('confirmar',this)">

                    Mostrar

                </button>

            </div>

        </div>

        <button
                class="btn btn-primary-custom w-100"
                type="submit">

            Cambiar contraseña

        </button>

    </form>

    <hr>

    <div class="footer-links text-center">

        <a href="${pageContext.request.contextPath}/login.jsp">

            Cancelar

        </a>

    </div>

</div>

<script>

function mostrarPassword(id,btn){

    const input=document.getElementById(id);

    if(input.type==="password"){

        input.type="text";
        btn.innerHTML="Ocultar";

    }else{

        input.type="password";
        btn.innerHTML="Mostrar";

    }

}

document.querySelector("form").addEventListener("submit",function(e){

    let p1=document.getElementById("password").value;
    let p2=document.getElementById("confirmar").value;

    if(p1!==p2){

        alert("Las contraseñas no coinciden.");

        e.preventDefault();

    }

});

</script>

</body>

</html>