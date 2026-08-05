<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.demo.utils.SQLConnector" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Diagnostico BD - Click & Drive</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <style>
    body { background: #f4f6f9; font-family: monospace; }
    .table-section { margin-bottom: 30px; }
    .table-name { background: #1a4f76; color: white; padding: 8px 16px; border-radius: 6px 6px 0 0; font-size: 1.1rem; }
    .row-count { background: #28a745; color: white; padding: 2px 10px; border-radius: 12px; font-size: 0.8rem; margin-left: 10px; }
    pre { background: #1e1e1e; color: #d4d4d4; padding: 15px; border-radius: 8px; max-height: 400px; overflow: auto; font-size: 0.85rem; }
    .error { color: #dc3545; font-weight: bold; }
    .success { color: #28a745; }
  </style>
</head>
<body>
<div class="container py-4">
  <h2 class="mb-1">Diagnostico de Base de Datos</h2>
  <p class="text-muted mb-4">Oracle Cloud - Click & Drive</p>

  <% try {
    Connection con = SQLConnector.getConnection();
    DatabaseMetaData meta = con.getMetaData();

    // Listar todas las tablas
    ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
    java.util.List<String> tableNames = new java.util.ArrayList<>();
    while (tables.next()) {
      tableNames.add(tables.getString("TABLE_NAME"));
    }

    out.println("<p class='success'>Conexion exitosa. Tablas encontradas: " + tableNames.size() + "</p>");

    for (String tableName : tableNames) {
      out.println("<div class='table-section'>");
      out.println("<div class='table-name'>" + tableName + "</div>");

      // Contar registros
      Statement countStmt = con.createStatement();
      ResultSet countRs = countStmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
      int rowCount = 0;
      if (countRs.next()) rowCount = countRs.getInt(1);
      out.println("<span class='row-count'>" + rowCount + " registros</span>");

      // Obtener columnas
      ResultSet cols = meta.getColumns(null, null, tableName, "%");
      java.util.List<String> colNames = new java.util.ArrayList<>();
      while (cols.next()) {
        colNames.add(cols.getString("COLUMN_NAME"));
      }

      // Obtener datos (max 50 filas)
      Statement dataStmt = con.createStatement();
      ResultSet dataRs = dataStmt.executeQuery("SELECT * FROM " + tableName + " WHERE ROWNUM <= 50");

      out.println("<pre>");
      out.println("Columnas: " + String.join(" | ", colNames));
      out.println("---------------------------------------------------");

      int rowNum = 0;
      while (dataRs.next()) {
        rowNum++;
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= colNames.size(); i++) {
          String val = dataRs.getString(i);
          if (val == null) val = "NULL";
          if (val.length() > 40) val = val.substring(0, 40) + "...";
          row.append(colNames.get(i-1)).append("=").append(val);
          if (i < colNames.size()) row.append(" | ");
        }
        out.println("[" + rowNum + "] " + row.toString());
      }
      if (rowNum == 0) out.println("(sin datos)");
      out.println("</pre>");
      out.println("</div>");
    }

    con.close();
  } catch (Exception e) {
    out.println("<p class='error'>Error de conexion: " + e.getMessage() + "</p>");
    e.printStackTrace(new java.io.PrintWriter(out));
  } %>

  <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-navy mt-4">Volver al inicio</a>
</div>
</body>
</html>
