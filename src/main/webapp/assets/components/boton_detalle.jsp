<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Componente reutilizable: Botón de Detalles (Ojito) -->
<a href="${pageContext.request.contextPath}/DetalleAutoServlet?id=${param.matricula}"
   class="btn btn-navy btn-sm rounded-2 d-flex align-items-center justify-content-center"
   style="width: 32px; height: 32px;" title="Ver detalles">
  <i class="bi bi-eye-fill"></i>
</a>