package com.server.founder.request;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

import static com.server.founder.function.Function.connect;
@Service
public class LoginRequest {
    public static ResponseEntity<?> authorization (String auth, Registration registration, MultipartFile avatar, MultipartFile preview) {
        ResponseEntity<?> response;
        if(!(registration.getRole()==Role.USER || registration.getRole()==Role.COMPANY))
            response=ResponseEntity.badRequest().body(new Response(ResponseState.REGISTRATION_TYPE_ERROR));
        else if(!(avatar==null && preview==null || avatar!=null && preview!=null))
            response=ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY_FILE));
        else if(avatar!= null && avatar.getSize()>0 && avatar.getSize()<Function.toMB(100) && preview.getSize()>0 && preview.getSize()<Function.toMB(100) && preview.getSize()<avatar.getSize())
            response=ResponseEntity.badRequest().body(new Response(ResponseState.SIZE_ERROR));
        else if(registration.getName()==null)
            response=ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY_NAME));
        else if(registration.getName().length()>50)
            response=ResponseEntity.badRequest().body(new Response(ResponseState.LENGTH_ERROR));
        else {
            try {
                Connection connection=Function.connect();
                HashMap token=JwtUtil.extractAllClaims(auth);
                String username=String.valueOf(token.get(Column.sub));
                if (!Request.ItemExist(TableName.users, Column.username,username,connection)){
                    PreparedStatement insertUser=connection.prepareStatement(Statement.insertUser(registration.getRole()));
                    insertUser.setString(2,username);
                    insertUser.setString(3,Encrypt.getHash(registration.getPassword()));
                    insertUser.setString(4,String.valueOf(registration.getRole()));
                    insertUser.setString(5,String.valueOf(token.get(Column.registration)));
                    insertUser.setString(6,registration.getName());
                    insertUser.setString(7, registration.getDescription());
                    insertUser.setInt(8, registration.getCity_id());
                    if(registration.getRole()==Role.USER){
                        insertUser.setDate(9,null);
                        insertUser.setString(10,String.valueOf(registration.getGender()));
                        insertUser.setString(11, registration.getHobbies());
                        insertUser.setString(12, registration.getSkills());
                        insertUser.setString(13, registration.getQualities());
                    }
                    int user_id=Request.tableIndex(TableName.users, Column.user_id,connection)+1;
                    insertUser.setInt(1,user_id);
                    insertUser.execute();
                    if(avatar!=null) {
                        FileRequest.loadAvatar(user_id, FileRequest.loadFile(avatar, true, user_id, connection), connection);
                        FileRequest.loadPreviewAvatar(user_id, FileRequest.loadFile(preview,false,user_id,connection),connection);
                    }
                    response = ResponseEntity.ok().body(new Jwt(JwtUtil.generateAccessToken(user_id,registration.getRole())));
                }
                else response=ResponseEntity.badRequest().body(new Response(ResponseState.ALREADY_EXIST));
                connection.close();
            } catch (SQLException e) {
                System.out.println(e);
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            } catch (IOException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.FILE_ERROR));
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.TOKEN_EXCEPTION));
            }
        }
        return response;
    }
    public static ResponseEntity<?> confirmCode(Login confirm) {
        ResponseEntity<?> responseEntity;
        try {
            Connection connection=connect();
            Login login = UserRequest.findLogin(confirm.getUsername(),connection);
            if(login!=null) {
                if(login.getCode()==confirm.getCode()){
                    PreparedStatement dropEvent = connection.prepareStatement(Statement.dropEvent(String.format("login_%s",confirm.getUsername())));
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
                PreparedStatement dropEvent = connection.prepareStatement(Statement.dropEvent(String.format("login_%s",login.getUsername())));
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
    public static ResponseEntity<?> authentication(Authentication authentication) {
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            PreparedStatement findTokenInformation=connection.prepareStatement(Statement.findTokenInformation);
            findTokenInformation.setObject(1,authentication.getUsername());
            findTokenInformation.setObject(2,Encrypt.getHash(authentication.getPassword()));
            ResultSet resultSet=findTokenInformation.executeQuery();
            if(resultSet.next())
                response = ResponseEntity.ok().body(new Jwt(JwtUtil.generateAccessToken(resultSet.getInt(Column.user_id),Role.valueOf(resultSet.getString(Column.role)))));
            else
                response = ResponseEntity.badRequest().body(new Response(ResponseState.INCORRECT_LOGIN_PASSWORD));
            connection.close();
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
}
