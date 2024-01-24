package com.app.theraventesttask.config.jwt;

import com.app.theraventesttask.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Component responsible for JWT token creation, validation, and extraction of information from tokens.
 *
 * This class handles the generation of JWT tokens, authentication based on tokens,
 * and token validation to ensure their integrity and expiration.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expired}")
    private long validityTimeInMillis;

    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtTokenProvider.
     *
     * @param userDetailsService Service for loading user(customer) details during authentication.
     */
    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Initializes the JwtTokenProvider by encoding the secret key using Base64.
     */
    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * Creates a JWT token based on the provided username and role.
     *
     * @param username User's username.
     * @param role     User's role.
     * @return String representing the generated JWT token.
     */
    public String createToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration((new Date(System.currentTimeMillis() + validityTimeInMillis)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Retrieves the authentication information from a JWT token.
     *
     * @param token JWT token.
     * @return Authentication object containing user details and authorities.
     */
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Extracts the email (subject) from a JWT token.
     *
     * @param token JWT token.
     * @return String representing the email extracted from the token.
     */
    public String getEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    /**
     * Resolves the JWT token from the request's Authorization header.
     *
     * @param request HttpServletRequest containing the Authorization header.
     * @return String representing the resolved JWT token or null if not present.
     */
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Validates the integrity and expiration of a JWT token.
     *
     * @param token JWT token to validate.
     * @return boolean indicating whether the token is valid.
     * @throws JwtAuthenticationException Thrown if the token is expired or invalid.
     */
    public boolean validateToken(String token) {
        try{
            Claims claims = extractAllClaims(token);

            return claims.getExpiration().getTime() >= System.currentTimeMillis();

        } catch (JwtException | IllegalArgumentException e){
            throw new JwtAuthenticationException("JWT token is expired or invalid!");
        }
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
