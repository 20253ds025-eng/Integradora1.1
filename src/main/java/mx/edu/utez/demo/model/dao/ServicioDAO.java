package mx.edu.utez.demo.model.dao;
import mx.edu.utez.demo.model.ServicioDTO;
import mx.edu.utez.demo.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO implements Dao<ServicioDTO, Integer> {

    @Override
    public boolean create(ServicioDTO servicio) {
        String sql = "INSERT INTO Servicios (nombre_servicio, descripcion, costo, tipo_aplicacion) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setDouble(3, servicio.getCosto());
            ps.setString(4, servicio.getTipoAplicacion());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    servicio.setIdServicio(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ServicioDTO> getAll() {
        List<ServicioDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Servicios WHERE activo = TRUE ORDER BY nombre_servicio";
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
    public ServicioDTO getById(Integer id) {
        String sql = "SELECT * FROM Servicios WHERE id_servicio = ?";
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

    @Override
    public boolean update(ServicioDTO servicio) {
        String sql = "UPDATE Servicios SET nombre_servicio = ?, descripcion = ?, costo = ?, tipo_aplicacion = ? "
                + "WHERE id_servicio = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setDouble(3, servicio.getCosto());
            ps.setString(4, servicio.getTipoAplicacion());
            ps.setInt(5, servicio.getIdServicio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "UPDATE Servicios SET activo = FALSE WHERE id_servicio = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ServicioDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ServicioDTO dto = new ServicioDTO();
        dto.setIdServicio(rs.getInt("id_servicio"));
        dto.setNombreServicio(rs.getString("nombre_servicio"));
        dto.setDescripcion(rs.getString("descripcion"));
        dto.setCosto(rs.getDouble("costo"));
        dto.setTipoAplicacion(rs.getString("tipo_aplicacion"));
        dto.setActivo(rs.getBoolean("activo"));
        return dto;
    }
}