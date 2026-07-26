package mx.edu.utez.demo.model.dao;


import mx.edu.utez.demo.model.UsuarioDTO;
import mx.edu.utez.demo.utils.PasswordHasher;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements Dao<UsuarioDTO, Integer> {

    @Override
    public boolean create(UsuarioDTO usuario) {
        String sql = "INSERT INTO Usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, PasswordHasher.hashPassword(usuario.getContrasena()));
            ps.setString(4, usuario.getRol());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
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
    public List<UsuarioDTO> getAll() {
        List<UsuarioDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios ORDER BY fecha_registro DESC";
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
    public UsuarioDTO getById(Integer id) {
        String sql = "SELECT * FROM Usuarios WHERE id_usuario = ?";
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

    public UsuarioDTO getByCorreo(String correo) {
        String sql = "SELECT * FROM Usuarios WHERE correo = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UsuarioDTO autenticar(String correo, String contrasena) {
        String sql = "SELECT * FROM Usuarios WHERE correo = ? AND activo = TRUE";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("bloqueado")) return null;
                if (PasswordHasher.checkPassword(contrasena, rs.getString("contrasena"))) {
                    reiniciarIntentos(rs.getInt("id_usuario"));
                    return mapResultSetToDTO(rs);
                } else {
                    incrementarIntentos(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void reiniciarIntentos(int id) throws SQLException {
        String sql = "UPDATE Usuarios SET intentos_fallidos = 0, bloqueado = FALSE WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void incrementarIntentos(int id) throws SQLException {
        String sql = "UPDATE Usuarios SET intentos_fallidos = intentos_fallidos + 1 WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        String sql2 = "UPDATE Usuarios SET bloqueado = TRUE WHERE id_usuario = ? AND intentos_fallidos >= 3";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean cambiarContrasena(int id, String nueva) {
        String sql = "UPDATE Usuarios SET contrasena = ? WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, PasswordHasher.hashPassword(nueva));
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(UsuarioDTO usuario) {
        String sql = "UPDATE Usuarios SET nombre = ?, correo = ? WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setInt(3, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "UPDATE Usuarios SET activo = FALSE WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private UsuarioDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(rs.getInt("id_usuario"));
        dto.setNombre(rs.getString("nombre"));
        dto.setCorreo(rs.getString("correo"));
        dto.setContrasena(rs.getString("contrasena"));
        dto.setRol(rs.getString("rol"));
        dto.setActivo(rs.getBoolean("activo"));
        dto.setIntentosFallidos(rs.getInt("intentos_fallidos"));
        dto.setBloqueado(rs.getBoolean("bloqueado"));
        dto.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return dto;
    }
}