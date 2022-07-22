package com.server.founder.request;

import com.server.founder.function.Function;
import com.server.founder.model.*;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Column;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.server.founder.function.Function.connect;

public class Request {
    public static Poll getPoll(Object user_id,int poll_id,Connection connection) throws SQLException {
        PreparedStatement selectVote=connection.prepareStatement(Statement.selectPull);
        selectVote.setObject(1,poll_id);
        System.out.println(selectVote);
        ResultSet resultSet=selectVote.executeQuery();
        if(resultSet.next()) return new Poll(user_id,resultSet,connection);
        else return null;
    }
    public static void editPoll(Object poll_id, int user_id, List<String> append, List<Integer> delete, List<EditLine> update, String theme, Connection connection) throws SQLException {
        PreparedStatement checkPoll=connection.prepareStatement(Statement.checkPoll);
        checkPoll.setObject(1,poll_id);
        checkPoll.setInt(2,user_id);
        ResultSet resultSet=checkPoll.executeQuery();
        if(resultSet.next()){
            if(delete!=null && resultSet.getInt(Column.number)>delete.size()) PollRequest.deletePostPollLines(poll_id,delete,connection);
            PollRequest.updatePostPollLines(poll_id,update,connection);
            PollRequest.appendPollLines(poll_id,append,connection);
            PollRequest.updatePollTheme(poll_id,theme,connection);
        }
    }
    public static Object savePoll(int user_id, Poll poll, Connection connection) throws SQLException {
        if(poll!=null && poll.getLines()!=null && poll.getLines().size()>0){
            PreparedStatement savePoll=connection.prepareStatement(Statement.savePoll);
            savePoll.setString(2,String.valueOf(poll.getType()));
            savePoll.setString(3,String.valueOf(poll.getView()));
            savePoll.setString(4, poll.getTheme());
            savePoll.setInt(5,user_id);
            int poll_id=Request.tableIndex(TableName.polls,Column.poll_id,connection)+1;
            savePoll.setInt(1,poll_id);
            savePoll.execute();
            PreparedStatement insertIntoVoteLines=connection.prepareStatement(Statement.insertIntoPollLines+Function.toValue(poll.getLines().size()));
            for (int i = 0; i< poll.getLines().size(); i++){
                insertIntoVoteLines.setInt(i*2+1,poll_id);
                insertIntoVoteLines.setString(i*2+2, poll.getLines().get(i).getText());
            }
            insertIntoVoteLines.execute();
            return poll_id;
        }
        return null;
    }
    public static List<File> getUserAvatars(String auth, int id, Object last, Connection connection) throws SQLException {
        PreparedStatement findUserAvatars=connection.prepareStatement(Statement.findUserAvatars(last));
        Object user_id=JwtUtil.extractIdOrNull(auth);
        findUserAvatars.setObject(1,user_id);
        findUserAvatars.setInt(2,id);
        if(last!=null) findUserAvatars.setObject(3,last);
        ResultSet resultSet=findUserAvatars.executeQuery();
        List<File> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(new File(resultSet,Column.avatar_id));
        }
        return list;
    }
    public static int getItemNumber(String tableName,String column,int id,Connection connection) throws SQLException {
        PreparedStatement selectItemNumber=connection.prepareStatement(Statement.selectItemNumber(tableName,column));
        selectItemNumber.setInt(1,id);
        ResultSet resultSet=selectItemNumber.executeQuery();
        resultSet.next();
        return resultSet.getInt(Column.number);
    }
    public static void delete(int user_id,String statement,Object id,Connection connection) throws SQLException {
        PreparedStatement delete=connection.prepareStatement(statement);
        delete.setObject(1,id);
        delete.setInt(2, user_id);
        delete.execute();
    }
    public static void putLike(String tableName,String column,int user_id,int item,Connection connection) throws SQLException {
        PreparedStatement putLike=connection.prepareStatement(Statement.putLike(tableName,column));
        putLike.setInt(1,item);
        putLike.setInt(2,user_id);
        putLike.execute();
    }
    public static ResultSet checkLike(int user_id,Object item,String by,Connection connection) throws SQLException {
        PreparedStatement checkPostLike=connection.prepareStatement(by);
        checkPostLike.setInt(1,user_id);
        checkPostLike.setObject(2,item);
        return checkPostLike.executeQuery();
    }
    public static List<Post> getPostsBy(Object user_id, String tableName, String by, String column, Object item, Object last) throws SQLException {
        Connection connection= Function.connect();
        PreparedStatement getPostsBy=connection.prepareStatement(Statement.getPostsBy(tableName,by) +
                Statement.findBy(Function.concat(tableName,column))+
                Statement.andFindByLess(Function.concat(TableName.user_posts,Column.user_post_id),last)+
                Statement.status(TableName.user_posts) +
                Statement.orderByDesc(Function.concat(TableName.user_posts,Column.user_post_id)) +
                Statement.limit(15)
        );
        getPostsBy.setObject(1,user_id);
        getPostsBy.setObject(2,item);
        if(last!=null) getPostsBy.setObject(3,last);
        ResultSet resultSet=getPostsBy.executeQuery();
        List<Post> posts = new ArrayList<>();
        while (resultSet.next()){
            posts.add(new Post(user_id,resultSet,connection));
        }
        connection.close();
        return posts;
    }
    public static void saveComment(String auth,String tableName,String column,int item,String text,Connection connection) throws SQLException {
        PreparedStatement saveComment=connection.prepareStatement(Statement.saveComment(tableName,column));
        saveComment.setInt(1,item);
        saveComment.setInt(2,JwtUtil.extractId(auth));
        saveComment.setString(3,text);
        saveComment.execute();
    }
    public static List<File> getPostPostFilesById(int id, Connection connection) throws SQLException {
        PreparedStatement selectPostFiles=connection.prepareStatement(Statement.selectPostFiles);
        selectPostFiles.setInt(1,id);
        ResultSet resultSet=selectPostFiles.executeQuery();
        List<File> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(new File(resultSet,Column.post_file_id));
        }
        return list;
    }
    public static Object getItemId(String tableName,String column,String by,Object item,Connection connection) throws SQLException {
        PreparedStatement findItemId=connection.prepareStatement(Statement.findItem(tableName,column,by)+Statement.orderByDesc(by));
        findItemId.setObject(1,item);
        ResultSet resultSet=findItemId.executeQuery();
        if(resultSet.next()) return resultSet.getInt(1);
        else return null;
    }
    public static void createTables() throws SQLException {
        Connection connection=connect();
        PreparedStatement createTableUsers=connection.prepareStatement(Statement.createTableUsers);
        PreparedStatement createTableFiles=connection.prepareStatement(Statement.createTableFiles);
        PreparedStatement createTableLogin=connection.prepareStatement(Statement.createTableLogin);
        PreparedStatement createTablePolls=connection.prepareStatement(Statement.createTablePolls);
        PreparedStatement createTablePollLines=connection.prepareStatement(Statement.createTablePollLines);
        PreparedStatement createTablePosts=connection.prepareStatement(Statement.createTablePosts);
        PreparedStatement createTableUserPosts=connection.prepareStatement(Statement.createTableUserPosts);
        PreparedStatement createTablePostsFiles = connection.prepareStatement(Statement.createTablePostsFiles);
        PreparedStatement createTableSubscribe=connection.prepareStatement(Statement.createTableSubscribe);
        PreparedStatement createTablePostLikes=connection.prepareStatement(Statement.createTablePostLikes);
        PreparedStatement createTableFileLikes=connection.prepareStatement(Statement.createTableFileLikes);
        PreparedStatement createTablePostComments=connection.prepareStatement(Statement.createTablePostComments);
        PreparedStatement createTableFileComment=connection.prepareStatement(Statement.createTableFileComment);
        PreparedStatement createTableUserAvatars=connection.prepareStatement(Statement.createTableUserAvatars);
        PreparedStatement createTableLineUsers=connection.prepareStatement(Statement.createTableLineUsers);
        PreparedStatement createTableChats=connection.prepareStatement(Statement.createTableChats);
        PreparedStatement createTablePrivetChat=connection.prepareStatement(Statement.createTablePrivetChat);
        PreparedStatement createTablePublicChat=connection.prepareStatement(Statement.createTablePublicChat);
        PreparedStatement createTableMessages=connection.prepareStatement(Statement.createTableMessages);
        PreparedStatement createTableDeleteMessages=connection.prepareStatement(Statement.createTableDeleteMessages);
        PreparedStatement createTableUserChats=connection.prepareStatement(Statement.createTableUserChats);
        PreparedStatement createTableMessageFiles=connection.prepareStatement(Statement.createTableMessageFiles);
        PreparedStatement createTableMembers=connection.prepareStatement(Statement.createTableMembers);
        PreparedStatement createTableUserConnection=connection.prepareStatement(Statement.createTableUserConnection);
        PreparedStatement createTablePublicAvatars=connection.prepareStatement(Statement.createTablePublicAvatars);
        createTableUsers.execute();
        createTableFiles.execute();
        createTableLogin.execute();
        createTablePolls.execute();
        createTablePollLines.execute();
        createTablePosts.execute();
        createTableUserPosts.execute();
        createTablePostsFiles.execute();
        createTablePostLikes.execute();
        createTableFileLikes.execute();
        createTableSubscribe.execute();
        createTablePostComments.execute();
        createTableFileComment.execute();
        createTableUserAvatars.execute();
        createTableLineUsers.execute();
        createTableChats.execute();
        createTablePrivetChat.execute();
        createTablePublicChat.execute();
        createTableMessages.execute();
        createTableDeleteMessages.execute();
        createTableUserChats.execute();
        createTableMessageFiles.execute();
        createTableMembers.execute();
        createTableUserConnection.execute();
        createTablePublicAvatars.execute();
        connection.close();
    }
    public static void deleteAllVotes(int poll_id ,int user_id,Connection connection) throws SQLException {
        PreparedStatement deleteAllVotes=connection.prepareStatement(Statement.deleteAllVotes);
        deleteAllVotes.setInt(1,poll_id);
        deleteAllVotes.setInt(2,user_id);
        deleteAllVotes.execute();
    }
    public static void saveUserConnect(int user_id_1,int user_id_2,Connection connection) throws SQLException {
        PreparedStatement saveUserConnection=connection.prepareStatement(Statement.saveUserConnection);
        saveUserConnection.setInt(1,Math.min(user_id_1,user_id_2));
        saveUserConnection.setInt(2,Math.max(user_id_1,user_id_2));
        saveUserConnection.execute();
    }
    public static ResponseState ItemExistsWithStatus(String tableName,String column,Object item,Connection connection) throws SQLException {
        PreparedStatement findItemBy;
        findItemBy=connection.prepareStatement(Statement.findItemBy(tableName,Column.status,column));
        findItemBy.setObject(1,item);
        ResultSet resultSet=findItemBy.executeQuery();
        if(resultSet.next()){
            if(resultSet.getBoolean(Column.status)) return ResponseState.EXIST;
            else return ResponseState.NO_ACTIVE;
        }
        else return ResponseState.NOT_EXIST;
    }
    public static boolean ItemExist(String tableName,String column,Object item,Connection connection) throws SQLException {
        PreparedStatement findItemBy;
        findItemBy=connection.prepareStatement(Statement.findItemBy(tableName,column,column));
        findItemBy.setObject(1,item);
        return findItemBy.executeQuery().next();
    }
    public static int tableIndex(String tableName,String column,Connection connection) throws SQLException {
        PreparedStatement lastTableIndex=connection.prepareStatement(Statement.tableIndex(tableName,column));
        ResultSet resultSet = lastTableIndex.executeQuery();
        int index;
        if(resultSet.next()) index=resultSet.getInt(column);
        else index=0;
        return index;
    }
}
