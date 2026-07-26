package mx.edu.utez.demo.utils;
import org.mindrot.jbcrypt.BCrypt;
public class PasswordHasher {

    /**
     * Genera un hash BCrypt de la contraseña
     * @param password Contraseña en texto plano
     * @return Hash de la contraseña
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Verifica si la contraseña coincide con el hash almacenado
     * @param password Contraseña en texto plano
     * @param hashed Hash almacenado en la base de datos
     * @return true si coincide, false en caso contrario
     */
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}