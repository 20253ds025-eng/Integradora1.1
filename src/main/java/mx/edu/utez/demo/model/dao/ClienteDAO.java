package mx.edu.utez.demo.model.dao;
import mx.edu.utez.demo.model.ClienteDTO;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements Dao<ClienteDTO, Integer> {

    @Override
    public boolean create(ClienteDTO cliente) {
        // Usamos SYSDATE de Oracle para manejar la fecha automáticamente
        String sql = "INSERT INTO Clientes (id_cliente, id_asesor, fecha_registro) VALUES (?, ?, SYSDATE)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getIdCliente());

            // Lógica para permitir que un cliente no tenga asesor asignado (NULL)
            // Asumimos que si getIdAsesor() es 0, significa que no hay asesor.
            if (cliente.getIdAsesor() > 0) {
                ps.setInt(2, cliente.getIdAsesor());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ClienteDTO> getAll() {
        List<ClienteDTO> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre, u.correo FROM Clientes c JOIN Usuarios u ON c.id_cliente = u.id_usuario ORDER BY u.nombre";
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
    public ClienteDTO getById(Integer id) {
        String sql = "SELECT c.*, u.nombre, u.correo FROM Clientes c JOIN Usuarios u ON c.id_cliente = u.id_usuario WHERE c.id_cliente = ?";
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

    public List<ClienteDTO> getByAsesor(int idAsesor) {
        List<ClienteDTO> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre, u.correo FROM Clientes c JOIN Usuarios u ON c.id_cliente = u.id_usuario WHERE c.id_asesor = ? ORDER BY u.nombre";
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

    public boolean reasignarAsesor(int idCliente, int nuevoAsesor) {
        String sql = "UPDATE Clientes SET id_asesor = ? WHERE id_cliente = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoAsesor);
            ps.setInt(2, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(ClienteDTO cliente) {
        String sql = "UPDATE Clientes SET id_asesor = ? WHERE id_cliente = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Mismo caso: si le quitan el asesor, lo pasamos a NULL
            if (cliente.getIdAsesor() > 0) {
                ps.setInt(1, cliente.getIdAsesor());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setInt(2, cliente.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        // CORRECCIÓN ORACLE: En lugar de FALSE, usamos 0
        String sql = "UPDATE Usuarios SET activo = 0 WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ClienteDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(rs.getInt("id_cliente"));
        dto.setNombre(rs.getString("nombre"));
        dto.setCorreo(rs.getString("correo"));

        // Si el valor en la base de datos es NULL, rs.getInt() automáticamente devolverá 0 en Java.
        dto.setIdAsesor(rs.getInt("id_asesor"));

        dto.setFechaRegistro(rs.getDate("fecha_registro"));
        return dto;
    }
}