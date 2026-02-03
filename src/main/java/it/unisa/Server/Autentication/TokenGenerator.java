package it.unisa.Server.Autentication;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class TokenGenerator {

    private  Instant expiresAt;
    private  String token;


    public TokenGenerator(int amount) {
        this.expiresAt = Instant.now().plus(amount, ChronoUnit.MINUTES);
        this.token = generateToken();
    }


    /**
     * Genera un token
     *
     * @post result != null && result != "" && result.startsWith("PWD-PASS-")
     * @return String
     */
    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);

        return "PWD-TMP-" + randomPart;
    }


    /**
     * Verifica se è scaduto il token
     *
     * @param expiresAt
     * @pre expiresAt is valid & expiresAt != null
     * @post result != null
     * @return true o false
     */
    public static boolean isExpired(Instant expiresAt) {
        return Instant.now().isAfter(expiresAt);
    }

    // Getters

    /**
     * Restituisce il valore di token
     *
     * @post result != null && result != ""
     * @return token
     */
    public String getToken() {
        return token;
    }


    /**
     * Restituisce expiresAt
     *
     * @post result != null && result is valid
     * @return token
     */
    public Instant getExpiresAt() {
        return expiresAt;
    }


}
