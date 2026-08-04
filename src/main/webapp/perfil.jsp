<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Mi Perfil - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
  <style>
    body {
      background-color: #f4f4f4;
    }
  </style>
</head>
<body>

<!-- MÓDULOS GLOBALES -->
<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<%
    String nombreUsuario = (String) session.getAttribute("nombre");
    String correoUsuario = (String) session.getAttribute("correo");
    
    if (nombreUsuario == null) {
        nombreUsuario = "Carlos Alberto Mendoza Ruiz";
    }
    if (correoUsuario == null) {
        correoUsuario = "carlosalberto@gmail.com";
    }
    
    String[] partesNombre = nombreUsuario.trim().split(" ");
    String iniciales = "";
    if (partesNombre.length >= 2) {
        iniciales = partesNombre[0].substring(0,1).toUpperCase() + "." + partesNombre[1].substring(0,1).toUpperCase();
    } else if (partesNombre.length == 1 && partesNombre[0].length() > 0) {
        iniciales = partesNombre[0].substring(0,1).toUpperCase();
    }
%>

<main class="container py-5 d-flex justify-content-center align-items-center" style="min-height: 70vh;">

  <div class="card border-0 shadow-sm rounded-4 p-5" style="max-width: 600px; width: 100%; border: 1px solid #eaeaea!important;">
    <div class="row align-items-center">
      
      <!-- Círculo Iniciales -->
      <div class="col-md-5 d-flex justify-content-center mb-4 mb-md-0">
        <div class="rounded-circle d-flex justify-content-center align-items-center bg-light" style="width: 200px; height: 200px; border: 1px solid #ddd; background-color: #d8d8d8!important;">
          <span class="fw-normal" style="font-size: 5rem; color: #000; font-family: sans-serif;"><%= iniciales %></span>
        </div>
      </div>
      
      <!-- Info y Botones -->
      <div class="col-md-7 ps-md-4">
        <div class="mb-3">
          <h5 class="fw-bold font-serif mb-0 text-dark" style="font-family: 'Playfair Display', serif;">Nombre:</h5>
          <p class="font-serif fs-5 mb-0" style="font-family: 'Playfair Display', serif; color: #333;"><%= nombreUsuario %></p>
        </div>
        
        <div class="mb-4">
          <h5 class="fw-bold font-serif mb-0 text-dark" style="font-family: 'Playfair Display', serif;">Correo:</h5>
          <p class="font-serif fs-5 mb-0" style="font-family: 'Playfair Display', serif; color: #333;"><%= correoUsuario %></p>
        </div>
        
        <div class="d-flex gap-2 w-100 mt-4">
          <a href="${pageContext.request.contextPath}/index_cliente.jsp" class="btn text-white w-50 py-2 rounded-1 font-sans shadow-sm" style="background-color: #001f4c; font-size: 0.9rem;">
            Ir al inicio
          </a>
          <a href="${pageContext.request.contextPath}/editar_perfil.jsp" class="btn text-white w-50 py-2 rounded-1 font-sans shadow-sm" style="background-color: #5c6c84; font-size: 0.9rem;">
            Editar informacion
          </a>
        </div>
      </div>
      
    </div>
  </div>

</main>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>
