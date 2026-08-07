package mx.edu.utez.demo.controller;

import mx.edu.utez.demo.model.dao.*;
import mx.edu.utez.demo.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/MisVehiculosServlet")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class MisVehiculosServlet extends HttpServlet {

    private AutomovilDAO autoDAO;

    @Override
    public void init() {
        autoDAO = new AutomovilDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"No hay sesion activa\"}");
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            Integer idCliente = (Integer) session.getAttribute("usuario");
            // Obtener autos de agencia comprados por el cliente
            List<AutomovilDTO> autosAgencia = autoDAO.getVehiculosDeCliente(idCliente);
            // Obtener autos externos del cliente
            List<AutomovilDTO> autosExternos = autoDAO.getExternosPorCliente(idCliente);

            StringBuilder sb = new StringBuilder();
            sb.append("{\"agencia\":[");

            boolean first = true;
            for (AutomovilDTO a : autosAgencia) {
                if (!first) sb.append(",");
                first = false;
                sb.append("{");
                sb.append("\"matricula\":\"").append(escapeJson(a.getMatricula())).append("\",");
                sb.append("\"marca\":\"").append(escapeJson(a.getMarca())).append("\",");
                sb.append("\"modelo\":\"").append(escapeJson(a.getModelo())).append("\",");
                sb.append("\"anio\":").append(a.getAnio()).append(",");
                sb.append("\"precio\":").append(a.getPrecio()).append(",");
                sb.append("\"imagen\":\"").append(escapeJson(a.getImagen())).append("\"");
                sb.append("}");
            }

            sb.append("],\"externo\":[");

            first = true;
            for (AutomovilDTO a : autosExternos) {
                if (!first) sb.append(",");
                first = false;
                sb.append("{");
                sb.append("\"matricula\":\"").append(escapeJson(a.getMatricula())).append("\",");
                sb.append("\"marca\":\"").append(escapeJson(a.getMarca())).append("\",");
                sb.append("\"modelo\":\"").append(escapeJson(a.getModelo())).append("\",");
                sb.append("\"anio\":").append(a.getAnio()).append(",");
                sb.append("\"precio\":").append(a.getPrecio()).append(",");
                sb.append("\"numeroSerie\":\"").append(escapeJson(a.getNumeroSerie())).append("\"");
                sb.append("}");
            }

            sb.append("]}");
            out.write(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"error\":\"Error al cargar los vehiculos\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"No hay sesion activa\"}");
            return;
        }

        String action = req.getParameter("action");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            if ("registrar".equals(action)) {
                String matricula = req.getParameter("matricula");
                String numSerie = req.getParameter("numeroSerie");
                String marca = req.getParameter("marca");
                String modelo = req.getParameter("modelo");
                String descripcion = req.getParameter("descripcion");

                if (matricula == null || matricula.trim().isEmpty() ||
                    marca == null || marca.trim().isEmpty() ||
                    modelo == null || modelo.trim().isEmpty()) {
                    out.write("{\"error\":\"Faltan campos obligatorios (marca, modelo, matricula)\"}");
                    return;
                }

                if (!matricula.toUpperCase().matches("^AUE-[0-9]{3}$")) {
                    out.write("{\"error\":\"La matricula debe tener formato AUE-XXX (ej: AUE-001)\"}");
                    return;
                }

                int anio = 0;
                double precio = 0.0;
                try {
                    String anioStr = req.getParameter("anio");
                    anio = (anioStr != null && !anioStr.isEmpty()) ? Integer.parseInt(anioStr) : 0;
                } catch (NumberFormatException nfe) {
                    out.write("{\"error\":\"Anio con formato invalido\"}");
                    return;
                }

                if (autoDAO.existeMatricula(matricula)) {
                    out.write("{\"error\":\"La matricula ya esta registrada\"}");
                    return;
                }
                if (autoDAO.existeNumeroSerie(numSerie)) {
                    out.write("{\"error\":\"El numero de serie ya esta registrado\"}");
                    return;
                }

                // Manejar imagen opcional
                String imagen = "VKjetta.jpg"; // Imagen por defecto
                try {
                    Part filePart = req.getPart("imagen");
                    if (filePart != null && filePart.getSize() > 0) {
                        String fileName = filePart.getSubmittedFileName();
                        if (fileName != null && !fileName.isEmpty()) {
                            // Guardar en la carpeta de imagenes
                            String uploadPath = req.getServletContext().getRealPath("/assets/images");
                            java.io.File uploadDir = new java.io.File(uploadPath);
                            if (!uploadDir.exists()) uploadDir.mkdirs();
                            filePart.write(uploadPath + java.io.File.separator + fileName);
                            imagen = fileName;
                        }
                    }
                } catch (Exception e) {
                    // Si falla la imagen, usar la default
                }

                AutomovilDTO auto = new AutomovilDTO(matricula, numSerie != null ? numSerie : "", marca, modelo, anio, "Externo", precio, descripcion, imagen);

                if (autoDAO.create(auto)) {
                    out.write("{\"success\":true,\"mensaje\":\"Vehiculo registrado correctamente\"}");
                } else {
                    out.write("{\"error\":\"Error SQL al registrar. Revisa la consola del servidor para ver el detalle.\"}");
                }

            } else if ("eliminar".equals(action)) {
                String matricula = req.getParameter("matricula");
                if (autoDAO.delete(matricula)) {
                    out.write("{\"success\":true,\"mensaje\":\"Vehiculo eliminado\"}");
                } else {
                    out.write("{\"error\":\"No se pudo eliminar el vehiculo\"}");
                }

            } else if ("editar".equals(action)) {
                String matricula = req.getParameter("matricula");
                String marca = req.getParameter("marca");
                String modelo = req.getParameter("modelo");
                String numSerie = req.getParameter("numeroSerie");

                int anio = 0;
                try {
                    String anioStr = req.getParameter("anio");
                    anio = (anioStr != null && !anioStr.isEmpty()) ? Integer.parseInt(anioStr) : 0;
                } catch (NumberFormatException nfe) {
                    out.write("{\"error\":\"Anio con formato invalido\"}");
                    return;
                }

                AutomovilDTO auto = autoDAO.getById(matricula);
                if (auto != null) {
                    auto.setMarca(marca);
                    auto.setModelo(modelo);
                    auto.setAnio(anio);
                    auto.setNumeroSerie(numSerie);
                    if (autoDAO.update(auto)) {
                        out.write("{\"success\":true,\"mensaje\":\"Vehiculo actualizado\"}");
                    } else {
                        out.write("{\"error\":\"Error al actualizar\"}");
                    }
                } else {
                    out.write("{\"error\":\"Vehiculo no encontrado\"}");
                }
            } else {
                out.write("{\"error\":\"Accion no valida\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"error\":\"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
