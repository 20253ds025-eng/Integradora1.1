package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.model.dao.AutomovilDAO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@WebServlet("/AutoServlet")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,      // 5MB
        maxRequestSize = 10 * 1024 * 1024
)
public class AutoServlet extends HttpServlet {

    private AutomovilDAO autoDAO;

    @Override
    public void init() {
        autoDAO = new AutomovilDAO();
    }

    // ==========================================
    // DO GET
    // ==========================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            // Listar autos disponibles
            List<AutomovilDTO> autos = autoDAO.getDisponibles();
            req.setAttribute("autos", autos);
            req.getRequestDispatcher("autos.jsp").forward(req, resp);
        } else if ("ver".equals(action)) {
            String matricula = req.getParameter("matricula");
            AutomovilDTO auto = autoDAO.getById(matricula);
            req.setAttribute("auto", auto);
            req.getRequestDispatcher("detalle-auto.jsp").forward(req, resp);
        }
    }

    // ==========================================
    // DO POST
    // ==========================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("registrar".equals(action)) {
            registrarAuto(req, resp);
        } else {
            resp.sendRedirect("AutoServlet");
        }
    }

    // ==========================================
    // REGISTRAR AUTO (solo Dueño) - con imagen
    // ==========================================
    private void registrarAuto(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // --- 1. Leer campos de texto del formulario ---
        String matricula = req.getParameter("matricula");
        String numeroSerie = req.getParameter("numeroSerie");
        String marca = req.getParameter("marca");
        String modelo = req.getParameter("modelo");
        String descripcion = req.getParameter("descripcion");
        String tipoOrigen = req.getParameter("tipoOrigen");

        int anio;
        double precio;
        try {
            anio = Integer.parseInt(req.getParameter("anio"));
            precio = Double.parseDouble(req.getParameter("precio"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("AutoServlet?error=Anio o precio invalidos");
            return;
        }

        // Validar que no exista ya esa matricula o numero de serie
        if (autoDAO.existeMatricula(matricula) || autoDAO.existeNumeroSerie(numeroSerie)) {
            resp.sendRedirect("AutoServlet?error=La matricula o numero de serie ya existen");
            return;
        }

        // --- 2. Procesar la imagen subida ---
        String nombreArchivo = "sin_imagen.png"; // valor por defecto si no suben nada
        Part filePart = req.getPart("foto");

        if (filePart != null && filePart.getSize() > 0) {
            String nombreOriginal = filePart.getSubmittedFileName();

            if (nombreOriginal != null && nombreOriginal.contains(".")) {
                String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
                nombreArchivo = UUID.randomUUID().toString() + extension;

                String rutaCarpeta = getServletContext().getRealPath("/assets/images/");
                String rutaCompleta = rutaCarpeta + File.separator + nombreArchivo;

                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, Paths.get(rutaCompleta), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

        // --- 3. Armar el DTO (una sola vez, con todos los campos) ---
        AutomovilDTO auto = new AutomovilDTO();
        auto.setMatricula(matricula);
        auto.setNumeroSerie(numeroSerie);
        auto.setMarca(marca);
        auto.setModelo(modelo);
        auto.setAnio(anio);
        auto.setTipoOrigen(tipoOrigen);
        auto.setPrecio(precio);
        auto.setDescripcion(descripcion);
        auto.setImagen(nombreArchivo);

        // --- 4. Guardar en la base de datos ---
        boolean creado = autoDAO.create(auto);

        if (creado) {
            resp.sendRedirect("AutoServlet?mensaje=Auto registrado correctamente");
        } else {
            resp.sendRedirect("AutoServlet?error=Error al registrar auto");
        }
    }
}