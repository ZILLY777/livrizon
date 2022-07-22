package com.server.founder.controller;

import com.server.founder.request.SubscribeRequest;
import com.server.founder.model.*;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscribeController {
    @PostMapping("/subscribe/{user_id}")
    ResponseEntity<?> subscribe(@RequestHeader String auth, @PathVariable int user_id) {
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.subscribe(auth,user_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/subscribe/{user_id}")
    ResponseEntity<?> unsubscribe(@RequestHeader String auth, @PathVariable int user_id) {
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.unsubscribe(auth,user_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/follower/{user_id}")
    ResponseEntity<?> listOfFollower(@PathVariable int user_id,@RequestParam(required = false) String last){
        return SubscribeRequest.getSubscribe(user_id, Column.user_id,Column.sub_id,last);
    }
    @GetMapping("/sub/{user_id}")
    ResponseEntity<?> listOfUsers(@PathVariable int user_id,@RequestParam(required = false) String last){
        return SubscribeRequest.getSubscribe(user_id, Column.sub_id,Column.user_id,last);
    }
    @GetMapping("/sub/subscribes/{user_id}")
    ResponseEntity<?> listOf_sub_subscribes(@RequestHeader String auth,@PathVariable int user_id,@RequestParam(required = false) String last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.getMutualSubscribe(auth,user_id,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }

    @GetMapping("/sub/handshakes/{generation}")
    ResponseEntity<?> listOf_sub_handshakes(@RequestHeader String auth,@PathVariable  int generation, @RequestParam(required = false) String last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.getHandshakesSubscribe(auth, generation, last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }

}
