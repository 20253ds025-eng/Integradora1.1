<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.demo.model.UsuarioDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Click & Drive - Usuarios</title>

    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>

<%
    String rol = (String) request.getAttribute("rol");
    List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute("usuarios");
    String buscar = (String) request.getAttribute("buscar");
    Integer paginaActual = (Integer) request.getAttribute("paginaActual");
    Integer totalPaginas = (Integer) request.getAttribute("totalPaginas");
    Integer totalRegistros = (Integer) request.getAttribute("totalRegistros");

    boolean esEmpleado = "Empleado".equals(rol);
    String tituloSeccion = esEmpleado ? "Asesores" : "Clientes";
    String etiquetaTotal = esEmpleado ? "Total de empleados" : "Total de clientes";
    String accionRegistro = esEmpleado ? "registrarEmpleado" : "registrarCliente";
    String textoBoton = esEmpleado ? "+ Agregar asesor" : "+ Agregar cliente";
    if (buscar == null) buscar = "";
%>

<jsp:include page="/assets/components/header.jsp" />
<jsp:include page="/assets/components/sidebar.jsp" />

<main class="container py-4" style="max-width: 1140px; margin: 0 auto;">

    <!-- Volver -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/index_cliente.jsp" class="text-decoration-none text-dark d-inline-flex flex-column align-items-center" style="font-size: 0.75rem;">
            <i class="bi bi-arrow-left fs-4"></i>
            Regresar
        </a>
    </div>

    <!-- Título -->
    <h2 class="fw-bold mb-4" style="color: #001E50;"><%= tituloSeccion %></h2>

    <!-- Buscador + botón agregar -->
    <form action="${pageContext.request.contextPath}/UsuarioServlet" method="get" class="d-flex flex-wrap gap-3 align-items-center mb-4">
        <input type="hidden" name="action" value="listar">
        <input type="hidden" name="rol" value="<%= rol %>">

        <div class="search-box flex-grow-1" style="min-width: 250px;">
            <i class="bi bi-search search-icon"></i>
            <input type="text"
                   name="buscar"
                   value="<%= buscar %>"
                   class="form-control form-control-custom"
                   placeholder="Buscar <%= tituloSeccion %> ...">
        </div>

        <a href="${pageContext.request.contextPath}/UsuarioServlet?action=<%= accionRegistro %>"
           class="btn btn-navy px-3 py-2 fw-semibold text-nowrap">
            <%= textoBoton %>
        </a>
    </form>

    <!-- Mensajes -->
    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");
        if (error != null) {
    %>
    <div class="alert alert-danger"><%= error %></div>
    <% } if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>

    <!-- Tabla -->
    <div class="table-responsive rounded-3 overflow-hidden border">
        <table class="table table-hover mb-0 align-middle">
            <thead>
            <tr style="background-color: #001E50; color: #ffffff;">
                <th class="py-3 ps-4">#</th>
                <th class="py-3">Nombre</th>
                <th class="py-3">Correo</th>
                <th class="py-3">Estado</th>
                <th class="py-3 text-center">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (usuarios == null || usuarios.isEmpty()) {
            %>
            <tr>
                <td colspan="5" class="text-center text-muted py-4">No se encontraron resultados.</td>
            </tr>
            <%
            } else {
                int contador = (paginaActual - 1) * 10 + 1;
                for (UsuarioDTO u : usuarios) {
            %>
            <tr>
                <td class="ps-4"><%= contador++ %></td>
                <td><%= u.getNombre() %></td>
                <td><%= u.getCorreo() %></td>
                <td>
                    <% if (u.isActivo()) { %>
                    <span class="badge rounded-pill" style="background-color:#e6f4ea; color:#1e7e34; font-weight:500;">Activo</span>
                    <% } else { %>
                    <span class="badge rounded-pill" style="background-color:#fdecea; color:#c0392b; font-weight:500;">Inactivo</span>
                    <% } %>
                </td>
                <td class="text-center">
                    <!-- Eliminar (desactivar) -->
                    <form action="${pageContext.request.contextPath}/UsuarioServlet" method="post" class="d-inline"
                          onsubmit="return confirm('¿Desactivar a <%= u.getNombre() %>?');">
                        <input type="hidden" name="action" value="eliminarUsuario">
                        <input type="hidden" name="idUsuario" value="<%= u.getIdUsuario() %>">
                        <input type="hidden" name="rol" value="<%= rol %>">
                        <button type="submit" class="btn btn-sm border-0" title="Eliminar">
                            <i class="bi bi-trash3-fill" style="color:#e74c3c;"></i>
                        </button>
                    </form>

                    <!-- Editar -->
                    <a href="${pageContext.request.contextPath}/UsuarioServlet?action=editarUsuario&idUsuario=<%= u.getIdUsuario() %>"
                       class="btn btn-sm border-0" title="Editar">
                        <i class="bi bi-pencil-fill" style="color:#e6a817;"></i>
                    </a>

                    <!-- Reactivar -->
                    <form action="${pageContext.request.contextPath}/UsuarioServlet" method="post" class="d-inline"
                          onsubmit="return confirm('¿Reactivar a <%= u.getNombre() %>?');">
                        <input type="hidden" name="action" value="reactivarUsuario">
                        <input type="hidden" name="idUsuario" value="<%= u.getIdUsuario() %>">
                        <input type="hidden" name="rol" value="<%= rol %>">
                        <button type="submit" class="btn btn-sm border-0" title="Reactivar">
                            <i class="bi bi-arrow-repeat" style="color:#27ae60;"></i>
                        </button>
                    </form>
                </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>

    <!-- Total -->
    <p class="mt-4 mb-2"><%= etiquetaTotal %>: <%= (totalRegistros != null ? totalRegistros : 0) %></p>

    <!-- Paginación -->
    <% if (totalPaginas != null && totalPaginas > 1) { %>
    <nav class="d-flex justify-content-center mt-3">
        <ul class="pagination">
            <li class="page-item <%= (paginaActual <= 1) ? "disabled" : "" %>">
                <a class="page-link" href="${pageContext.request.contextPath}/UsuarioServlet?action=listar&rol=<%= rol %>&buscar=<%= buscar %>&pagina=1">&laquo;</a>
            </li>
            <%
                for (int i = 1; i <= totalPaginas; i++) {
                    String activo = (i == paginaActual) ? "active" : "";
            %>
            <li class="page-item <%= activo %>">
                <a class="page-link" href="${pageContext.request.contextPath}/UsuarioServlet?action=listar&rol=<%= rol %>&buscar=<%= buscar %>&pagina=<%= i %>"
                   style="<%= i == paginaActual ? "background-color:#001E50; border-color:#001E50;" : "" %>">
                    <%= i %>
                </a>
            </li>
            <% } %>
            <li class="page-item <%= (paginaActual >= totalPaginas) ? "disabled" : "" %>">
                <a class="page-link" href="${pageContext.request.contextPath}/UsuarioServlet?action=listar&rol=<%= rol %>&buscar=<%= buscar %>&pagina=<%= totalPaginas %>">&raquo;</a>
            </li>
        </ul>
    </nav>
    <% } %>

</main>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>
