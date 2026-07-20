<%--
  Created by IntelliJ IDEA.
  User: ujaco
  Date: 20/07/2026
  Time: 12:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>

    <title>Registro</title>

    <link rel="stylesheet" href="css/styles.css">

</head>

<body>

<div class="formulario">

    <h2>Crear Cuenta</h2>

    <form action="RegistroServlet" method="post">

        <label>Nombre</label>

        <input type="text" name="nombre">

        <label>Apellido</label>

        <input type="text" name="apellido">

        <label>Correo</label>

        <input type="email" name="correo">

        <label>Contraseña</label>

        <input type="password" name="password">

        <button type="submit">

            Registrarme

        </button>

    </form>

    <p>

        ¿Ya tienes cuenta?

        <a href="login.jsp">

            Inicia sesión

        </a>

    </p>

</div>

</body>

</html>