package com.server.founder.controller;

import com.server.founder.model.Response;
import com.server.founder.model.ResponseState;
import com.server.founder.model.Role;
import com.server.founder.model.TokenType;
import com.server.founder.request.ChatRequest;
import com.server.founder.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class PrivetChatController {
    @PostMapping("/user/{user_id}")
    ResponseEntity<?> sendMessage(@RequestHeader String auth,@PathVariable int user_id,@RequestParam String text){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return ChatRequest.sendMessage(auth,user_id,text);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/user/{user_id}")
    ResponseEntity<?> getPrivetChat(@RequestHeader String auth,@PathVariable int user_id,@RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return ChatRequest.getChat(auth,user_id,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
}
