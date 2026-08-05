package mx.edu.utez.demo.model.dao;
import mx.edu.utez.demo.model.EmpleadoDTO;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements Dao<EmpleadoDTO, Integer> {

    @Override
    public boolean create(EmpleadoDTO empleado) {
        String sql = "INSERT INTO Empleados (id_empleado, fecha_contratacion) VALUES (?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, empleado.getIdEmpleado());
            ps.setDate(2, empleado.getFechaContratacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<EmpleadoDTO> getAll() {
        List<EmpleadoDTO> lista = new ArrayList<>();
        String sql = "SELECT e.*, u.nombre, u.correo FROM Empleados e JOIN Usuarios u ON e.id_empleado = u.id_usuario ORDER BY e.fecha_contratacion DESC";
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

    @Override
    public EmpleadoDTO getById(Integer id) {
        String sql = "SELECT e.*, u.nombre, u.correo FROM Empleados e JOIN Usuarios u ON e.id_empleado = u.id_usuario WHERE e.id_empleado = ?";
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

    public List<EmpleadoDTO> getActivos() {
        List<EmpleadoDTO> lista = new ArrayList<>();
        String sql = "SELECT e.*, u.nombre, u.correo FROM Empleados e JOIN Usuarios u ON e.id_empleado = u.id_usuario WHERE e.activo = 1 ORDER BY u.nombre";
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

    @Override
    public boolean update(EmpleadoDTO empleado) {
        String sql = "UPDATE Empleados SET activo = ? WHERE id_empleado = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, empleado.isActivo() ? 1 : 0);
            ps.setInt(2, empleado.getIdEmpleado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "UPDATE Empleados SET activo = 0 WHERE id_empleado = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private EmpleadoDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setIdEmpleado(rs.getInt("id_empleado"));
        dto.setNombre(rs.getString("nombre"));
        dto.setCorreo(rs.getString("correo"));
        dto.setFechaContratacion(rs.getDate("fecha_contratacion"));
        dto.setActivo(rs.getBoolean("activo"));
        return dto;
    }
}