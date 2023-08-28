package com.veda.service.Jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.veda.model.auth.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtTokenUtil implements IJwtTokenUtil {

    @ConfigProperty(name = "jwt.token.expiration-time")
    int tokenExpiryTime;

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

    public User verifyToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(token).getBody();

        User user = new User();
        user.setId(claims.get(ID).toString());
        user.setRoles((List<String>) claims.get(ROLES));
        user.setName(claims.get(NAME).toString());

        return user;
    }

    private Date convertLocalDateTimeToDate(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public String generateToken(User user) {

        // TODO: Implement Issuer
        LocalDateTime now = LocalDateTime.now();
        JwtBuilder builder = Jwts.builder()
                .claim(ID, user.getId())
                .claim(NAME, user.getName())
                .claim(ROLES, user.getRoles())
                .setIssuedAt(convertLocalDateTimeToDate(now))
                .signWith(signatureAlgorithm, key);
        builder.setExpiration(convertLocalDateTimeToDate(now.plusMinutes(tokenExpiryTime)));
        return builder.compact();
    }
}
