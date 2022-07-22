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

public class ChatRequest {
    public static ResponseEntity<?> getUserChats(String auth,String next){
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            PreparedStatement getUserChats=connection.prepareStatement(Statement.getUserChats(next));
            getUserChats.setInt(1,user_id);
            if(next!=null) getUserChats.setInt(2,Integer.parseInt(next));
            ResultSet resultSet=getUserChats.executeQuery();
            List<UserChat> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new UserChat(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> getChat(String auth,int user_id,Object last){
        try {
            Connection connection=Function.connect();
            int from = JwtUtil.extractId(auth);
            PreparedStatement selectPrivetMessages=connection.prepareStatement(Statement.selectPrivetMessages(last));
            selectPrivetMessages.setInt(1,Math.min(from,user_id));
            selectPrivetMessages.setInt(2,Math.max(from,user_id));
            selectPrivetMessages.setInt(3,from);
            if (last!=null) selectPrivetMessages.setObject(4,last);
            ResultSet resultSet=selectPrivetMessages.executeQuery();
            List<PrivetMessage> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new PrivetMessage(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> sendMessage(String auth,int user_id,String text){
        ResponseEntity<?> response;
        int from = JwtUtil.extractId(auth);
        if(from!=user_id){
            try {
                Connection connection= Function.connect();
                int user_id_1=Math.min(from,user_id);
                int user_id_2=Math.max(from,user_id);
                ResultSet resultSet=checkPrivetChat(user_id_1,user_id_2,user_id,connection);
                if(resultSet.next()){
                    if(!resultSet.getBoolean(Column.status)) response = ResponseEntity.badRequest().body(new Response(ResponseState.NO_ACTIVE));
                    else {
                        int chat_id;
                        if (resultSet.getObject(Column.chat_id) == null) chat_id=createChat(user_id_1,user_id_2,connection);
                        else chat_id=resultSet.getInt(Column.chat_id);
                        saveMessage(chat_id, from, text, connection);
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
        else return ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
    }
    private static void saveMessage(int chat_id,int from,String text,Connection connection) throws SQLException {
        PreparedStatement insertIntoMessages=connection.prepareStatement(Statement.insertIntoMessages);
        insertIntoMessages.setObject(1,chat_id);
        insertIntoMessages.setInt(2,from);
        insertIntoMessages.setString(3,text);
        insertIntoMessages.execute();
    }
    private static ResultSet checkPrivetChat(int user_id_1,int user_id_2,int user_id,Connection connection) throws SQLException {
        PreparedStatement checkPrivetChat=connection.prepareStatement(Statement.checkPrivetChat);
        checkPrivetChat.setInt(1,user_id_1);
        checkPrivetChat.setInt(2,user_id_2);
        checkPrivetChat.setInt(3,user_id);
        return checkPrivetChat.executeQuery();
    }
    private static int createChat(int user_id_1,int user_id_2,Connection connection) throws SQLException {
        PreparedStatement insertIntoChats=connection.prepareStatement(Statement.insertIntoChats);
        insertIntoChats.setString(2, String.valueOf(ChatType.PRIVET));
        int chat_id=Request.tableIndex(TableName.chats,Column.chat_id,connection)+1;
        insertIntoChats.setInt(1,chat_id);
        insertIntoChats.execute();
        PreparedStatement insertIntoPrivetChats=connection.prepareStatement(Statement.insertIntoPrivetChats);
        insertIntoPrivetChats.setInt(1,chat_id);
        insertIntoPrivetChats.setInt(2,user_id_1);
        insertIntoPrivetChats.setInt(3,user_id_2);
        insertIntoPrivetChats.execute();
        PreparedStatement insertIntoUserChats=connection.prepareStatement(Statement.insertIntoUserChats+Function.toValue(2));
        insertIntoUserChats.setInt(1,chat_id);
        insertIntoUserChats.setInt(2,user_id_1);
        insertIntoUserChats.setInt(3,chat_id);
        insertIntoUserChats.setInt(4,user_id_2);
        insertIntoUserChats.execute();
        return chat_id;
    }
}
