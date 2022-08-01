package com.server.founder.controller;

import com.google.gson.Gson;
import com.server.founder.request.LoginRequest;
import com.server.founder.model.*;
import com.server.founder.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AuthorizationController {
    @PostMapping("/login")
    ResponseEntity<?> createAccount(@RequestBody Login login) {
        return LoginRequest.login(login);
    }
    @PostMapping("/confirm")
    ResponseEntity<?> confirmCode(@RequestBody Login login) {
        return LoginRequest.confirmCode(login);
    }
    @PostMapping("/authorization")
    ResponseEntity<?> authorization(@RequestHeader String auth, @RequestParam String registration, @RequestParam(required = false) MultipartFile avatar,@RequestParam(required = false) MultipartFile preview){
        ResponseState tokenState = JwtUtil.validateToken(auth,TokenType.REGISTER_TOKEN,null);
        if(tokenState == ResponseState.ACCESS) return LoginRequest.authorization(auth,new Gson().fromJson(registration, Registration.class),avatar,preview);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/authentication")
    ResponseEntity<?> authentication(@RequestBody Authentication authentication) {
        return LoginRequest.authentication(authentication);
    }

}
