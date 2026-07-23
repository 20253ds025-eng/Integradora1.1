package mx.edu.utez.demo.model.dao;

import mx.edu.utez.demo.model.ContratacionDTO;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratacionDAO implements Dao<ContratacionDTO, Integer> {

    // ==========================================
    // CREATE
    // ==========================================
    @Override
    public boolean create(ContratacionDTO contratacion) {
        String sql = "INSERT INTO Contrataciones_Servicios (id_venta, id_cliente, id_servicio, matricula_auto, "
                + "costo_aplicado, fecha_vigencia_inicio, fecha_vigencia_fin, estatus_servicio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Solo 8 parámetros, no 22
            ps.setObject(1, contratacion.getIdVenta() > 0 ? contratacion.getIdVenta() : null);
            ps.setInt(2, contratacion.getIdCliente());
            ps.setInt(3, contratacion.getIdServicio());
            ps.setString(4, contratacion.getMatriculaAuto());
            ps.setDouble(5, contratacion.getCostoAplicado());
            ps.setDate(6, contratacion.getFechaVigenciaInicio());
            ps.setDate(7, contratacion.getFechaVigenciaFin());
            ps.setString(8, contratacion.getEstatusServicio());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    contratacion.setIdContratacion(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // READ - OBTENER TODOS
    // ==========================================
    @Override
    public List<ContratacionDTO> getAll() {
        List<ContratacionDTO> lista = new ArrayList<>();
        String sql = "SELECT c.*, s.nombre_servicio, s.tipo_aplicacion, a.marca, a.modelo "
                + "FROM Contrataciones_Servicios c "
                + "JOIN Servicios s ON c.id_servicio = s.id_servicio "
                + "JOIN Automoviles a ON c.matricula_auto = a.matricula "
                + "ORDER BY c.fecha_contratacion DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // READ - OBTENER POR ID
    // ==========================================
    @Override
    public ContratacionDTO getById(Integer id) {
        String sql = "SELECT c.*, s.nombre_servicio, s.tipo_aplicacion, a.marca, a.modelo "
                + "FROM Contrataciones_Servicios c "
                + "JOIN Servicios s ON c.id_servicio = s.id_servicio "
                + "JOIN Automoviles a ON c.matricula_auto = a.matricula "
                + "WHERE c.id_contratacion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================
    // READ - OBTENER POR CLIENTE
    // ==========================================
    public List<ContratacionDTO> getByCliente(int idCliente) {
        List<ContratacionDTO> lista = new ArrayList<>();
        String sql = "SELECT c.*, s.nombre_servicio, s.tipo_aplicacion, a.marca, a.modelo "
                + "FROM Contrataciones_Servicios c "
                + "JOIN Servicios s ON c.id_servicio = s.id_servicio "
                + "JOIN Automoviles a ON c.matricula_auto = a.matricula "
                + "WHERE c.id_cliente = ? ORDER BY c.fecha_contratacion DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // READ - OBTENER POR MATRÍCULA
    // ==========================================
    public List<ContratacionDTO> getByMatricula(String matricula) {
        List<ContratacionDTO> lista = new ArrayList<>();
        String sql = "SELECT c.*, s.nombre_servicio, s.tipo_aplicacion, a.marca, a.modelo "
                + "FROM Contrataciones_Servicios c "
                + "JOIN Servicios s ON c.id_servicio = s.id_servicio "
                + "JOIN Automoviles a ON c.matricula_auto = a.matricula "
                + "WHERE c.matricula_auto = ? ORDER BY c.fecha_contratacion DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // READ - OBTENER ACTIVOS POR CLIENTE
    // ==========================================
    public List<ContratacionDTO> getActivosByCliente(int idCliente) {
        List<ContratacionDTO> lista = new ArrayList<>();
        String sql = "SELECT c.*, s.nombre_servicio, s.tipo_aplicacion, a.marca, a.modelo "
                + "FROM Contrataciones_Servicios c "
                + "JOIN Servicios s ON c.id_servicio = s.id_servicio "
                + "JOIN Automoviles a ON c.matricula_auto = a.matricula "
                + "WHERE c.id_cliente = ? AND c.estatus_servicio = 'Activo' "
                + "ORDER BY c.fecha_vigencia_fin ASC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // UPDATE
    // ==========================================
    @Override
    public boolean update(ContratacionDTO contratacion) {
        String sql = "UPDATE Contrataciones_Servicios SET estatus_servicio = ? WHERE id_contratacion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contratacion.getEstatusServicio());
            ps.setInt(2, contratacion.getIdContratacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // DELETE
    // ==========================================
    @Override
    public boolean delete(Integer id) {
        // Las contrataciones no se eliminan (inmutabilidad financiera)
        return false;
    }

    // ==========================================
    // MÉTODOS ADICIONALES
    // ==========================================

    public boolean cancelar(int id) {
        String sql = "UPDATE Contrataciones_Servicios SET estatus_servicio = 'Cancelado' WHERE id_contratacion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activar(int id) {
        String sql = "UPDATE Contrataciones_Servicios SET estatus_servicio = 'Activo' WHERE id_contratacion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // MAPEO DE RESULTADOS
    // ==========================================
    private ContratacionDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ContratacionDTO dto = new ContratacionDTO();
        dto.setIdContratacion(rs.getInt("id_contratacion"));
        dto.setFolio(rs.getString("folio"));
        dto.setIdVenta(rs.getInt("id_venta"));
        dto.setIdCliente(rs.getInt("id_cliente"));
        dto.setIdServicio(rs.getInt("id_servicio"));
        dto.setNombreServicio(rs.getString("nombre_servicio"));
        dto.setTipoAplicacion(rs.getString("tipo_aplicacion"));
        dto.setMatriculaAuto(rs.getString("matricula_auto"));
        dto.setMarca(rs.getString("marca"));
        dto.setModelo(rs.getString("modelo"));
        dto.setCostoAplicado(rs.getDouble("costo_aplicado"));
        dto.setFechaContratacion(rs.getTimestamp("fecha_contratacion"));
        dto.setFechaVigenciaInicio(rs.getDate("fecha_vigencia_inicio"));
        dto.setFechaVigenciaFin(rs.getDate("fecha_vigencia_fin"));
        dto.setEstatusServicio(rs.getString("estatus_servicio"));
        return dto;
    }
}