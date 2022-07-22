package com.server.founder.controller;

import com.google.gson.Gson;
import com.server.founder.model.*;
import com.server.founder.request.PostRequest;
import com.server.founder.request.Request;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @PostMapping("/save")
    ResponseEntity<?> savePost(@RequestHeader String auth, @RequestParam(required = false) String page, @RequestParam(required = false) String text, @RequestParam(required = false) List<MultipartFile> files,@RequestParam(required = false) String vote){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.save(auth,page,text, files,new Gson().fromJson(vote, Poll.class));
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/like/{user_post_id}")
    ResponseEntity<?> putLike(@RequestHeader String auth, @PathVariable int user_post_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.putLikeOnPost(auth,user_post_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/comment/{user_post_id}")
    ResponseEntity<?> saveComment(@RequestHeader String auth, @RequestParam String text, @PathVariable int user_post_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.savePostComment(auth,user_post_id,text);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/repost/{user_post_id}")
    ResponseEntity<?> repost(@RequestHeader String auth,@RequestParam(required = false) String page,@PathVariable int user_post_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.repost(auth, user_post_id, page);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/like/{user_post_id}")
    ResponseEntity<?> deleteLike(@RequestHeader String auth, @PathVariable int user_post_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.deleteLikeOnPost(auth,user_post_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/comment/{user_post_id}")
    ResponseEntity<?> deleteComment(@RequestHeader String auth, @PathVariable int user_post_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.delete(auth,Statement.deleteComment(TableName.post_comments),user_post_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/file/{post_id}")
    ResponseEntity<?> deletePostFile(@RequestHeader String auth,@PathVariable int post_id,@RequestParam int file_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.deleteFileFromPost(auth,post_id,file_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/{user_post_id}")
    ResponseEntity<?> getPost(@RequestHeader(required = false) String auth,@PathVariable int user_post_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return PostRequest.getPostById(auth,user_post_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/likes/subscribes/{user_post_id}")
    ResponseEntity<?> getSubscribesLikes(@RequestHeader String auth,@PathVariable int user_post_id,@RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.getPostLikes(auth,user_post_id,true,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/likes/users/{user_post_id}")
    ResponseEntity<?> getUsersLikes(@PathVariable int user_post_id,@RequestParam(required = false) Object last){
        return PostRequest.getPostLikes(null,user_post_id,false,last);
    }
    @GetMapping("/comments/{user_post_id}")
    ResponseEntity<?> getComments(@PathVariable int user_post_id,@RequestParam(required = false) Object last){
        return PostRequest.getComments(user_post_id,last);
    }
    @PutMapping("/edit/{user_post_id}")
    ResponseEntity<?> editPost(@RequestHeader String auth, @PathVariable int user_post_id, @RequestParam(required = false) String text, @RequestParam(required = false) List<MultipartFile> append, @RequestParam(required = false) List<Integer> delete, @RequestParam(required = false) String poll,@RequestParam(required = false) boolean poll_delete){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return PostRequest.editPost(auth,user_post_id,text,append,delete,new Gson().fromJson(poll,Poll.class),poll_delete);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
}
