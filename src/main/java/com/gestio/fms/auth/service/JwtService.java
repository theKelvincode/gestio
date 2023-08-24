package com.gestio.fms.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "Xhqq6gtB4Bs0mymDv6qJaVyPzSTwuZjE";

    public String extractUsername(String token) {
        // subject refers to the username of the token
        return extractClaim(token, Claims::getSubject);
    }

    // method to extract a single claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // generate a token from the user details itself
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // method that will validate a token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // this is how we can generate a token out of extra claims and user details
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // a method that allows us extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                // a sign-in key is a secret key that is used to digitally sign the jwt,
                // used to create the signature part of the jwt which is used to verify the authenticity
                // of the sender and ensure the integrity of the message.
                // the sign-in key is used in conjunction with the sign in algorithm specified in the jwt header
                // to create the signature.
                // the specific sign-in algo and key-size will depend on the security requirement of your app and the
                // level of trust you have in the sign-in party.
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                // within this method, we can get all the claims inside the token
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

// what is JWT?
/*
 a jwt is a compact url-safe means of representing claims to be transferred between two parties.
 The claims in jwt are encoded as json objects, ie digitally signed using a json web signature.
 The jwt consists of three parts : header, payload, signature

        header:
        typically consists of two parts: type of token "JWT" and the signing algorithm being used.

        payload: this contains the claims
        claims are statements about an entity typically the user and additional data.

        verify signature:
        used to verify the authenticity of the jwt sender. and to ensure that the message was not changed along the way.
 */