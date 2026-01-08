package it.unisa.Server.Autentication;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class TokenGenerator {

    private static Instant expiresAt;
    private static String token;

    public TokenGenerator(String token) {
        TokenGenerator.expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
        TokenGenerator.token = token;
    }

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);

        String token = "PWD-PASS-" + randomPart;
        return token;
    }


}
