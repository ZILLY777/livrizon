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

public class SubscribeRequest {
    public static ResponseEntity<?> getMyFollowing(String auth,int sort,String next){
        try {
            Connection connection=Function.connect();
            String sorting = switch (sort) {
                case 1 -> Statement.sortByLike;
                case 2 -> Statement.orderByDesc(Column.subscribe_id);
                default -> Statement.orderBy(Column.subscribe_id);
            };
            PreparedStatement selectSub=connection.prepareStatement(Statement.selectSub(Column.sub_id,Column.user_id,false)+
                    sorting+
                    Statement.rangeLimit(next,15)
            );
            selectSub.setInt(1,JwtUtil.extractId(auth));
            if(next!=null) selectSub.setInt(2, Integer.parseInt(next));
            ResultSet resultSet=selectSub.executeQuery();
            List<UserProfile> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new UserProfile(resultSet, TableName.users,TableName.user_avatar));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> getSubscribe(String auth,int user_id,String column1,String column2,String last){
        try {
            Connection connection=Function.connect();
            PreparedStatement selectSub=connection.prepareStatement(Statement.selectSub(column1,column2,true)+
                    Statement.andFindByLess(Column.subscribe_id,last)+
                    Statement.orderByDesc(Column.subscribe_id)
            );
            selectSub.setObject(1,JwtUtil.extractIdOrNull(auth));
            selectSub.setInt(2,user_id);
            if(last!=null) selectSub.setObject(3,last);
            ResultSet resultSet=selectSub.executeQuery();
            List<Subscribe> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new Subscribe(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> unsubscribe(String auth,int sub_id){
        ResponseEntity<?> response;
        try {
            Connection connection = Function.connect();
            int user_id=JwtUtil.extractId(auth);
            PreparedStatement unsubscribe=connection.prepareStatement(Statement.unsubscribe);
            unsubscribe.setInt(1, user_id);
            unsubscribe.setInt(2, sub_id);
            unsubscribe.execute();
            connection.close();
            response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
        } catch (SQLException e) {
            response=ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    private static ResponseState checkSubscribe(int user_id,int sub_id,Connection connection) throws SQLException {
        PreparedStatement checkSubscribe=connection.prepareStatement(Statement.checkSubscribe);
        checkSubscribe.setInt(1,user_id);
        checkSubscribe.setInt(2,sub_id);
        ResultSet resultSet=checkSubscribe.executeQuery();
        if(resultSet.next()){
            if(!resultSet.getBoolean(Column.status)) return ResponseState.NO_ACTIVE;
            else if (resultSet.getBoolean(Column.my_subscribe)) return ResponseState.ALREADY_EXIST;
            else return ResponseState.EXIST;
        }
        else return ResponseState.NOT_EXIST;
    }
    public static ResponseEntity<?> subscribe(String auth,int user_id) {
        ResponseEntity<?> response;
        int owner_id=JwtUtil.extractId(auth);
        if(owner_id!=user_id) {
            try {
                Connection connection = Function.connect();
                ResponseState status = checkSubscribe(owner_id, user_id, connection);
                if (status == ResponseState.EXIST) {
                    Request.saveUserConnects(owner_id, user_id, connection);
                    subscribe(owner_id, user_id, connection);
                    response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                }
                else response = ResponseEntity.badRequest().body(new Response(status));
                connection.close();
            } catch (SQLException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }
        }
        else response = ResponseEntity.badRequest().body(new Response(ResponseState.YOURSELF));
        return response;
    }

    public static ResponseEntity<?> getMutualConnection(String auth, int user_id, Object last){
        ResponseEntity<?> response;
        int owner_id=JwtUtil.extractId(auth);
        if (owner_id!=user_id){
            try {
                Connection connection = Function.connect();
                PreparedStatement getSubOfSub=connection.prepareStatement(Statement.getMutualConnection(last));
                getSubOfSub.setInt(1,user_id);
                getSubOfSub.setInt(2,owner_id);
                if(last!=null) getSubOfSub.setObject(3,last);
                ResultSet resultSet=getSubOfSub.executeQuery();
                List<MySubscribe> list=new ArrayList<>();
                while (resultSet.next()){
                    list.add(new MySubscribe(resultSet));
                }
                response = ResponseEntity.ok().body(list);
                connection.close();
            } catch (SQLException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }

        }else response = ResponseEntity.badRequest().body(new Response(ResponseState.YOURSELF));
        return response;
    }

    public static ResponseEntity<?> getHandshakes(String auth, int handshakes, Object last){
        ResponseEntity<?> response;
        int owner_id=JwtUtil.extractId(auth);
            try {
                Connection connection = Function.connect();

                PreparedStatement getHandShake=connection.prepareStatement(switch(handshakes){
                    case 1 -> Statement.getHandshakeFirstGen(last);
                    case 2 -> Statement.getHandshakeSecondGen(last);
                    default -> Statement.getHandshakeThirdGen(last);
                });
                getHandShake.setInt(1,owner_id);
                if(last!=null) getHandShake.setObject(2,last);
                ResultSet resultSet=getHandShake.executeQuery();
                List<Subscribe> list=new ArrayList<>();
                while (resultSet.next()){
                    list.add(new Subscribe(resultSet));
                }
                connection.close();
                response = ResponseEntity.ok().body(list);
            } catch (SQLException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }
            return response;

    }

    private static void subscribe(int user_id, int sub_id, Connection connection) throws SQLException {
        PreparedStatement subscribe=connection.prepareStatement(Statement.subscribe);
        subscribe.setInt(1, user_id);
        subscribe.setInt(2, sub_id);
        subscribe.execute();
    }
}
