package mx.edu.utez.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.model.dao.AutomovilDAO;

import java.io.IOException;

/**
 * Permite que un Comprador registre un vehículo propio que NO fue comprado
 * en la agencia (tipo_origen = 'Externo'), únicamente para poder contratarle
 * servicios de mantenimiento. Solo se piden los datos básicos del vehículo,
 * sin fotografía (a diferencia del registro de autos de Agencia, que hace
 * el Dueño desde AutoServlet).
 */
@WebServlet(name = "RegistrarAutoExternoServlet", value = "/RegistrarAutoExterno")
public class RegistrarAutoExternoServlet extends HttpServlet {

    private AutomovilDAO autoDAO;

    @Override
    public void init() {
        autoDAO = new AutomovilDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si venimos desde el detalle de un servicio, propagamos el id para
        // regresar directo ahí después de registrar el auto.
        request.getRequestDispatcher("/registrar_auto_externo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer idCliente = (session != null) ? (Integer) session.getAttribute("usuario") : null;
        if (idCliente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String numeroSerie = request.getParameter("numeroSerie");
        String anioParam = request.getParameter("anio");
        String idServicioOrigen = request.getParameter("idServicioOrigen"); // opcional, para regresar al detalle

        if (marca == null || marca.isBlank() || modelo == null || modelo.isBlank()
                || numeroSerie == null || numeroSerie.isBlank() || anioParam == null || anioParam.isBlank()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/registrar_auto_externo.jsp").forward(request, response);
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El año no es válido.");
            request.getRequestDispatcher("/registrar_auto_externo.jsp").forward(request, response);
            return;
        }

        if (autoDAO.existeNumeroSerie(numeroSerie)) {
            request.setAttribute("error", "Ya existe un vehículo registrado con ese número de serie.");
            request.getRequestDispatcher("/registrar_auto_externo.jsp").forward(request, response);
            return;
        }

        // La matrícula (identificador interno) la genera el sistema; el
        // cliente jamás la escribe a mano, ya que debe respetar el formato
        // AUE-XXX exigido por la restricción de la base de datos.
        String matricula = autoDAO.generarSiguienteMatriculaExterno();
        if (matricula == null) {
            request.setAttribute("error", "No se pudo generar el identificador del vehículo. Intenta de nuevo.");
            request.getRequestDispatcher("/registrar_auto_externo.jsp").forward(request, response);
            return;
        }

        AutomovilDTO auto = new AutomovilDTO();
        auto.setMatricula(matricula);
        auto.setNumeroSerie(numeroSerie);
        auto.setMarca(marca);
        auto.setModelo(modelo);
        auto.setAnio(anio);
        auto.setTipoOrigen("Externo");
        auto.setPrecio(0); // No aplica: un auto Externo no está en venta
        auto.setDescripcion("Vehículo externo registrado por el cliente para contratación de servicios.");
        auto.setImagen("sin_imagen.png");
        auto.setIdClientePropietario(idCliente);

        boolean creado = autoDAO.create(auto);
        if (!creado) {
            request.setAttribute("error", "Ocurrió un error al registrar tu vehículo. Intenta de nuevo.");
            request.getRequestDispatcher("/registrar_auto_externo.jsp").forward(request, response);
            return;
        }

        // Si veníamos de un servicio específico, regresamos directo ahí con
        // este auto ya preseleccionado.
        if (idServicioOrigen != null && !idServicioOrigen.isBlank()) {
            response.sendRedirect(request.getContextPath()
                    + "/DetalleServicioServlet?id=" + idServicioOrigen + "&matriculaAuto=" + matricula);
        } else {
            response.sendRedirect(request.getContextPath() + "/mis_vehiculos.jsp?exito=1");
        }
    }
}
