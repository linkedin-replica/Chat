package com.linkedin.replica.chat.utils;

import io.jsonwebtoken.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtilities {
    private static byte[] secretKey ;

    private static byte[] generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(192);
        SecretKey secretKey = generator.generateKey();
        return secretKey.getEncoded();
    }

    public static void initKey() throws NoSuchAlgorithmException {
//        secretKey = generateSecretKey();
        secretKey = "hatem".getBytes();
    }

    /**
     * Generate jwt token based on claims, id of user to be authenticated and expiration duration
     *
     * @param senderId the userId of the sender
     * @param receiverId the userId of the receiver
     * @return jwt token for the chat session
     */
    public static String generateToken(String senderId, String receiverId) {
        // TODO get expiration minutes from config
        Map<String, Object> claims = new HashMap<>();
        claims.put("senderId", senderId);
        claims.put("receiverId", receiverId);
        return Jwts.builder()
                .setClaims(claims)
//                .setId(senderId)
//                .setIssuedAt(new Date())
//                .setIssuer("linkedin.chat")
//                .setExpiration(generateExpirationDate(600L))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    /**
     * Get claims (stored data) from a valid token
     * @param token the token to get the claims from
     */
    public static Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        }
        catch (JwtException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    private static Date generateExpirationDate(long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
    	initKey();
		System.out.println(generateToken("1", "2"));
	}
}

