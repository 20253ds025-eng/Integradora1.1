package mx.edu.utez.demo.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailSender {

    private static final Properties mailProps = new Properties();

    static {
        try (InputStream input = EmailSender.class.getClassLoader()
                .getResourceAsStream("credentials.properties")) {

            if (input == null) {
                throw new RuntimeException("No se encontró credentials.properties");
            }

            mailProps.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Error cargando credentials.properties", e);
        }
    }

    public static void enviarCredenciales(String destinatario,
                                          String nombre,
                                          String contrasena) {

        String asunto = "Bienvenido a Click & Drive";

        String cuerpo =
                "Hola " + nombre + ",\n\n" +
                        "Tu cuenta ha sido creada correctamente.\n\n" +
                        "Correo: " + destinatario + "\n" +
                        "Contraseña: " + contrasena + "\n\n" +
                        "Te recomendamos cambiar tu contraseña después del primer inicio de sesión.\n\n" +
                        "Saludos.\n" +
                        "Equipo Click & Drive";

        try {
            enviarCorreo(destinatario, asunto, cuerpo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void enviarNotificacion(String destinatario,
                                          String asunto,
                                          String cuerpo) {

        try {
            enviarCorreo(destinatario, asunto, cuerpo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void enviarCorreo(String destinatario,
                                     String asunto,
                                     String cuerpo) throws MessagingException {

        Properties props = new Properties();

        props.put("mail.smtp.host", mailProps.getProperty("mail.host"));
        props.put("mail.smtp.port", mailProps.getProperty("mail.port"));
        props.put("mail.smtp.auth", mailProps.getProperty("mail.auth"));
        props.put("mail.smtp.starttls.enable", mailProps.getProperty("mail.starttls"));

        props.put("mail.smtp.ssl.protocols",
                mailProps.getProperty("mail.smtp.ssl.protocols"));

        props.put("mail.smtp.connectiontimeout",
                mailProps.getProperty("mail.smtp.connectiontimeout"));

        props.put("mail.smtp.timeout",
                mailProps.getProperty("mail.smtp.timeout"));

        props.put("mail.smtp.writetimeout",
                mailProps.getProperty("mail.smtp.writetimeout"));

        Session session = Session.getInstance(props, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(
                        mailProps.getProperty("mail.username"),
                        mailProps.getProperty("mail.password")
                );
            }
        });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(mailProps.getProperty("mail.username")));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinatario)
        );

        message.setSubject(asunto);
        message.setText(cuerpo);

        Transport.send(message);

        System.out.println("Correo enviado correctamente a: " + destinatario);
    }
}