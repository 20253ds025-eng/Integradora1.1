package mx.edu.utez.demo.model.dao;

import mx.edu.utez.demo.model.UsuarioDTO;  // ← CORREGIDO: agregado "dto."
import mx.edu.utez.demo.utils.PasswordHasher;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements Dao<UsuarioDTO, Integer> {

    // ==========================================
    // CREATE - INSERTAR USUARIO
    // ==========================================
    @Override
    public boolean create(UsuarioDTO usuario) {
        String sql = "INSERT INTO Usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
        // CORRECCIÓN: Le pasamos un arreglo con el nombre de la columna (en MAYÚSCULAS para Oracle)
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_USUARIO"})) {
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

    // ==========================================
    // READ - OBTENER TODOS LOS USUARIOS
    // ==========================================
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

    // ==========================================
    // READ - OBTENER USUARIO POR ID
    // ==========================================
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

    // ==========================================
    // READ - OBTENER USUARIO POR CORREO
    // ==========================================
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

    // ==========================================
    // READ - OBTENER USUARIOS POR ROL
    // ==========================================
    public List<UsuarioDTO> getByRol(String rol) {
        List<UsuarioDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE rol = ? ORDER BY fecha_registro DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol);
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
    // READ - VERIFICAR SI CORREO EXISTE
    // ==========================================
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE correo = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // READ - AUTENTICAR USUARIO
    // ==========================================
    public UsuarioDTO autenticar(String correo, String contrasena) {
        // CORRECCIÓN: activo = 1
        String sql = "SELECT * FROM Usuarios WHERE correo = ? AND activo = 1";
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

    // ==========================================
    // UPDATE - ACTUALIZAR USUARIO
    // ==========================================
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

    // ==========================================
    // UPDATE - CAMBIAR CONTRASEÑA
    // ==========================================
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

    // ==========================================
    // REACTIVAR USUARIO (revertir desactivación/bloqueo)
    // ==========================================
    public boolean reactivar(int id) {
        String sql = "UPDATE Usuarios SET activo = 1, bloqueado = 0, intentos_fallidos = 0 " +
                "WHERE id_usuario = ?";
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
    // DELETE - DESACTIVAR USUARIO (No eliminar)
    // ==========================================
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

    // ==========================================
    // MÉTODOS PRIVADOS
    // ==========================================
    private void reiniciarIntentos(int id) throws SQLException {
        // CORRECCIÓN: bloqueado = 0
        String sql = "UPDATE Usuarios SET intentos_fallidos = 0, bloqueado = 0 WHERE id_usuario = ?";
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
        // CORRECCIÓN: bloqueado = 1
        String sql2 = "UPDATE Usuarios SET bloqueado = 1 WHERE id_usuario = ? AND intentos_fallidos >= 3";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql2)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ==========================================
    // GUARDAR CÓDIGO DE RECUPERACIÓN
    // ==========================================
    public boolean guardarCodigoRecuperacion(String correo, String codigo) {
        String sql = "UPDATE Usuarios SET codigo_recuperacion = ?, " +
                "codigo_expiracion = ? WHERE correo = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            // Código válido por 10 minutos
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));
            ps.setString(3, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // VALIDAR CÓDIGO DE RECUPERACIÓN
    // ==========================================
    public UsuarioDTO validarCodigoRecuperacion(String correo, String codigo) {
        String sql = "SELECT * FROM Usuarios WHERE correo = ? " +
                "AND codigo_recuperacion = ? AND codigo_expiracion > ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigo);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
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
    // LIMPIAR CÓDIGO (tras usarlo o al invalidarlo)
    // ==========================================
    public void limpiarCodigoRecuperacion(int idUsuario) {
        String sql = "UPDATE Usuarios SET codigo_recuperacion = NULL, " +
                "codigo_expiracion = NULL WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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