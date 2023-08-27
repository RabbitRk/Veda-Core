package com.veda.service.Jwt;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.*;
import jakarta.enterprise.context.ApplicationScoped;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.veda.model.User;
import com.veda.model.UserBuilder;

import java.security.Key;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class JwtTokenUtil implements IJwtTokenUtil {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ROLES = "roles";

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final Key key;
    private final String issuer;
    private final String secret;

    public JwtTokenUtil(@ConfigProperty(name = "jwt-token-key") String key,
            @ConfigProperty(name = "jwt-token-issuer") String issuer) {
        // TODO: Bad code => DatabaseKindConverter
        byte[] secretBytes = jakarta.xml.bind.DatatypeConverter.parseBase64Binary(key);
        this.key = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
        this.issuer = issuer;
        this.secret = key;
    }

    public String generateToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        JwtBuilder builder = Jwts.builder()
                .claim(ID, user.getId())
                .claim(NAME, user.getName())
                .claim(ROLES, user.getRoles())
                .setIssuedAt(convertLocalDateTimeToDate(now))
                .signWith(signatureAlgorithm, key);
        builder.setExpiration(convertLocalDateTimeToDate(now.plusMinutes(30)));
        return builder.compact();
    }

    public User verifyToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(token).getBody();
        return new UserBuilder()
                .setId(claims.get(ID).toString())
                .setRoles(claims.get(ROLES).toString())
                .setName(claims.get(NAME).toString()).createUser();
    }

    private Date convertLocalDateTimeToDate(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static final int TOKEN_LENGTH_BYTES = 64; // Adjust the token length as needed

    // public static String generateRefreshToken() {
    //     SecureRandom secureRandom = new SecureRandom();
    //     byte[] randomBytes = new byte[TOKEN_LENGTH_BYTES];
    //     secureRandom.nextBytes(randomBytes);
    //     secureRandom.
    //     return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    // }

}
