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
        String sql = "SELECT * FROM Automoviles WHERE vendido = 0 AND tipo_origen = 'Agencia' ORDER BY fecha_registro DESC FETCH FIRST 4 ROWS ONLY";
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

    public List<AutomovilDTO> getExternosPorCliente(int idCliente) {
        List<AutomovilDTO> lista = new ArrayList<>();
        // Consultamos autos de tipo Externo contratados por el cliente específico
        String sql = "SELECT DISTINCT a.* FROM Automoviles a "
                + "JOIN Contrataciones_Servicios c ON a.matricula = c.matricula_auto "
                + "WHERE a.tipo_origen = 'Externo' AND c.id_cliente = ? "
                + "ORDER BY a.fecha_registro DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<AutomovilDTO> getDisponiblesParaServicio() {
        List<AutomovilDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Automoviles WHERE (vendido = 0 AND tipo_origen = 'Agencia') OR tipo_origen = 'Externo' ORDER BY tipo_origen, fecha_registro DESC";
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

    public String generarSiguienteMatriculaExterno() {
        String sql = "SELECT matricula FROM Automoviles WHERE matricula LIKE 'AUE-%' "
                + "ORDER BY TO_NUMBER(SUBSTR(matricula, 5)) DESC FETCH FIRST 1 ROWS ONLY";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String ultima = rs.getString("matricula");
                int num = Integer.parseInt(ultima.substring(4)) + 1;
                return String.format("AUE-%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "AUE-001";
    }

    public List<AutomovilDTO> getVehiculosDeCliente(int idCliente) {
        List<AutomovilDTO> lista = new ArrayList<>();
        String sql = "SELECT a.* FROM Automoviles a "
                + "JOIN Detalle_Venta_Autos d ON a.matricula = d.matricula_auto "
                + "JOIN Ventas v ON d.id_venta = v.id_venta "
                + "WHERE v.id_cliente = ? ORDER BY a.fecha_registro DESC";
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

    public boolean esVehiculoDeCliente(String matricula, int idCliente) {
        String sql = "SELECT COUNT(*) FROM ("
                + "SELECT matricula FROM Automoviles WHERE matricula = ? AND tipo_origen = 'Externo' "
                + "UNION "
                + "SELECT d.matricula_auto FROM Detalle_Venta_Autos d "
                + "JOIN Ventas v ON d.id_venta = v.id_venta "
                + "WHERE d.matricula_auto = ? AND v.id_cliente = ?"
                + ")";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ps.setString(2, matricula);
            ps.setInt(3, idCliente);
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

    public boolean restablecerAutosAgencia() {
        String sqlUpdate = "UPDATE Automoviles SET vendido = 0 WHERE tipo_origen = 'Agencia'";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.executeUpdate();

            String[][] autosDefault = {
                {"AUC-001", "3VWJETTA202300001", "Volkswagen", "Jetta", "2023", "Agencia", "430000", "0", "Sedán compacto ideal para la ciudad. Equipado con motor turbo eficiente y amplio espacio interior.", "VKjetta.jpg"},
                {"AUC-002", "JTDMPRIUS202500002", "Toyota", "Prius", "2025", "Agencia", "600000", "0", "Vehículo híbrido de última generación. Excelente rendimiento de combustible y tecnología avanzada.", "Priustoyota.png"},
                {"AUC-003", "4T14RUNNER20260003", "Toyota", "4Runner", "2026", "Agencia", "1000000", "0", "SUV todoterreno de alta resistencia. Motor V6, tracción 4x4 y acabados de lujo.", "4runner.png"},
                {"AUC-004", "2T1COROLLA2024004", "Toyota", "Corolla", "2024", "Agencia", "428000", "0", "El sedán más vendido del mundo. Confiable, seguro y muy cómodo para el día a día.", "tcorolla.png"},
                {"AUC-005", "VIN12345678905", "Honda", "Civic", "2025", "Agencia", "520000", "0", "Auto sedan elegante y deportivo.", "VKjetta.jpg"}
            };

            String sqlInsert = "INSERT INTO Automoviles (matricula, numero_serie, marca, modelo, anio, tipo_origen, precio, vendido, descripcion, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?)";

            for (String[] a : autosDefault) {
                if (getById(a[0]) == null) {
                    try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                        psIns.setString(1, a[0]);
                        psIns.setString(2, a[1]);
                        psIns.setString(3, a[2]);
                        psIns.setString(4, a[3]);
                        psIns.setInt(5, Integer.parseInt(a[4]));
                        psIns.setString(6, a[5]);
                        psIns.setDouble(7, Double.parseDouble(a[6]));
                        psIns.setString(8, a[8]);
                        psIns.setString(9, a[9]);
                        psIns.executeUpdate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}