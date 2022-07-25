package com.server.founder.controller;

import com.server.founder.model.Response;
import com.server.founder.model.ResponseState;
import com.server.founder.model.Role;
import com.server.founder.model.TokenType;
import com.server.founder.request.ChatRequest;
import com.server.founder.request.SubscribeRequest;
import com.server.founder.request.PostRequest;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OwnerController {
    @GetMapping("/my_sub")
    ResponseEntity<?> mySub(@RequestHeader String auth,@RequestParam int sort,@RequestParam(required = false) String next){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.getMyFollowing(auth,sort,next);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/wall")
    ResponseEntity<?> getWall(@RequestHeader String auth, @RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) {
            Object user_id=JwtUtil.extractIdOrNull(auth);
            return PostRequest.getPostsBy(user_id, TableName.subscribes, Statement.bySubscribe, Column.user_id,user_id, last);
        }
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/my_likes/posts")
    ResponseEntity<?> getMyLikePosts(@RequestHeader String auth, @RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) {
            int user_id=JwtUtil.extractId(auth);
            return PostRequest.getPostsBy(user_id, TableName.post_likes, Statement.byUserLikes, Column.user_id, user_id, last);
        }
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/my_likes/files")
    ResponseEntity<?> getMyLikeFiles(@RequestHeader String auth, @RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);

        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/chats")
    ResponseEntity<?> getChatUsers(@RequestHeader String auth,@RequestParam(required = false) String next){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return ChatRequest.getUserChats(auth,next);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/connection/mutual/{user_id}")
    ResponseEntity<?> getMutualConnection(@RequestHeader String auth, @PathVariable int user_id, @RequestParam(required = false) String last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.getMutualConnection(auth,user_id,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/handshakes/{generation}")
    ResponseEntity<?> getHandshakesSubscribe(@RequestHeader String auth,@PathVariable int generation, @RequestParam(required = false) String last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return SubscribeRequest.getHandshakes(auth, generation, last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    //@GetMapping("/relation/{user_id}")
    //ResponseEntity<?> getRelationWithUser(@RequestHeader String auth,@PathVariable int user_id){
    //    ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
    //    if(tokenState == ResponseState.ACCESS) return SubscribeRequest.getRelationWithUser(auth,user_id);
    //    return ResponseEntity.badRequest().body(new Response(tokenState));
    //}
}
