package mx.edu.utez.demo.model.dao;

import mx.edu.utez.demo.model.AutomovilDTO;
import mx.edu.utez.demo.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutomovilDAO implements Dao<AutomovilDTO, String> {

    @Override
    public boolean create(AutomovilDTO auto) {
        String sql = "INSERT INTO Automoviles (matricula, numero_serie, marca, modelo, anio, tipo_origen, precio, descripcion, imagen) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, auto.getMatricula());
            ps.setString(2, auto.getNumeroSerie());
            ps.setString(3, auto.getMarca());
            ps.setString(4, auto.getModelo());
            ps.setInt(5, auto.getAnio());
            ps.setString(6, auto.getTipoOrigen());
            ps.setDouble(7, auto.getPrecio());
            ps.setString(8, auto.getDescripcion());
            ps.setString(9, auto.getImagen());   // <-- NUEVO
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AutomovilDTO> getAll() {
        List<AutomovilDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Automoviles ORDER BY fecha_registro DESC";
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

    public List<AutomovilDTO> getDisponibles() {
        List<AutomovilDTO> lista = new ArrayList<>();
        // CORRECCIÓN ORACLE: vendido = 0 en lugar de FALSE
        String sql = "SELECT * FROM Automoviles WHERE vendido = 0 AND tipo_origen = 'Agencia' ORDER BY fecha_registro DESC";
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

    public List<AutomovilDTO> getDestacados() {
        List<AutomovilDTO> lista = new ArrayList<>();
        // CORRECCIÓN ORACLE: vendido = 0 y sintaxis FETCH FIRST en lugar de LIMIT
        String sql = "SELECT * FROM Automoviles WHERE vendido = 0 AND tipo_origen = 'Agencia' ORDER BY fecha_registro DESC FETCH FIRST 3 ROWS ONLY";
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

    public List<AutomovilDTO> getExternos() {
        List<AutomovilDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Automoviles WHERE tipo_origen = 'Externo' ORDER BY fecha_registro DESC";
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
    public AutomovilDTO getById(String matricula) {
        String sql = "SELECT * FROM Automoviles WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
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
    public boolean update(AutomovilDTO auto) {
        // Incluye "imagen" para permitir cambiar la foto al editar el auto.
        // Si no se sube una foto nueva, el Servlet debe pasar el nombre de archivo
        // que ya tenía (consultado antes con getById) para no perder la referencia.
        String sql = "UPDATE Automoviles SET marca = ?, modelo = ?, anio = ?, tipo_origen = ?, precio = ?, descripcion = ?, imagen = ? "
                + "WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, auto.getMarca());
            ps.setString(2, auto.getModelo());
            ps.setInt(3, auto.getAnio());
            ps.setString(4, auto.getTipoOrigen());
            ps.setDouble(5, auto.getPrecio());
            ps.setString(6, auto.getDescripcion());
            ps.setString(7, auto.getImagen());
            ps.setString(8, auto.getMatricula());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean marcarVendido(String matricula) {
        // CORRECCIÓN ORACLE: vendido = 1 en lugar de TRUE
        String sql = "UPDATE Automoviles SET vendido = 1 WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String matricula) {
        // CORRECCIÓN ORACLE: vendido = 0 en lugar de FALSE
        String sql = "DELETE FROM Automoviles WHERE matricula = ? AND vendido = 0";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeMatricula(String matricula) {
        String sql = "SELECT COUNT(*) FROM Automoviles WHERE matricula = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeNumeroSerie(String numeroSerie) {
        String sql = "SELECT COUNT(*) FROM Automoviles WHERE numero_serie = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroSerie);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private AutomovilDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        AutomovilDTO dto = new AutomovilDTO();
        dto.setMatricula(rs.getString("matricula"));
        dto.setNumeroSerie(rs.getString("numero_serie"));
        dto.setMarca(rs.getString("marca"));
        dto.setModelo(rs.getString("modelo"));
        dto.setAnio(rs.getInt("anio"));
        dto.setTipoOrigen(rs.getString("tipo_origen"));
        dto.setPrecio(rs.getDouble("precio"));
        // rs.getBoolean sí funciona bien en Java, no te preocupes por esto
        dto.setVendido(rs.getBoolean("vendido"));
        dto.setDescripcion(rs.getString("descripcion"));
        dto.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        dto.setImagen(rs.getString("imagen"));
        return dto;
    }
}