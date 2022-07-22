package com.server.founder.controller;

import com.server.founder.model.Response;
import com.server.founder.model.ResponseState;
import com.server.founder.model.Role;
import com.server.founder.model.TokenType;
import com.server.founder.request.UserRequest;
import com.server.founder.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindController {
    @GetMapping("/users/find")
    ResponseEntity<?> findUsersByName(@RequestHeader(required = false) String auth, @RequestParam String name, @RequestParam(required = false) String next){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return UserRequest.findUsersByName(auth,name,next);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
}
