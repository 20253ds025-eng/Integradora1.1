package mx.edu.utez.demo.model.dao;

import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Maneja los tokens de login persistente ("Recuérdame") usando la tabla
 * Sesiones_Activas. No implementa la interfaz Dao<T,K> genérica porque su
 * ciclo de vida es distinto: se valida y se cierra por token, no por id.
 */
public class SesionActivaDAO {

    // Crea un nuevo token de sesión persistente para el usuario.
    public boolean crear(int idUsuario, String token, String ipOrigen, int diasValidez) {
        String sql = "INSERT INTO Sesiones_Activas (id_usuario, token_sesion, fecha_expiracion, ip_origen, activa) "
                + "VALUES (?, ?, ?, ?, 1)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, token);
            Timestamp expiracion = new Timestamp(System.currentTimeMillis() + (diasValidez * 24L * 60 * 60 * 1000));
            ps.setTimestamp(3, expiracion);
            ps.setString(4, ipOrigen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Devuelve el id_usuario si el token existe, sigue activo y no ha expirado.
    // Si no es válido por cualquier motivo, regresa null.
    public Integer validarToken(String token) {
        String sql = "SELECT id_usuario FROM Sesiones_Activas "
                + "WHERE token_sesion = ? AND activa = 1 AND fecha_expiracion > CURRENT_TIMESTAMP";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Invalida (cierra) un token específico, por ejemplo al hacer logout.
    public boolean invalidarToken(String token) {
        String sql = "UPDATE Sesiones_Activas SET activa = 0 WHERE token_sesion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Invalida TODOS los tokens "recuérdame" activos de un usuario, sin
     * importar en qué dispositivo se hayan creado. El DFR exige que al
     * cambiar la contraseña se cierren todas las sesiones activas del
     * usuario (Módulos 1.1, 1.2 y 5.5); esto cubre específicamente los
     * dispositivos con login persistente, ya que un simple
     * session.invalidate() en el navegador actual no los afecta.
     */
    public boolean invalidarTodasDeUsuario(int idUsuario) {
        String sql = "UPDATE Sesiones_Activas SET activa = 0 WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
