package com.example.readingisgood.utils;

import com.example.readingisgood.configuration.properties.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.example.readingisgood.configuration.constants.SecurityConstants.*;
import static org.apache.commons.lang3.StringUtils.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    private final JwtProperties jwtProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error(INVALID_SIGNATURE_LOG, e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(INVALID_TOKEN_LOG, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(TOKEN_EXPIRED_LOG, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(TOKEN_UNSUPPORTED_LOG, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(EMPTY_CLAIMS_LOG, e.getMessage());
        }

        return false;
    }

    public String parseTokenFromAuthorizationHeader(String authorizationHeader) {
        if (!isEmpty(authorizationHeader) && authorizationHeader.startsWith(BEARER + SPACE)) {
            return authorizationHeader.substring(7);
        } else {
            return EMPTY;
        }
    }
}
