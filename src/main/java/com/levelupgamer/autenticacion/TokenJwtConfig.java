package com.levelupgamer.autenticacion;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class TokenJwtConfig {
    private static final String SECRET_STRING = "LevelUpGamerSecretKey2024ParaJWTTokenSeguro12345678901234567890";
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}

