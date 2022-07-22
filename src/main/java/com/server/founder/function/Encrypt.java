package com.server.founder.function;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.server.founder.security.JwtUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

public class Encrypt {
    public static String crypt(String str) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec s_key = new SecretKeySpec(JwtUtil.SecretKey.getBytes(),"HmacSHA256");
        sha256.init(s_key);
        return base64Encode(sha256.doFinal(str.getBytes()));
    }
    public static String base64Encode (byte[] str){
        Base64.Encoder encoder=Base64.getEncoder();
        byte[] encoded=encoder.encode(str);
        return new String(encoded);
    }
    public static String base64Encode (Map<String, Object> str) {
        Base64.Encoder encoder=Base64.getEncoder();
        byte[] encoded=encoder.encode(new Gson().toJson(str).getBytes());
        return new String(encoded);
    }
    public static String base64Decode (String str){
        Base64.Decoder decoder=Base64.getDecoder();
        byte[] decoded=decoder.decode(str.getBytes());
        return new String(decoded);
    }
    public static String getHash(String str){
        try {
            StringBuilder hash= new StringBuilder();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(str.getBytes());
            for (byte b: bytes){
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        }catch (Exception e){
            return null;
        }

    }
}