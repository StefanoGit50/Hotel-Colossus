package it.unisa.Testing.Server.Autentication;

import it.unisa.Server.Autentication.TokenGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenGeneratorTester
{

    // Test per generateToken

    // token non nullo
    @Test
    void testGenerateTokenNonNullToken()
    {
        // Act
        String token = TokenGenerator.generateToken();

        // Assert - Il token generato non deve essere null
        assertNotNull(token);
    }

    // testa se il token inizia col giusto prefisso
    @Test
    void testGenerateTokenStartWithPrefix() {
        // Act
        String token = TokenGenerator.generateToken();

        // Assert - Il token deve iniziare con il prefisso "PWD-PASS-"
        assertTrue(token.startsWith("PWD-PASS-"));
    }

    // verifica formato corretto
    @Test
    void testGenerateTokenCorrectFormat() {
        // Act
        String token = TokenGenerator.generateToken();

        // Assert - Il token dovrebbe essere nel formato: PWD-PASS-[random-base64]
        String[] parts = token.split("PWD-PASS-");
        assertEquals(2, parts.length);

        // La parte random non dovrebbe essere vuota
        String randomPart = parts[1];
        assertFalse(randomPart.isEmpty());
    }

    // verifica che non generi piu volte la stessa password
    @Test
    void testGenerateTokenDifferentTokensEachTime()
    {
        // Act
        String token1 = TokenGenerator.generateToken();
        String token2 = TokenGenerator.generateToken();
        String token3 = TokenGenerator.generateToken();

        // Assert - Ogni token generato dovrebbe essere diverso
        assertNotEquals(token1, token2);
        assertNotEquals(token2, token3);
        assertNotEquals(token1, token3);
    }

    // verifica lunghezza prefisso
    @Test
    void testGenerateTokenExpectedLength()
    {
        // Act
        String token = TokenGenerator.generateToken();

        assertEquals(41, token.length());
    }

    // verifica che da un insieme di 100 token siano unici
    @Test
    void testMultipleTokenGenerationsShouldAllBeUnique()
    {
        // Arrange - Prepara un set per raccogliere 100 token
        int numberOfTokens = 100;
        java.util.Set<String> uniqueTokens = new java.util.HashSet<>();

        // Act - Genera 100 token
        for (int i = 0; i < numberOfTokens; i++) {
            uniqueTokens.add(TokenGenerator.generateToken());
        }

        // Assert - Tutti i 100 token dovrebbero essere unici
        assertEquals(numberOfTokens, uniqueTokens.size());
    }
}