package com.server.founder.request;

import com.server.founder.function.Function;
import com.server.founder.model.*;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.server.founder.function.Function.connect;

public class FileRequest {
    
    public static ResponseEntity<?> webFileView(String url){
        try {
            FileDb fileDb =findFileByUrl(url);
            if(fileDb !=null) return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(fileDb.getContentType()))
                    .contentLength(fileDb.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(fileDb.getBytes())));
            else return ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
        }catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static FileDb findFileByUrl(String url) throws SQLException {
        FileDb fileDb;
        Connection connection=connect();
        PreparedStatement findFileByUrl=connection.prepareStatement(Statement.findItemBy(TableName.files, Column.all, Column.url));
        findFileByUrl.setString(1,url);
        ResultSet resultSet=findFileByUrl.executeQuery();
        if(resultSet.next()) fileDb = new FileDb(resultSet);
        else fileDb =null;
        connection.close();
        return fileDb;
    }
    public static ResponseEntity<?> getFileStatistic(String auth,String url){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            PreparedStatement getFileInformation=connection.prepareStatement(Statement.getFileStatistic);
            getFileInformation.setObject(1,JwtUtil.extractIdOrNull(auth));
            getFileInformation.setString(2,url);
            ResultSet resultSet=getFileInformation.executeQuery();
            if(resultSet.next()) response = ResponseEntity.ok().body(new FileInformation(resultSet));
            else response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> getLikes(String auth,String url,boolean subscribes,Object last){
        try {
            Connection connection=Function.connect();
            PreparedStatement getFileLikes=connection.prepareStatement(Statement.getFileLikes(subscribes,last));
            if(subscribes){
                getFileLikes.setInt(1, JwtUtil.extractId(auth));
                getFileLikes.setString(2, url);
                if (last != null) getFileLikes.setObject(3, last);
            }
            else {
                getFileLikes.setString(1, url);
                if (last != null) getFileLikes.setObject(2, last);
            }
            ResultSet resultSet=getFileLikes.executeQuery();
            List<Like> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new Like(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    private  static ResultSet checkFileComment(String url,Connection connection) throws SQLException {
        PreparedStatement checkFileComment=connection.prepareStatement(Statement.checkFileComment);
        checkFileComment.setString(1,url);
        return checkFileComment.executeQuery();
    }
    public static ResponseEntity<?> selectComments(String url,Object last){
        try {
            Connection connection=Function.connect();
            PreparedStatement selectFileComments=connection.prepareStatement(Statement.selectFileComments(last));
            selectFileComments.setString(1,url);
            if(last!=null) selectFileComments.setObject(2,last);
            ResultSet resultSet=selectFileComments.executeQuery();
            List<CommentProfile> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new CommentProfile(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> deleteLikeOnFile(String auth,int url){
        try {
            Connection connection=Function.connect();
            PreparedStatement deleteLikeOnFile=connection.prepareStatement(Statement.deleteLikeOnFile);
            deleteLikeOnFile.setInt(1,url);
            deleteLikeOnFile.setInt(2,JwtUtil.extractId(auth));
            deleteLikeOnFile.execute();
            connection.close();
            return ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> delete(String auth,String statement,int id){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            Request.delete(JwtUtil.extractId(auth),statement,id,connection);
            response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> saveFileComment(String auth,String url,String text){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            ResultSet resultSet=checkFileComment(url,connection);
            if(resultSet.next()){
                if(!resultSet.getBoolean(Column.status)) response = ResponseEntity.badRequest().body(new Response(ResponseState.NO_ACTIVE));
                else {
                    Request.saveComment(auth,TableName.file_comments,Column.file_id,resultSet.getInt(Column.file_id),text,connection);
                    response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                }
            }
            else response = ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> putLikeOnFile(String auth,String url){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            ResultSet resultSet=Request.checkLike(user_id,url,Statement.checkFileLike,connection);
            if(resultSet.next()){
                if(!resultSet.getBoolean(Column.status)) response = ResponseEntity.badRequest().body(new Response(ResponseState.NO_ACTIVE));
                else if(resultSet.getBoolean(Column.my_like)) response = ResponseEntity.badRequest().body(new Response(ResponseState.ALREADY_EXIST));
                else {
                    Request.putLike(TableName.file_likes,Column.file_id,user_id,resultSet.getInt(Column.file_id),connection);
                    response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                }
            }
            else response = ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> loadFileApart(String auth,MultipartFile file,String url){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            if(!Request.ItemExist(TableName.files,Column.url,url,connection)) {
                loadFile(file, url, false, JwtUtil.extractId(auth), connection);
                response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
            }
            else response = ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
            connection.close();
        } catch (SQLException e) {
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        } catch (IOException e) {
            response = ResponseEntity.badRequest().body(new Response(ResponseState.FILE_ERROR));
        }
        return response;
    }
    public static void loadAvatar(int user_id,Object file_id,Connection connection) throws SQLException, IOException {
        PreparedStatement insertAvatar = connection.prepareStatement(Statement.insertAvatar);
        insertAvatar.setInt(1, user_id);
        insertAvatar.setObject(2, file_id);
        insertAvatar.execute();
    }
    public static void loadPreviewAvatar(int user_id,Object preview,Connection connection) throws SQLException, IOException {
        PreparedStatement insertAvatar = connection.prepareStatement(Statement.insertPreviewAvatar);
        insertAvatar.setInt(1, user_id);
        insertAvatar.setObject(2, preview);
        insertAvatar.execute();
    }
    public static Object loadFile(MultipartFile file,boolean status,int user_id,Connection connection) throws SQLException, IOException {
        return loadFile(file,String.valueOf(UUID.randomUUID()),status,user_id,connection);
    }
    public static Object loadFile(MultipartFile file,String url,boolean status,Object user_id, Connection connection) throws SQLException, IOException {
        PreparedStatement insertFile= connection.prepareStatement(Statement.insertFile);
        insertFile.setString(1,url);
        insertFile.setString(2,file.getContentType());
        insertFile.setLong(3,file.getSize());
        insertFile.setBoolean(4,status);
        insertFile.setObject(5,user_id);
        insertFile.setBytes(6,file.getBytes());
        insertFile.execute();
        return Request.getItemId(TableName.files, Column.file_id, Column.url,url,connection);
    }
}
