package com.server.founder.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.server.founder.function.Encrypt;
import com.server.founder.model.*;
import com.server.founder.function.Function;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
    public static String SecretKey="RgUkXp2s5u8x/A?D";

    public static String extractUsername(String token) {
        return extractAllClaims(token).get("sub").toString();
    }
    public static Object extractIdOrNull(String token){
        return switch (token) {
            case null -> null;
            default -> extractId(token);
        };
    }
    public static int extractId(String token) {
        return (int)(double) extractAllClaims(token).get("id");
    }
    public static int extractRegistration(String token) {
        return (int)(double) extractAllClaims(token).get("registration");
    }
    public static Role extractRole(String token) {
        return Role.valueOf(extractAllClaims(token).get("role").toString());
    }

    public static TokenType extractTokenType(String token){
        return TokenType.valueOf(extractAllClaims(token).get("type").toString());
    }
    public static boolean isTokenNotExpired(String token) {
        Object exp=extractAllClaims(token).get("exp");
        return exp == null || !((long)(double) exp < System.currentTimeMillis());
    }
    public static String generateAccessToken(int user_id,UserType type) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", user_id);
        claim.put("role", type);
        claim.put("type","ACCESS_TOKEN");
        return createToken(claim, 3L * 60 * 60 * 1000000000);
    }
    public static String generateTemporaryToken(Login login) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Map<String, Object> claim = new HashMap<>();
        claim.put("type","REGISTER_TOKEN");
        claim.put("registration",login.getRegistration());
        claim.put("sub",login.getUsername());
        return createToken(claim, 60L * 60 * 1000000000);
    }
    public static boolean validSign(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        String[] jwt=token.split("\\.");
        return jwt.length == 2 && Encrypt.crypt(jwt[0]).equals(jwt[1]);
    }
    public static String createToken(Map<String,Object> claim,long expired) throws NoSuchAlgorithmException, InvalidKeyException {
        String token = "";
        String hash;
        long date=System.currentTimeMillis();
        claim.put("iat",date);
        claim.put("exp",date+expired);
        token+=Encrypt.base64Encode(claim);
        hash=Encrypt.crypt(token);
        token+="."+hash;
        return token;
    }
    public static HashMap extractAllClaims(String toke){
        String claims=toke.split("\\.")[0];
        String json=Encrypt.base64Decode(claims);
        return new Gson().fromJson(json,HashMap.class);
    }
    public static ResponseState validateToken(String token, TokenType type, Role role) {
        if(role==Role.PERMIT_ALL && token==null) return ResponseState.ACCESS;
        else if(token==null) return ResponseState.EMPTY;
        else try {
            if(!validSign(token)) return ResponseState.INCORRECT_TOKEN_SIGN;
            else if(!isTokenNotExpired(token)) return ResponseState.TOKEN_EXPIRED;
            else if(extractTokenType(token)!=type) return ResponseState.INCORRECT_TOKEN_TYPE;
            else if(type==TokenType.ACCESS_TOKEN && !Function.roleFilter(token,role)) return ResponseState.DENIED;
            else return ResponseState.ACCESS;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return ResponseState.TOKEN_ERROR;
        }
    }

}