package com.levelupgamer.autenticacion;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class TokenJwtConfig {
    private static final String DEFAULT_SECRET = "LevelUpGamerSecretKey2024ParaJWTTokenSeguro12345678901234567890123456789012345678901234567890";
    
    private static String getSecretFromEnv() {
        String jwtSecret = System.getenv("JWT_SECRET");
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            jwtSecret = System.getProperty("JWT_SECRET");
        }
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            return DEFAULT_SECRET;
        }
        return jwtSecret;
    }
    
    private static SecretKey createSecretKey() {
        String secretString = getSecretFromEnv();
        byte[] secretBytes = secretString.getBytes(StandardCharsets.UTF_8);
        
        if (secretBytes.length < 64) {
            byte[] paddedBytes = new byte[64];
            System.arraycopy(secretBytes, 0, paddedBytes, 0, Math.min(secretBytes.length, 64));
            if (secretBytes.length < 64) {
                byte[] defaultBytes = DEFAULT_SECRET.getBytes(StandardCharsets.UTF_8);
                System.arraycopy(defaultBytes, secretBytes.length, paddedBytes, secretBytes.length, 64 - secretBytes.length);
            }
            secretBytes = paddedBytes;
        }
        
        return Keys.hmacShaKeyFor(secretBytes);
    }
    
    public static final SecretKey SECRET_KEY = createSecretKey();
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}

