package com.server.founder.controller;

import com.server.founder.model.*;
import com.server.founder.request.PollRequest;
import com.server.founder.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PollController {
    @PostMapping("/poll/save")
    ResponseEntity<?> savePoll(@RequestHeader String auth,@RequestBody Poll poll){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.ADMIN);
        if(tokenState == ResponseState.ACCESS) return PollRequest.savePoll(auth, poll);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/vote/single/{poll_id}/{line_id}")
    ResponseEntity<?> singleVote(@RequestHeader String auth, @PathVariable int poll_id,@PathVariable int line_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PollRequest.putSingleVote(auth,poll_id,line_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/vote/multiple/{poll_id}")
    ResponseEntity<?> multipleVote(@RequestHeader String auth, @PathVariable int poll_id,@RequestParam(required = false) List<Integer> append,@RequestParam(required = false) List<Integer> delete){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PollRequest.putMultipleVote(auth,poll_id,append,delete);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/line/subscribes/{line_id}")
    ResponseEntity<?> getSubscribesPollLine(@RequestHeader String auth,@PathVariable int line_id,@RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PollRequest.getUserLine(auth,line_id,true,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/line/users/{line_id}")
    ResponseEntity<?> getUsersPollLine(@RequestHeader String auth,@PathVariable int line_id,@RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PollRequest.getUserLine(auth,line_id,false,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/poll/{poll_id}")
    ResponseEntity<?> getVote(@RequestHeader(required = false) String auth,@PathVariable int poll_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return PollRequest.getPoll(auth,poll_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PutMapping("/poll/edit/{poll_id}")
    ResponseEntity<?> editPoll(@RequestHeader String auth, @PathVariable int poll_id, @RequestBody EditPoll editPoll){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PollRequest.editPoll(auth,poll_id,editPoll);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
}
