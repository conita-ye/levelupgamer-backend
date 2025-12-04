package com.levelupgamer.autenticacion;

import com.levelupgamer.usuarios.RolUsuario;
import com.levelupgamer.usuarios.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.levelupgamer.autenticacion.TokenJwtConfig.SECRET_KEY;

@Component
public class JwtProvider {
    private final long jwtAccessExpirationMs = 3600000; // 1 hora
    private final long jwtRefreshExpirationMs = 604800000; // 7 dias

    public String generateAccessToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", usuario.getRoles().stream().map(RolUsuario::name).collect(Collectors.toList()));
        claims.put("usuarioId", usuario.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getCorreo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessExpirationMs))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessToken(Usuario usuario, RolUsuario rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of(rol.name()));
        claims.put("usuarioId", usuario.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getCorreo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessExpirationMs))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getCorreo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
}
