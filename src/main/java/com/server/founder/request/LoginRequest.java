package com.server.founder.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.founder.constant.Constant;
import com.server.founder.function.Encrypt;
import com.server.founder.function.Function;
import com.server.founder.model.*;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

import static com.server.founder.function.Function.connect;
import static com.server.founder.request.UserRequest.findLoginByUserName;
@Service
public class LoginRequest {
    public static ResponseEntity<?> authorization (String auth,User user, MultipartFile file) {
        ResponseEntity<?> response;
        if((user.getLast_name().length()<=20 && !user.getLast_name().contains(Constant.space)) && (user.getFirst_name().length()<20 && !user.getFirst_name().contains(Constant.space))){
            try {
                Connection connection=connect();
                HashMap token=JwtUtil.extractAllClaims(auth);
                String username=String.valueOf(token.get(Column.sub));
                if (!Request.ItemExist(TableName.users, Column.username,username,connection)){
                    PreparedStatement insertUser=connection.prepareStatement(Statement.insertUser);
                    insertUser.setString(2,username);
                    insertUser.setString(3,Encrypt.getHash(user.getPassword()));
                    insertUser.setString(4,String.valueOf(token.get(Column.registration)));
                    insertUser.setString(5,user.getFirst_name());
                    insertUser.setString(6,user.getLast_name());
                    int user_id=Request.tableIndex(TableName.users, Column.user_id,connection)+1;
                    insertUser.setInt(1,user_id);
                    insertUser.execute();
                    if(file!=null && file.getSize()<Function.toMB(100)) FileRequest.loadAvatar(user_id,FileRequest.loadFile(file,true,user_id, connection),connection);
                    user.setUser_id(user_id);
                    user.setRole(Role.USER);
                    user.setUsername(username);
                    response = ResponseEntity.ok().body(new Jwt(JwtUtil.generateToken(user)));
                }
                else response=ResponseEntity.badRequest().body(new Response(ResponseState.ALREADY_EXIST));
                connection.close();
            } catch (SQLException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            } catch (IOException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.FILE_ERROR));
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.TOKEN_EXCEPTION));
            }
        }
        else if(user.getLast_name().length()>20 || user.getFirst_name().length()>20) response=ResponseEntity.badRequest().body(new Response(ResponseState.LENGTH_ERROR));
        else response=ResponseEntity.badRequest().body(new Response(ResponseState.TEXT_ERROR));
        return response;
    }
    public static ResponseEntity<?> confirmCode(Login confirm) {
        ResponseEntity<?> responseEntity;
        try {
            Connection connection=connect();
            Login login = findLoginByUserName(confirm.getUsername(),connection);
            if(login!=null) {
                if(login.getCode()==confirm.getCode()){
                    PreparedStatement dropEvent = connect().prepareStatement(Statement.dropEvent(String.format("login_%s",confirm.getUsername())));
                    dropEvent.execute();
                    PreparedStatement deleteFromLogin=connection.prepareStatement(Statement.deleteFromLogin);
                    deleteFromLogin.setString(1,confirm.getUsername());
                    deleteFromLogin.execute();
                    responseEntity = ResponseEntity.ok().body(new Jwt(JwtUtil.generateTemporaryToken(login)));
                }
                else responseEntity = ResponseEntity.badRequest().body(new Response(ResponseState.INCORRECT_CODE));
            }
            else responseEntity = ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
            connection.close();
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            responseEntity = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return responseEntity;
    }
    public static ResponseEntity<?> login(Login login) {
        ResponseEntity<?> response;
        try {
            Connection connection = connect();
            if (!Request.ItemExist(TableName.users, Column.username,login.getUsername(),connection)){
                PreparedStatement replaceLogin=connection.prepareStatement(Statement.replaceLogin);
                replaceLogin.setString(1, login.getUsername());
                replaceLogin.setInt(2, Function.random(1000,9000));
                replaceLogin.setString(3, String.valueOf(login.getRegistration()));
                replaceLogin.execute();
                PreparedStatement dropEvent= connect().prepareStatement(Statement.dropEvent(String.format("login_%s",login.getUsername())));
                dropEvent.execute();
                PreparedStatement launchLoginEvent=connection.prepareStatement(Statement.launchLoginEvent(login.getUsername()));
                launchLoginEvent.execute();
                response = ResponseEntity.ok().body(new Response(ResponseState.CODE_SEND));
            }
            else response = ResponseEntity.badRequest().body(new Response(ResponseState.ALREADY_EXIST));
            connection.close();
        } catch (SQLException e) {
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> authentication(Authentication authenticationRequest) {
        try {
            User user = UserRequest.findUserBy(TableName.users, Column.username,authenticationRequest.getUsername(),null);
            if(user==null) return ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
            else {
                if(Objects.equals(Encrypt.getHash(authenticationRequest.getPassword()), user.getPassword())) return ResponseEntity.ok().body(new Jwt(JwtUtil.generateToken(user)));
                else return ResponseEntity.badRequest().body(new Response(ResponseState.INCORRECT_PASSWORD));
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }

    }
}
