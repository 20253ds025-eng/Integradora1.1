package mx.edu.utez.demo.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailSender {

    private static Properties mailProps;

    static {
        mailProps = new Properties();
        try (InputStream input = EmailSender.class.getClassLoader()
                .getResourceAsStream("credentials.properties")) {
            if (input != null) {
                mailProps.load(input);
            } else {
                System.err.println("credentials.properties no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ENVIAR CREDENCIALES (Método que faltaba)
    // ==========================================
    public static void enviarCredenciales(String destinatario, String nombre, String contrasena) {
        String asunto = "Bienvenido a Click & Drive - Tus credenciales de acceso";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Tu cuenta en Click & Drive ha sido creada exitosamente.\n\n"
                + "Correo: " + destinatario + "\n"
                + "Contraseña: " + contrasena + "\n\n"
                + "Te recomendamos cambiar tu contraseña en tu primer inicio de sesión.\n\n"
                + "Saludos,\n"
                + "Equipo Click & Drive";

        try {
            enviarCorreo(destinatario, asunto, cuerpo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ENVIAR CÓDIGO DE RECUPERACIÓN
    // ==========================================
    public static void enviarCodigoRecuperacion(String destinatario, String nombre, String codigo) {
        String asunto = "Click & Drive - Código de recuperación de contraseña";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Recibimos una solicitud para restablecer tu contraseña.\n\n"
                + "Tu código de verificación es: " + codigo + "\n\n"
                + "Este código es válido por 10 minutos. Si tú no solicitaste este cambio, "
                + "ignora este correo y tu contraseña seguirá siendo la misma.\n\n"
                + "Saludos,\n"
                + "Equipo Click & Drive";

        try {
            enviarCorreo(destinatario, asunto, cuerpo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ENVIAR NOTIFICACIÓN (Método que faltaba)
    // ==========================================
    public static void enviarNotificacion(String destinatario, String asunto, String cuerpo) {
        try {
            enviarCorreo(destinatario, asunto, cuerpo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ENVIAR CORREO (Método privado)
    // ==========================================
    private static void enviarCorreo(String destinatario, String asunto, String cuerpo) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailProps.getProperty("mail.host"));
        props.put("mail.smtp.port", mailProps.getProperty("mail.port"));
        props.put("mail.smtp.auth", mailProps.getProperty("mail.auth"));
        props.put("mail.smtp.starttls.enable", mailProps.getProperty("mail.starttls"));

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        mailProps.getProperty("mail.username"),
                        mailProps.getProperty("mail.password")
                );
            }
        };

        Session session = Session.getInstance(props, auth);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailProps.getProperty("mail.username")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setText(cuerpo);
        Transport.send(message);
        System.out.println("Correo enviado a: " + destinatario);
    }
}