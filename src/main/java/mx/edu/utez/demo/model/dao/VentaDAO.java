package mx.edu.utez.demo.model.dao;
import mx.edu.utez.demo.model.VentaDTO;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO implements Dao<VentaDTO, Integer> {

    @Override
    public boolean create(VentaDTO venta) {
        String sql = "INSERT INTO Ventas (id_cliente, id_asesor_historico, tipo_adquisicion, estatus_pago, total) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, venta.getIdCliente());
            if (venta.getIdAsesorHistorico() > 0) {
                ps.setInt(2, venta.getIdAsesorHistorico());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, venta.getTipoAdquisicion());
            ps.setString(4, venta.getEstatusPago());
            ps.setDouble(5, venta.getTotal());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (PreparedStatement find = con.prepareStatement("SELECT id_venta FROM Ventas WHERE id_cliente = ? ORDER BY fecha_venta DESC FETCH FIRST 1 ROWS ONLY")) {
                    find.setInt(1, venta.getIdCliente());
                    ResultSet rs = find.executeQuery();
                    if (rs.next()) {
                        venta.setIdVenta(rs.getInt("id_venta"));
                    }
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
    public List<VentaDTO> getAll() {
        List<VentaDTO> lista = new ArrayList<>();
        String sql = "SELECT v.*, u.nombre as nombre_cliente, u2.nombre as nombre_asesor "
                + "FROM Ventas v "
                + "LEFT JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "LEFT JOIN Usuarios u ON c.id_cliente = u.id_usuario "
                + "LEFT JOIN Empleados e ON v.id_asesor_historico = e.id_empleado "
                + "LEFT JOIN Usuarios u2 ON e.id_empleado = u2.id_usuario "
                + "ORDER BY v.fecha_venta DESC";
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
    public VentaDTO getById(Integer id) {
        String sql = "SELECT v.*, u.nombre as nombre_cliente, u2.nombre as nombre_asesor "
                + "FROM Ventas v "
                + "LEFT JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "LEFT JOIN Usuarios u ON c.id_cliente = u.id_usuario "
                + "LEFT JOIN Empleados e ON v.id_asesor_historico = e.id_empleado "
                + "LEFT JOIN Usuarios u2 ON e.id_empleado = u2.id_usuario "
                + "WHERE v.id_venta = ?";
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

    public VentaDTO getByFolio(String folio) {
        String sql = "SELECT v.*, u.nombre as nombre_cliente, u2.nombre as nombre_asesor "
                + "FROM Ventas v "
                + "LEFT JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "LEFT JOIN Usuarios u ON c.id_cliente = u.id_usuario "
                + "LEFT JOIN Empleados e ON v.id_asesor_historico = e.id_empleado "
                + "LEFT JOIN Usuarios u2 ON e.id_empleado = u2.id_usuario "
                + "WHERE v.folio = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, folio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VentaDTO> getByCliente(int idCliente) {
        List<VentaDTO> lista = new ArrayList<>();
        String sql = "SELECT v.*, u.nombre as nombre_cliente, u2.nombre as nombre_asesor "
                + "FROM Ventas v "
                + "LEFT JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "LEFT JOIN Usuarios u ON c.id_cliente = u.id_usuario "
                + "LEFT JOIN Empleados e ON v.id_asesor_historico = e.id_empleado "
                + "LEFT JOIN Usuarios u2 ON e.id_empleado = u2.id_usuario "
                + "WHERE v.id_cliente = ? ORDER BY v.fecha_venta DESC";
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

    public List<VentaDTO> getByAsesor(int idAsesor) {
        List<VentaDTO> lista = new ArrayList<>();
        String sql = "SELECT v.*, u.nombre as nombre_cliente, u2.nombre as nombre_asesor "
                + "FROM Ventas v "
                + "LEFT JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "LEFT JOIN Usuarios u ON c.id_cliente = u.id_usuario "
                + "LEFT JOIN Empleados e ON v.id_asesor_historico = e.id_empleado "
                + "LEFT JOIN Usuarios u2 ON e.id_empleado = u2.id_usuario "
                + "WHERE v.id_asesor_historico = ? ORDER BY v.fecha_venta DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsesor);
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
    public boolean update(VentaDTO venta) {
        String sql = "UPDATE Ventas SET estatus_pago = ? WHERE id_venta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, venta.getEstatusPago());
            ps.setInt(2, venta.getIdVenta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTotal(int idVenta, double total) {
        String sql = "UPDATE Ventas SET total = ? WHERE id_venta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, total);
            ps.setInt(2, idVenta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        // Las ventas no se eliminan (inmutabilidad financiera)
        return false;
    }

    public double getTotalVentasPorAsesor(int idAsesor) {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM Ventas WHERE id_asesor_historico = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsesor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private VentaDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(rs.getInt("id_venta"));
        dto.setFolio(rs.getString("folio"));
        dto.setIdCliente(rs.getInt("id_cliente"));
        String clienteNombre = rs.getString("nombre_cliente");
        dto.setNombreCliente(clienteNombre != null ? clienteNombre : "Cliente");
        dto.setIdAsesorHistorico(rs.getInt("id_asesor_historico"));
        String asesorNombre = rs.getString("nombre_asesor");
        dto.setNombreAsesor(asesorNombre != null ? asesorNombre : "Venta en línea");
        dto.setFechaVenta(rs.getTimestamp("fecha_venta"));
        dto.setTipoAdquisicion(rs.getString("tipo_adquisicion"));
        dto.setEstatusPago(rs.getString("estatus_pago"));
        dto.setTotal(rs.getDouble("total"));
        return dto;
    }
    public int createReturnId(VentaDTO venta) {
        String sql = "INSERT INTO Ventas (id_cliente, id_asesor_historico, tipo_adquisicion, estatus_pago, total) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, venta.getIdCliente());
            if (venta.getIdAsesorHistorico() > 0) {
                ps.setInt(2, venta.getIdAsesorHistorico());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, venta.getTipoAdquisicion());
            ps.setString(4, venta.getEstatusPago());
            ps.setDouble(5, venta.getTotal());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (PreparedStatement find = con.prepareStatement("SELECT id_venta FROM Ventas WHERE id_cliente = ? ORDER BY fecha_venta DESC FETCH FIRST 1 ROWS ONLY")) {
                    find.setInt(1, venta.getIdCliente());
                    ResultSet rs = find.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("id_venta");
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("ERROR VentaDAO.createReturnId: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}