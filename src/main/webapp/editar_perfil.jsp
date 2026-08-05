<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Editar Perfil - Click & Drive</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,600;0,700;1,400&display=swap" rel="stylesheet">
  <style>
    body {
      background-color: #ffffff;
    }
    
    .form-control:focus {
        border-color: #001f4c;
        box-shadow: 0 0 0 0.25rem rgba(0, 31, 76, 0.25);
    }
    
    .password-toggle {
        cursor: pointer;
    }
    
    .success-icon-container {
        width: 120px;
        height: 120px;
        border: 6px solid #28a745;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
    }
    
    .success-icon-container i {
        font-size: 5rem;
        color: #28a745;
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
    String nombreSolo = partesNombre.length > 0 ? partesNombre[0] : "";
    if (partesNombre.length > 2) {
        nombreSolo = partesNombre[0] + " " + partesNombre[1];
    }
    String apellidoP = partesNombre.length > 1 ? partesNombre[partesNombre.length - 2] : "";
    if (partesNombre.length == 2) {
        apellidoP = partesNombre[1];
    }
    String apellidoM = partesNombre.length > 2 ? partesNombre[partesNombre.length - 1] : "";
%>

<main class="container py-4 position-relative" style="max-width: 800px; margin: 0 auto;">

  <!-- Botón regresar alineado arriba a la izquierda del contenedor -->
  <div class="position-absolute" style="top: 1rem; left: 1rem;">
    <a href="${pageContext.request.contextPath}/perfil.jsp" class="text-dark text-decoration-none">
      <i class="bi bi-arrow-left fs-3"></i>
    </a>
  </div>

  <div class="px-md-5 pt-5 pb-3">
    <h2 class="text-center font-serif fw-bold mb-5" style="font-family: 'Playfair Display', serif; color: #1a1a1a; letter-spacing: 1px;">EDITAR PERFIL</h2>
    
    <form id="formEditarPerfil" onsubmit="guardarCambios(event)">
      <!-- INFORMACIÓN PERSONAL -->
      <h6 class="font-serif fw-bold mb-3" style="font-family: 'Playfair Display', serif; color: #333;">INFORMACIÓN PERSONAL</h6>
      
      <div class="row g-4 mb-3">
        <div class="col-md-6">
          <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Nombre (s):</label>
          <input type="text" class="form-control font-sans" name="nombre" value="<%= nombreSolo %>" required>
        </div>
        <div class="col-md-6">
          <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Apellido Paterno:</label>
          <input type="text" class="form-control font-sans" name="apellidoP" value="<%= apellidoP %>" required>
        </div>
      </div>
      
      <div class="row g-4 mb-5">
        <div class="col-md-6">
          <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Correo electronico:</label>
          <input type="email" class="form-control font-sans" name="correo" value="<%= correoUsuario %>" required>
        </div>
        <div class="col-md-6">
          <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Apellido Materno:</label>
          <input type="text" class="form-control font-sans" name="apellidoM" value="<%= apellidoM %>" required>
        </div>
      </div>

      <!-- CAMBIAR CONTRASEÑA -->
      <h6 class="font-serif fw-bold mb-3" style="font-family: 'Playfair Display', serif; color: #333;">CAMBIAR CONTRASEÑA</h6>
      
      <div class="mb-4">
        <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Contraseña actual:</label>
        <div class="position-relative">
          <input type="password" class="form-control font-sans pe-5" placeholder="**********" id="passActual">
          <i class="bi bi-eye-slash text-muted position-absolute top-50 end-0 translate-middle-y me-3 fs-5 password-toggle" onclick="togglePassword('passActual', this)"></i>
        </div>
      </div>
      
      <div class="row g-4 mb-5">
        <div class="col-md-6">
          <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Nueva contraseña:</label>
          <div class="position-relative">
            <input type="password" class="form-control font-sans pe-5" id="passNueva">
            <i class="bi bi-eye-slash text-muted position-absolute top-50 end-0 translate-middle-y me-3 fs-5 password-toggle" onclick="togglePassword('passNueva', this)"></i>
          </div>
        </div>
        <div class="col-md-6">
          <label class="form-label font-serif fw-bold" style="font-family: 'Playfair Display', serif; font-size: 0.9rem;">Confirma nueva contraseña:</label>
          <div class="position-relative">
            <input type="password" class="form-control font-sans pe-5" id="passConfirma">
            <i class="bi bi-eye-slash text-muted position-absolute top-50 end-0 translate-middle-y me-3 fs-5 password-toggle" onclick="togglePassword('passConfirma', this)"></i>
          </div>
        </div>
      </div>
      
      <!-- BOTÓN GUARDAR -->
      <div class="text-center mt-2">
        <button type="submit" class="btn text-white px-5 py-2 font-sans rounded-1" style="background-color: #001f4c;">
          Guardar cambios
        </button>
      </div>
    </form>
    
  </div>
</main>

<!-- MODAL DE ÉXITO -->
<div class="modal fade" id="modalExito" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 450px;">
    <div class="modal-content border-0 shadow-lg rounded-4 text-center p-4">
      <div class="modal-body p-4">
        
        <div class="success-icon-container mb-4">
            <i class="bi bi-check"></i>
        </div>
        
        <h4 class="font-serif fw-normal text-dark mb-4" style="font-family: 'Playfair Display', serif; font-size: 1.6rem;">
          ¡Operación realizada correctamente!
        </h4>
        
        <div class="mt-4">
            <button type="button" class="btn text-white px-5 py-2 rounded-1 font-sans shadow-sm" style="background-color: #001f4c;" onclick="irAlPerfil()">
              Aceptar
            </button>
        </div>
        
      </div>
    </div>
  </div>
</div>

<jsp:include page="/assets/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script>
    function togglePassword(inputId, iconElement) {
        const input = document.getElementById(inputId);
        if (input.type === 'password') {
            input.type = 'text';
            iconElement.classList.remove('bi-eye-slash');
            iconElement.classList.add('bi-eye');
        } else {
            input.type = 'password';
            iconElement.classList.remove('bi-eye');
            iconElement.classList.add('bi-eye-slash');
        }
    }
    
    async function guardarCambios(e) {
        e.preventDefault();
        
        const passNueva = document.getElementById('passNueva').value;
        const passConfirma = document.getElementById('passConfirma').value;
        
        if(passNueva !== '' && passNueva !== passConfirma) {
            alert('Las nuevas contraseñas no coinciden.');
            return;
        }

        const passActual = document.getElementById('passActual').value;
        
        const form = document.getElementById('formEditarPerfil');
        const formData = new FormData(form);
        
        // First save profile
        const profileData = new URLSearchParams(formData);
        profileData.append('action', 'actualizarPerfil');
        
        try {
            const resp = await fetch('${pageContext.request.contextPath}/UsuarioServlet', {
                method: 'POST',
                body: profileData
            });
            const result = await resp.json();
            
            if (result.error) {
                alert('Error: ' + result.error);
                return;
            }
            
            // If password change requested, do it next
            if (passNueva !== '' && passActual !== '') {
                const passData = new URLSearchParams();
                passData.append('action', 'cambiarContrasena');
                passData.append('idUsuario', '<%= session.getAttribute("usuario") %>');
                passData.append('nuevaContrasena', passNueva);
                
                const passResp = await fetch('${pageContext.request.contextPath}/UsuarioServlet', {
                    method: 'POST',
                    body: passData
                });
                // Password change invalidates session, redirect to login
                window.location.href = '${pageContext.request.contextPath}/login.jsp?msg=password_changed';
                return;
            }
            
            const modal = new bootstrap.Modal(document.getElementById('modalExito'));
            modal.show();
        } catch(err) {
            alert('Error al guardar los cambios');
        }
    }
    
    function irAlPerfil() {
        window.location.href = '${pageContext.request.contextPath}/perfil.jsp';
    }
</script>
</body>
</html>
