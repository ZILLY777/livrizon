package com.server.founder.request;

import com.server.founder.function.Function;
import com.server.founder.model.*;
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
import java.util.stream.Collectors;


public class PostRequest {
    public static ResponseEntity<?> deleteFileFromPost(String auth,int post_id,int file_id){
        try {
            Connection connection=Function.connect();
            PreparedStatement deleteFileFromPost=connection.prepareStatement(Statement.deleteFileFromPost);
            deleteFileFromPost.setInt(1,file_id);
            deleteFileFromPost.setInt(2,post_id);
            deleteFileFromPost.setInt(3,JwtUtil.extractId(auth));
            deleteFileFromPost.execute();
            connection.close();
            return ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static List<AvatarOwner> selectMySubOfPost(int post_id, int user_id,Connection connection) throws SQLException {
        PreparedStatement selectMySubOfPost=connection.prepareStatement(Statement.selectMySubOfPost);
        selectMySubOfPost.setInt(1,post_id);
        selectMySubOfPost.setInt(2,user_id);
        ResultSet resultSet=selectMySubOfPost.executeQuery();
        List<AvatarOwner> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(new AvatarOwner(Column.user_id,resultSet));
        }
        if(list.size()>0) return list;
        else return null;
    }
    public static ResponseEntity<?> getPostLikes(String auth,int item,boolean subscribes,Object last){
        try {
            Connection connection=Function.connect();
            PreparedStatement getPostLikes=connection.prepareStatement(Statement.getPostLikes(subscribes,last));
            if(subscribes){
                getPostLikes.setInt(1,JwtUtil.extractId(auth));
                getPostLikes.setInt(2,item);
                if(last!=null) getPostLikes.setObject(3,last);
            }
            else {
                getPostLikes.setInt(1,item);
                if(last!=null) getPostLikes.setObject(2,last);
            }
            ResultSet resultSet=getPostLikes.executeQuery();
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
    public static ResultSet checkPost(int user_post_id,int user_id,Connection connection) throws SQLException {
        PreparedStatement checkPost=connection.prepareStatement(Statement.checkPost);
        checkPost.setInt(1,user_post_id);
        checkPost.setInt(2,user_id);
        return checkPost.executeQuery();
    }
    private static List<Integer> saveFiles(List<MultipartFile> files,boolean status,int user_id,Connection connection) throws SQLException, IOException {
        List<Integer> listOfFile=new ArrayList<>();
        if(files!=null){
            for (int i=files.size()-1;i>=0;i--){
                if(files.get(i).getSize() < Function.toMB(100)) {
                    Object fileId=FileRequest.loadFile(files.get(i), status,user_id,connection);
                    if(fileId!=null) listOfFile.add((Integer) fileId);
                }
            }
        }
        return listOfFile;
    }
    private static void deletePostFiles(int id, List<Integer> files, Connection connection) throws SQLException {
        if(files!=null){
            files=files.stream().distinct().collect(Collectors.toList());
            PreparedStatement deletePostFiles=connection.prepareStatement(Statement.deletePostFiles+Function.toValues(files.size()));
            deletePostFiles.setInt(1,id);
            for (int i=0;i<files.size();i++){
                deletePostFiles.setInt(i+2,files.get(i));
            }
            deletePostFiles.execute();
        }
    }
    private static void editText(int id,String text,Connection connection) throws SQLException, IOException {
        PreparedStatement updatePostText=connection.prepareStatement(Statement.updatePostText);
        updatePostText.setString(1,text);
        updatePostText.setInt(2,id);
        updatePostText.execute();
    }
    public static void editPostPoll(int user_id,int post_id,Poll poll,boolean delete,Connection connection) throws SQLException {
        if(poll!=null){
            PreparedStatement editPostPoll=connection.prepareStatement(Statement.editPostPoll);
            editPostPoll.setObject(1,Request.savePoll(user_id, poll, connection));
            editPostPoll.setInt(2,post_id);
            editPostPoll.execute();
        }
        else if(delete){
            PreparedStatement editPostPoll=connection.prepareStatement(Statement.editPostPoll);
            editPostPoll.setObject(1,null);
            editPostPoll.setInt(2,post_id);
            editPostPoll.execute();
        }
    }
    public static ResponseEntity<?> editPost(String auth,int user_post_id,String text,List<MultipartFile> append,List<Integer> delete,Poll poll,boolean poll_delete){
        ResponseEntity<?> response;
        if(text!=null || append!=null || delete!=null || poll!=null || poll_delete){
            try {
                Connection connection=Function.connect();
                int user_id=JwtUtil.extractId(auth);
                ResultSet resultSet=checkPost(user_post_id,user_id,connection);
                if(resultSet.next()){
                    List<Integer> listOfFile=saveFiles(append,true,user_id,connection);
                    int post_id=resultSet.getInt(Column.post_id);
                    savePostFiles(post_id,listOfFile,connection);
                    deletePostFiles(post_id,delete,connection);
                    editText(post_id,text,connection);
                    editPostPoll(user_id,post_id,poll,poll_delete,connection);
                    response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                }
                else response = ResponseEntity.ok().body(new Response(ResponseState.NOT_EXIST));
                connection.close();

            } catch (SQLException e){
                response = ResponseEntity.ok().body(new Response(ResponseState.EXCEPTION));
            } catch (IOException e) {
                response = ResponseEntity.ok().body(new Response(ResponseState.FILE_ERROR));
            }
        }
        else response = ResponseEntity.ok().body(new Response(ResponseState.EMPTY));
        return response;
    }
    public static ResponseEntity<?> save(String auth, Object page, String text, List<MultipartFile> files, Poll poll){
        if(text!=null || files!=null || poll!=null){
            if(files != null && files.size() <= 10){
                try {
                    Connection connection = Function.connect();
                    int user_id=JwtUtil.extractId(auth);
                    List<Integer> listOfFile=saveFiles(files,true,user_id,connection);
                    int id=savePost(user_id,text,poll,connection);
                    savePostFiles(id,listOfFile,connection);
                    savaUserPosts(id,user_id,switch (page) {
                        case null -> user_id;
                        default -> page;
                    },connection);
                    connection.close();
                    return ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                } catch (SQLException e){
                    return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
                } catch (IOException e) {
                    return ResponseEntity.badRequest().body(new Response(ResponseState.FILE_ERROR));
                }
            }
            else return ResponseEntity.badRequest().body(new Response(ResponseState.TO_MUCH));
        }
        else return ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY));
    }
    private static void savaUserPosts(int id,int user_id,Object page,Connection connection) throws SQLException {
        PreparedStatement saveUserPosts = connection.prepareStatement(Statement.saveUserPosts);
        saveUserPosts.setInt(1,id);
        saveUserPosts.setInt(2,user_id);
        saveUserPosts.setObject(3,page);
        saveUserPosts.execute();
    }
    private static void savePostFiles(int post_id,List<Integer> posts,Connection connection) throws SQLException {
        if(posts.size()>0){
            PreparedStatement insertIntoPostsFilesMany = connection.prepareStatement(Statement.insertIntoPostsFiles + Function.toValue(posts.size()));
            for (int i=0;i<posts.size();i++){
                insertIntoPostsFilesMany.setInt(i*2+1,post_id);
                insertIntoPostsFilesMany.setInt(i*2+2,posts.get(i));
            }
            insertIntoPostsFilesMany.execute();
        }
    }
    private static int savePost(int user_id,String text,Poll poll,Connection connection) throws SQLException {
        PreparedStatement savePost=connection.prepareStatement(Statement.savePost);
        savePost.setString(2,text);
        savePost.setObject(3,Request.savePoll(user_id, poll,connection));
        int index=Request.tableIndex(TableName.posts, Column.post_id,connection)+1;
        savePost.setInt(1,index);
        savePost.execute();
        return index;
    }
    public static ResponseEntity<?> repost(String auth,int user_post_id,Object page){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            ResponseState status=Request.ItemExistsWithStatus(TableName.user_posts,Column.user_post_id,user_post_id,connection);
            if(status==ResponseState.EXIST){
                PreparedStatement repostOfPost=connection.prepareStatement(Statement.repostOfPost);
                repostOfPost.setInt(1,user_post_id);
                repostOfPost.setObject(2,switch (page) {
                    case null -> user_id;
                    default -> page;
                });
                repostOfPost.setInt(3,user_id);
                repostOfPost.setInt(4,user_post_id);
                repostOfPost.execute();
                response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
            }
            else response=ResponseEntity.badRequest().body(new Response(status));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> getComments(int item,Object last){
        try {
            Connection connection=Function.connect();
            PreparedStatement findComments=connection.prepareStatement(Statement.findPostComments(last));
            findComments.setInt(1,item);
            if(last!=null)findComments.setObject(2,last);
            ResultSet resultSet=findComments.executeQuery();
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
    public static ResponseEntity<?> deleteLikeOnPost(String auth,int post_id){
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            Request.delete(user_id,Statement.deleteLike(TableName.post_likes, Column.user_post_id),post_id,connection);
            deleteLikeOnPreviewFileOfPost(user_id,post_id,connection);
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
    public static void  deleteLikeOnPreviewFileOfPost(int user_id,int user_post_id,Connection connection) throws SQLException {
        PreparedStatement deleteLikeOnPostPreviewFile=connection.prepareStatement(Statement.deleteLikeOnPostPreviewFile);
        deleteLikeOnPostPreviewFile.setInt(1,user_post_id);
        deleteLikeOnPostPreviewFile.setInt(2,user_id);
        deleteLikeOnPostPreviewFile.execute();
    }
    public static void putLikeOnPreviewFileOfPost(int user_id,int user_post_id,Connection connection) throws SQLException {
        PreparedStatement putLikeOnPostPreviewFile=connection.prepareStatement(Statement.putLikeOnPostPreviewFile);
        putLikeOnPostPreviewFile.setInt(1,user_id);
        putLikeOnPostPreviewFile.setInt(2,user_post_id);
        putLikeOnPostPreviewFile.setInt(3,user_id);
        putLikeOnPostPreviewFile.execute();
    }
    public static ResponseEntity<?> putLikeOnPost(String auth,int user_post_id){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            ResultSet resultSet=Request.checkLike(user_id,user_post_id,Statement.checkPostLike,connection);
            if(resultSet.next()){
                if(!resultSet.getBoolean(Column.status)) response = ResponseEntity.badRequest().body(new Response(ResponseState.NO_ACTIVE));
                else if(resultSet.getBoolean(Column.my_like)) response = ResponseEntity.badRequest().body(new Response(ResponseState.ALREADY_EXIST));
                else {
                    Request.putLike(TableName.post_likes,Column.user_post_id,user_id,user_post_id,connection);
                    putLikeOnPreviewFileOfPost(user_id, user_post_id, connection);
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
    public static ResponseEntity<?> savePostComment(String auth,int user_post_id,String text){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            ResponseState status=Request.ItemExistsWithStatus(TableName.user_posts,Column.user_post_id,user_post_id,connection);
            if(status==ResponseState.EXIST){
                Request.saveComment(auth,TableName.post_comments,Column.user_post_id,user_post_id,text,connection);
                response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
            }
            else response = ResponseEntity.badRequest().body(new Response(status));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static ResponseEntity<?> getPostsBy(Object user_id,String tableName,String by,String column,Object item,Object last){
        try {
            return ResponseEntity.ok().body(Request.getPostsBy(user_id,tableName,by,column,item,last));
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> getPostById(String auth,int user_post_id){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            Object user_id=JwtUtil.extractIdOrNull(auth);
            ResponseState status=Request.ItemExistsWithStatus(TableName.user_posts,Column.user_post_id,user_post_id,connection);
            if(status==ResponseState.EXIST){
                PreparedStatement findPostById=connection.prepareStatement(Statement.getPostsBy(TableName.user_posts,null)+
                        Statement.findBy(Function.concat(TableName.user_posts, Column.user_post_id))
                );
                findPostById.setObject(1,user_id);
                findPostById.setInt(2,user_post_id);
                ResultSet resultSet=findPostById.executeQuery();
                resultSet.next();
                response=ResponseEntity.ok().body(new Post(user_id,resultSet,connection));
            }
            else response=ResponseEntity.badRequest().body(new Response(status));
            connection.close();
        } catch (SQLException e){
            response=ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
}
