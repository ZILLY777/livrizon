package com.server.founder.controller;

import com.server.founder.model.*;
import com.server.founder.request.FileRequest;
import com.server.founder.request.PostRequest;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/load/{url}")
    ResponseEntity<?> load(@RequestHeader String auth, @RequestParam MultipartFile file,@PathVariable String url) {
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.ADMIN);
        if(tokenState == ResponseState.ACCESS) return FileRequest.loadFileApart(file,url);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/like/{url}")
    ResponseEntity<?> fileLike(@RequestHeader String auth, @PathVariable String url){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return FileRequest.putLikeOnFile(auth,url);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @PostMapping("/comment/{url}")
    ResponseEntity<?> saveComment(@RequestHeader String auth, @PathVariable String url, @RequestParam String text){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return FileRequest.saveFileComment(auth,url,text);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/like/{url}")
    ResponseEntity<?> deleteLike(@RequestHeader String auth, @PathVariable String url){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return FileRequest.deleteLikeOnFile(auth,url);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @DeleteMapping("/comment/{comment_id}")
    ResponseEntity<?> deleteComment(@RequestHeader String auth, @PathVariable int comment_id){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return FileRequest.delete(auth,Statement.deleteComment(TableName.file_comments),comment_id);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/{url}")
    private ResponseEntity<?> getFileById(@PathVariable String url) {
        return FileRequest.webFileView(url);
    }
    @GetMapping("/statistic/{url}")
    private ResponseEntity<?> getFileStatistic(@RequestHeader(required = false) String auth,@PathVariable String url){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.PERMIT_ALL);
        if(tokenState == ResponseState.ACCESS) return FileRequest.getFileStatistic(auth,url);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
    @GetMapping("/comments/{url}")
    ResponseEntity<?> getComments(@PathVariable String url,@RequestParam(required = false) Object last){
        return FileRequest.selectComments(url,last);
    }
    @GetMapping("/likes/users/{url}")
    ResponseEntity<?> getUsersLikes(@PathVariable String url,@RequestParam(required = false) Object last){
        return FileRequest.getLikes(null,url,false,last);
    }
    @GetMapping("/likes/subscribes/{url}")
    ResponseEntity<?> getSubscribesLikes(@RequestHeader String auth,@PathVariable String url,@RequestParam(required = false) Object last){
        ResponseState tokenState = JwtUtil.validateToken(auth, TokenType.ACCESS_TOKEN, Role.USER);
        if(tokenState == ResponseState.ACCESS) return FileRequest.getLikes(auth,url,true,last);
        return ResponseEntity.badRequest().body(new Response(tokenState));
    }
}
