package com.server.founder.request;

import com.server.founder.function.Function;
import com.server.founder.model.Recommendation;
import com.server.founder.model.Response;
import com.server.founder.model.ResponseState;
import com.server.founder.model.UserProfile;
import com.server.founder.security.JwtUtil;
import com.server.founder.sql.Statement;
import com.server.founder.sql.TableName;
import org.springframework.http.ResponseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecommendationRequest {
    public static ResponseEntity<?> getRecommendationUsers(String auth, String next){
        try {
            Connection connection= Function.connect();
            PreparedStatement getUserRecommendation=connection.prepareStatement(Statement.getRecommendationUsers(next));
            getUserRecommendation.setInt(1, JwtUtil.extractId(auth));
            if(next!=null) getUserRecommendation.setInt(2,Integer.parseInt(next));
            ResultSet resultSet=getUserRecommendation.executeQuery();
            List<Recommendation> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new Recommendation(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> getPossibleUsers(String auth,String next){
        try{
            Connection connection=Function.connect();
            PreparedStatement getPossibleUsers=connection.prepareStatement(Statement.getPossibleUsers(next));
            getPossibleUsers.setInt(1,JwtUtil.extractId(auth));
            if(next!=null) getPossibleUsers.setInt(2,Integer.parseInt(next));
            ResultSet resultSet=getPossibleUsers.executeQuery();
            List<Recommendation> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new Recommendation(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static ResponseEntity<?> getPopularUsers(String auth,String next){
        try{
            Connection connection=Function.connect();
            PreparedStatement getPopularUsers=connection.prepareStatement(Statement.getPopularUsers(next));
            int user_id=JwtUtil.extractId(auth);
            getPopularUsers.setInt(1,user_id);
            getPopularUsers.setInt(2,user_id);
            getPopularUsers.setInt(3,user_id);
            if(next!=null) getPopularUsers.setInt(4,Integer.parseInt(next));
            ResultSet resultSet=getPopularUsers.executeQuery();
            List<Recommendation> list=new ArrayList<>();
            while (resultSet.next()){
                list.add(new Recommendation(resultSet));
            }
            connection.close();
            return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
}
