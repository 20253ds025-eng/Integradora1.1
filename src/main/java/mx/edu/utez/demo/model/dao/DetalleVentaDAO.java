package mx.edu.utez.demo.model.dao;
import mx.edu.utez.demo.model.DetalleVentaDTO;
import mx.edu.utez.demo.utils.SQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO implements Dao<DetalleVentaDTO, Integer> {

    @Override
    public boolean create(DetalleVentaDTO detalle) {
        String sql = "INSERT INTO Detalle_Venta_Autos (id_venta, matricula_auto, precio_venta) VALUES (?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getIdVenta());
            ps.setString(2, detalle.getMatriculaAuto());
            ps.setDouble(3, detalle.getPrecioVenta());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
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
    public List<DetalleVentaDTO> getAll() {
        List<DetalleVentaDTO> lista = new ArrayList<>();
        String sql = "SELECT d.*, a.marca, a.modelo FROM Detalle_Venta_Autos d JOIN Automoviles a ON d.matricula_auto = a.matricula";
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
    public DetalleVentaDTO getById(Integer id) {
        String sql = "SELECT d.*, a.marca, a.modelo FROM Detalle_Venta_Autos d JOIN Automoviles a ON d.matricula_auto = a.matricula WHERE d.id_detalle = ?";
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

    public List<DetalleVentaDTO> getByVenta(int idVenta) {
        List<DetalleVentaDTO> lista = new ArrayList<>();
        String sql = "SELECT d.*, a.marca, a.modelo FROM Detalle_Venta_Autos d JOIN Automoviles a ON d.matricula_auto = a.matricula WHERE d.id_venta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean update(DetalleVentaDTO detalle) {
        return false; // Los detalles no se actualizan (inmutabilidad)
    }

    @Override
    public boolean delete(Integer id) {
        return false; // Los detalles no se eliminan (inmutabilidad)
    }

    private DetalleVentaDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setIdDetalle(rs.getInt("id_detalle"));
        dto.setIdVenta(rs.getInt("id_venta"));
        dto.setMatriculaAuto(rs.getString("matricula_auto"));
        dto.setMarca(rs.getString("marca"));
        dto.setModelo(rs.getString("modelo"));
        dto.setPrecioVenta(rs.getDouble("precio_venta"));
        return dto;
    }
}