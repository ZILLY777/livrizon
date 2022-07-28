package com.server.founder.controller;

import com.server.founder.model.Response;
import com.server.founder.model.ResponseState;
import com.server.founder.model.Role;
import com.server.founder.model.TokenType;
import com.server.founder.request.RecommendationRequest;
import com.server.founder.request.UserRequest;
import com.server.founder.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecommendationController {
    @GetMapping("/users/recommendation")
    ResponseEntity<?> getRecommendationUsers(@RequestHeader String auth, @RequestParam(required = false) String next){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return RecommendationRequest.getRecommendationUsers(auth,next);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/users/possible")
    ResponseEntity<?> getPossibleUsers(@RequestHeader String auth, @RequestParam(required = false) String next){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return RecommendationRequest.getPossibleUsers(auth,next);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/users/popular")
    ResponseEntity<?> getPopularUsers(@RequestHeader String auth, @RequestParam(required = false) String next){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return RecommendationRequest.getPopularUsers(auth,next);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }

}
