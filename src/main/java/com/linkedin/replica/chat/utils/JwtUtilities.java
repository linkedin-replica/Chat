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
        secretKey = "hatemas".getBytes();
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
                .setId(senderId)
                .setIssuedAt(new Date())
                .setIssuer("linkedin.chat")
                .setExpiration(generateExpirationDate(600L))
                .signWith(SignatureAlgorithm.HS512, secretKey)
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
        	e.printStackTrace();
        }
		return null;
    }


    private static Date generateExpirationDate(long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
    	initKey();
		System.out.println(generateToken("2", "1"));
		
		String token =   "eyJhbGciOiJIUzUxMiJ9.eyJzZW5kZXJJZCI6IjIiLCJyZWNlaXZlcklkIjoiMSIsImlzcyI6ImxpbmtlZGluLmNoYXQiLCJleHAiOjE1MjM4NTAyMzQsImlhdCI6MTUyMzgxNDIzNCwianRpIjoiMiJ9.RsXt273nfkEcvtiLutXjiS2c_8UbxHJoXg5p2IN9L7jU7nwTjC1Ne3d6B9ohN5yUsRO4jqpaz4-DSzG82rMXQA";
		Jws<Claims> claims = JwtUtilities.getClaims(token);
        System.out.println(claims.getBody().get("senderId"));
	}
}

