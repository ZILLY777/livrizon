package com.server.founder.request;

import com.server.founder.constant.Constant;
import com.server.founder.function.Function;
import com.server.founder.model.*;
import com.server.founder.response.PreviewAvatars;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.server.founder.function.Function.connect;

public class UserRequest {
    public static ResponseEntity<?> getRelationWithUser(String auth,int user_id,String next){
        ResponseEntity<?> response;
        int owner_id=JwtUtil.extractId(auth);
        if(user_id!=owner_id){
            try {
                Connection connection=Function.connect();
                PreparedStatement getRelationWithUser=connection.prepareStatement(Statement.getRelationWithUser(next));
                getRelationWithUser.setInt(1,user_id);
                getRelationWithUser.setInt(2,user_id);
                getRelationWithUser.setInt(3,user_id);
                getRelationWithUser.setInt(4,owner_id);
                getRelationWithUser.setInt(5,user_id);
                getRelationWithUser.setInt(6,user_id);
                getRelationWithUser.setInt(7,user_id);
                getRelationWithUser.setInt(8,user_id);
                getRelationWithUser.setInt(9,user_id);
                getRelationWithUser.setInt(10,user_id);
                if(next!=null) getRelationWithUser.setObject(11,Integer.parseInt(next));
                ResultSet resultSet=getRelationWithUser.executeQuery();
                List<List<UserProfile>> list=new ArrayList<>();
                while (resultSet.next()){
                    List<UserProfile> users=new ArrayList<>();
                    users.add(new UserProfile(resultSet, TableName.user_one,TableName.user_one_avatar));
                    if(resultSet.getObject(Function.concat(TableName.user_two,Column.user_id))!=null)
                        users.add(new UserProfile(resultSet, TableName.user_two,TableName.user_two_avatar));
                    list.add(users);
                }
                response = ResponseEntity.ok().body(list);
                connection.close();
            } catch (SQLException e){
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }
        }
        else response = ResponseEntity.badRequest().body(new Response(ResponseState.YOURSELF));
        return response;
    }
    public static ResponseEntity<?> deleteUserAvatar(String auth,int file_id){
        try {
            Connection connection=Function.connect();
            PreparedStatement deleteUserAvatar=connection.prepareStatement(Statement.deleteUserAvatar);
            deleteUserAvatar.setInt(1,file_id);
            deleteUserAvatar.setInt(2,JwtUtil.extractId(auth));
            deleteUserAvatar.execute();
            connection.close();
            return ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> findUsersByName(String auth,String name,String next){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            String[] listOfName=name.split(Constant.space,2);
            Object user_id=JwtUtil.extractIdOrNull(auth);
            PreparedStatement findUsersByName=connection.prepareStatement(Statement.findUsersByName(user_id,listOfName,next));
            findUsersByName.setObject(1, user_id);
            findUsersByName.setObject(2, user_id);
            findUsersByName.setObject(3, user_id);
            findUsersByName.setString(4, listOfName[0] + "%");
            if (listOfName.length == 1) {
                findUsersByName.setString(5, listOfName[0] + "%");
                findUsersByName.setObject(6, user_id);
                if (user_id != null) {
                    findUsersByName.setObject(7, user_id);
                    if (next != null) findUsersByName.setInt(8, Integer.parseInt(next));
                }
            } else {
                findUsersByName.setString(5, listOfName[1] + "%");
                findUsersByName.setString(6, listOfName[0] + "%");
                findUsersByName.setString(7, listOfName[1] + "%");
                findUsersByName.setObject(8, user_id);
                if (user_id != null) {
                    findUsersByName.setObject(9, user_id);
                    if (next != null) findUsersByName.setInt(10, Integer.parseInt(next));
                }
            }
            ResultSet resultSet=findUsersByName.executeQuery();
            List<UserProfile> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new UserProfile(resultSet,TableName.users,TableName.user_avatar));
            }
            response = ResponseEntity.ok().body(list);
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> loadAvatar(String auth, MultipartFile file, MultipartFile preview){
        ResponseEntity<?> response;
        if(file.getContentType().substring(0,5).equals(Constant.image) && preview.getContentType().substring(0,5).equals(Constant.image)){
            try {
                Connection connection=Function.connect();
                int user_id=JwtUtil.extractId(auth);
                FileRequest.loadAvatar(user_id, FileRequest.loadFile(file,true,user_id,connection),connection);
                FileRequest.loadPreviewAvatar(user_id, FileRequest.loadFile(preview,true,user_id,connection),connection);
                response = ResponseEntity.badRequest().body(new Response(ResponseState.SUCCESS));
                connection.close();
            } catch (SQLException e){
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            } catch (IOException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.FILE_ERROR));
            }
        }
        else response = ResponseEntity.badRequest().body(new Response(ResponseState.TYPE_ERROR));
        return response;
    }
    public static ResponseEntity<?> getUserAvatarsView(String auth,int user_id){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            PreviewAvatars avatarsModel = new PreviewAvatars();
            avatarsModel.setNumber(Request.getItemNumber(TableName.user_avatars,Column.user_id,user_id,connection));
            avatarsModel.setFiles(Request.getUserAvatars(auth,user_id,null,connection));
            response = ResponseEntity.ok().body(avatarsModel);
            connection.close();
        } catch (Exception e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> getUserAvatars(String auth,int user_id,Object last){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            response=ResponseEntity.ok().body(Request.getUserAvatars(auth,user_id,last,connection));
            connection.close();
        } catch (Exception e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> setMyInterests(String auth,List<Integer> append){
        int owner_id=JwtUtil.extractId(auth);
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            append=append.stream().distinct().collect(Collectors.toList());
            PreparedStatement setMyInterest=connection.prepareStatement(Statement.setMyInterest(Function.toValues(append.size())));
            setMyInterest.setInt(1,owner_id);
            for (int i=0;i<append.size();i++){
                setMyInterest.setInt(i+2,append.get(i));
            }
            setMyInterest.execute();
            response=ResponseEntity.ok(new Response(ResponseState.SUCCESS));
            connection.close();
        } catch (Exception e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }


    public static ResponseEntity<?> changeMyInterests(String auth,List<Integer> append, List<Integer> delete){
        ResponseEntity<?> response;
        if(append!=null || delete!=null){
            try {
                int owner_id=JwtUtil.extractId(auth);
                Connection connection=Function.connect();
                if(append!=null){
                    append=append.stream().distinct().collect(Collectors.toList());
                    PreparedStatement setMyInterest=connection.prepareStatement(Statement.setMyInterest(Function.toValues(append.size())));
                    setMyInterest.setInt(1,owner_id);
                    for (int i=0;i<append.size();i++){
                        setMyInterest.setInt(i+2,append.get(i));
                    }
                    setMyInterest.execute();
                }
                if (delete!=null){
                    delete=delete.stream().distinct().collect(Collectors.toList());
                    PreparedStatement delMyInterest=connection.prepareStatement((Statement.delMyInterest+Function.toValues(delete.size())));
                    delMyInterest.setInt(1,owner_id);
                    for (int i=0;i<delete.size();i++){
                        delMyInterest.setInt(i+2, delete.get(i));
                    }
                    delMyInterest.execute();
                }
                response=ResponseEntity.ok(new Response(ResponseState.SUCCESS));
                connection.close();

            } catch (Exception e){
                System.out.println(e);
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));

            }
        }
        else response = ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY));
        return response;
    }
    public static ResponseEntity<?> getFileFromUserPage(int user_id,Object last){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            response=ResponseEntity.ok().body(findFilesFromPage(user_id,last,connection));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static int selectNumberFilesOfPage(int user_id,Connection connection) throws SQLException {
        PreparedStatement selectNumberFilesOfPage=connection.prepareStatement(Statement.selectNumberOfFileFromPage);
        selectNumberFilesOfPage.setInt(1,user_id);
        ResultSet resultSet=selectNumberFilesOfPage.executeQuery();
        resultSet.next();
        return resultSet.getInt(Column.number);
    }
    public static List<FileView> findFilesFromPage(int user_id,Object last,Connection connection) throws SQLException {
        PreparedStatement selectPreviewFileOfPage=connection.prepareStatement(Statement.selectPreviewFilesOfPage+
                Statement.andFindByLess(Column.post_file_id,last)+
                Statement.limit(15)
        );
        selectPreviewFileOfPage.setInt(1,user_id);
        ResultSet resultSet=selectPreviewFileOfPage.executeQuery();
        List<FileView> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(new FileView(resultSet));
        }
        if(list.size()>0) return list;
        else return null;
    }
    public static PageInformation findPageInformation(Object owner_id, int user_id, Connection connection) throws SQLException {
        PreparedStatement selectPageInformation=connection.prepareStatement(Statement.selectPageInformation);
        selectPageInformation.setObject(1,owner_id);
        selectPageInformation.setObject(2,owner_id);
        selectPageInformation.setInt(3,user_id);
        selectPageInformation.setInt(4,user_id);
        selectPageInformation.setObject(5,owner_id);
        selectPageInformation.setInt(6,user_id);
        selectPageInformation.setInt(7,user_id);
        selectPageInformation.setInt(8,user_id);
        selectPageInformation.setInt(9,user_id);
        ResultSet resultSet=selectPageInformation.executeQuery();
        if(resultSet.next()) return new PageInformation(resultSet);
        else return null;
    }

    public static ResponseEntity<?> getPage(String auth,int user_id){
        ResponseEntity<?> response;
        try {
            Object owner_id=JwtUtil.extractIdOrNull(auth);
            Connection connection= Function.connect();
            response = ResponseEntity.ok().body(new Page(
                    findPageInformation(owner_id,user_id,connection),
                    Request.getPostsBy(user_id,TableName.user_posts,null, Column.user_id,user_id,null)
            ));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static User findUserBy(String tableName,String column,Object object,Connection connect) throws SQLException {
        Connection connection;
        if(connect!=null) connection=connect;
        else connection=Function.connect();
        PreparedStatement findUserBy=connection.prepareStatement(Statement.findItemBy(tableName, Column.all,column));
        findUserBy.setObject(1,object);
        ResultSet resultSet=findUserBy.executeQuery();
        User user;
        if(resultSet.next()) user=new User(resultSet);
        else user=null;
        if(connect==null) connection.close();
        return user;
    }
    public static Login findLoginByUserName(String username, Connection connect) throws SQLException {
        Connection connection;
        if(connect!=null) connection=connect;
        else connection = connect();
        PreparedStatement findLoginByUsername=connection.prepareStatement(Statement.findItemBy(TableName.login, Column.all, Column.username));
        findLoginByUsername.setString(1, username);
        ResultSet resultSet=findLoginByUsername.executeQuery();
        Login login;
        if(resultSet.next()) login =new Login(resultSet);
        else login =null;
        if(connect==null) connection.close();
        return login;
    }
}
