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

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);

        return "PWD-TMP-" + randomPart;
    }

    public static boolean isExpired(Instant expiresAt) {
        return Instant.now().isAfter(expiresAt);
    }
    // Getters
    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }


}
