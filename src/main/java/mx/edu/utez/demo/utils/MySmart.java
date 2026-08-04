package mx.edu.utez.demo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class MySmart {

    public static String getFechaHoraActual() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public static String getFechaActual() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String sanitizar(String input) {
        if (input == null) return "";
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    // ==========================================
    // GENERAR CONTRASEÑA TEMPORAL
    // ==========================================
    public static String generarContrasenaTemporal() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // ==========================================
    // GENERAR CÓDIGO DE VERIFICACIÓN (6 dígitos)
    // ==========================================
    public static String generarCodigoVerificacion() {
        int codigo = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(codigo);
    }

    public static String formatearMoneda(double monto) {
        return String.format("$%,.2f", monto);
    }
}