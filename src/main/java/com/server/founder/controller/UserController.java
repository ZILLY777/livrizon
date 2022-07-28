package com.server.founder.controller;

import com.server.founder.model.Response;
import com.server.founder.model.ResponseState;
import com.server.founder.model.Role;
import com.server.founder.model.TokenType;
import com.server.founder.request.PostRequest;
import com.server.founder.request.UserRequest;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/avatar/load")
    ResponseEntity<?> loadAvatar(@RequestHeader String auth,@RequestParam MultipartFile file,@RequestParam MultipartFile preview){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return UserRequest.loadAvatar(auth,file,preview);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/avatar/delete/{file_id}")
    ResponseEntity<?> deleteUserAvatar(@RequestHeader String auth,@PathVariable int file_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return UserRequest.deleteUserAvatar(auth,file_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/avatars/view/{user_id}")
    ResponseEntity<?> getUserAvatarsView(@RequestHeader(required = false) String auth, @PathVariable int user_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return UserRequest.getUserAvatarsView(auth,user_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/avatars/{user_id}")
    ResponseEntity<?> getUserAvatars(@RequestHeader(required = false) String auth, @PathVariable int user_id,@RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return UserRequest.getUserAvatars(auth,user_id,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/page/files/{user_id}")
    ResponseEntity<?> getFileFromUserPage(@PathVariable int user_id,@RequestParam(required = false) Object last){
        return UserRequest.getFileFromUserPage(user_id,last);
    }
    @GetMapping("/page/{user_id}")
    ResponseEntity<?> getUserPage(@RequestHeader(required = false) String auth,@PathVariable int user_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return UserRequest.getPage(auth,user_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/posts/{user_id}")
    ResponseEntity<?> getUserPosts(@RequestHeader(required = false) String auth,@PathVariable int user_id, @RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return PostRequest.getPostsBy(JwtUtil.extractIdOrNull(auth),TableName.user_posts,null, Column.user_id,user_id,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
}
