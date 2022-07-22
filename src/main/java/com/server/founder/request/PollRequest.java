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
import java.util.Objects;
import java.util.stream.Collectors;

public class PollRequest {
    public static ResponseEntity<?> getUserLine(String auth,int line_id,boolean subscribe,Object last){
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            PreparedStatement getUserLine=connection.prepareStatement(Statement.getUserLine(last,subscribe));
            getUserLine.setInt(1,line_id);
            getUserLine.setInt(2,user_id);
            if(last!=null) getUserLine.setObject(3,last);
            List<Vote> list=new ArrayList<>();
            ResultSet resultSet=getUserLine.executeQuery();
            while (resultSet.next()){
                list.add(new Vote(resultSet));
            }
            connection.close();
           return ResponseEntity.ok().body(list);
        } catch (SQLException e){
            return ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
    }
    public static List<AvatarOwner> selectMySubOfPull(int poll_id, int user_id,Connection connection) throws SQLException {
        PreparedStatement selectMySubOfPull=connection.prepareStatement(Statement.selectMySubOfPull);
        selectMySubOfPull.setInt(1,poll_id);
        selectMySubOfPull.setInt(2,user_id);
        ResultSet resultSet=selectMySubOfPull.executeQuery();
        List<AvatarOwner> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(new AvatarOwner(Column.sub_id,resultSet));
        }
        if(list.size()>0) return list;
        else return null;
    }
    public static PollView getPollLines(Object user_id,int poll_id,Connection connection) throws SQLException {
        PreparedStatement selectPollLines=connection.prepareStatement(Statement.selectPollLines);
        selectPollLines.setObject(1,user_id);
        selectPollLines.setInt(2,poll_id);
        List<PollLine> list=new ArrayList<>();
        List<Integer> listOfNumber=new ArrayList<>();
        List<Boolean> listOfMy_vote=new ArrayList<>();
        ResultSet resultSet=selectPollLines.executeQuery();
        while (resultSet.next()){
            list.add(new PollLine(resultSet.getInt(Column.line_id),resultSet.getString(Column.text)));
            listOfNumber.add(resultSet.getInt(Column.number));
            listOfMy_vote.add(resultSet.getBoolean(Column.my_vote));
        }
        if(listOfMy_vote.contains(true)){
            for (int i=0;i<list.size();i++){
                list.get(i).setNumber(listOfNumber.get(i));
                list.get(i).setMy_vote(listOfMy_vote.get(i));
            }
        }
        return new PollView(Function.listSum(listOfNumber),listOfMy_vote.contains(true),list);
    }
    public static ResponseEntity<?> getPoll(String auth,int poll_id){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            Object user_id=JwtUtil.extractIdOrNull(auth);
            Poll poll=Request.getPoll(user_id,poll_id,connection);
            if(poll!=null) response = ResponseEntity.ok().body(poll);
            else response = ResponseEntity.badRequest().body(new Response(ResponseState.NOT_EXIST));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static void appendPollLines(Object poll_id, List<String> appendLines, Connection connection) throws SQLException {
        if(appendLines!=null){
            PreparedStatement insertIntoVoteLines=connection.prepareStatement(Statement.insertIntoPollLines+Function.toValue(appendLines.size()));
            for (int i = 0; i< appendLines.size(); i++){
                insertIntoVoteLines.setObject(i*2+1,poll_id);
                insertIntoVoteLines.setString(i*2+2, appendLines.get(i));
            }
            insertIntoVoteLines.execute();
        }
    }
    public static void deletePostPollLines(Object poll_id, List<Integer> deleteLines, Connection connection) throws SQLException {
        if(deleteLines!=null){
            PreparedStatement deletePostPollLines=connection.prepareStatement(Statement.deletePostPollLines);
            deletePostPollLines.setObject(2,poll_id);
            for (Integer deleteLine : deleteLines) {
                deletePostPollLines.setInt(1, deleteLine);
                deletePostPollLines.execute();
            }
        }
    }
    public static void deletePoll(int post_id,Connection connection) throws SQLException {
        PreparedStatement deletePoll=connection.prepareStatement(Statement.deletePoll);
        deletePoll.setInt(1,post_id);
        deletePoll.execute();
    }
    public static void updatePostPollLines(Object poll_id, List<EditLine> editLines, Connection connection) throws SQLException {
        if(editLines!=null){
            PreparedStatement updatePostPollLines=connection.prepareStatement(Statement.updatePostPollLines);
            updatePostPollLines.setObject(3,poll_id);
            for (EditLine editLine : editLines) {
                updatePostPollLines.setString(1, editLine.getText());
                updatePostPollLines.setInt(2, editLine.getLine_id());
                updatePostPollLines.execute();
            }
        }
    }
    public static void updatePollTheme(Object poll_id, String text, Connection connection) throws SQLException {
        if(text!=null){
            PreparedStatement updatePollTheme=connection.prepareStatement(Statement.updatePollTheme);
            updatePollTheme.setString(1,text);
            updatePollTheme.setObject(2,poll_id);
            updatePollTheme.execute();
        }
    }
    public static ResponseEntity<?> editPoll(String auth,int poll_id, EditPoll editPoll){
        ResponseEntity<?> response;
        if(editPoll.getAppend()!=null || editPoll.getDelete()!=null || editPoll.getUpdate()!=null || editPoll.getTheme()!=null){
            try {
                Connection connection=Function.connect();
                Request.editPoll(poll_id,JwtUtil.extractId(auth),editPoll.getAppend(),editPoll.getDelete(),editPoll.getUpdate(),editPoll.getTheme(),connection);
                response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                connection.close();
            } catch (SQLException e){
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }
        }
        else response = ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY));
        return response;
    }
    public static ResponseEntity<?> savePoll(String auth, Poll poll){
        ResponseEntity<?> response;
        if(Objects.requireNonNull(poll).getLines()!=null && poll.getLines().size()>0){
            try {
                Connection connection = Function.connect();
                Request.savePoll(JwtUtil.extractId(auth), poll, connection);
                response = ResponseEntity.ok().body(new Response(ResponseState.SUCCESS));
                connection.close();
            } catch (SQLException e) {
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }
            return response;
        }
        else return ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY));
    }
    public static ResponseEntity<?> putSingleVote(String auth,int poll_id,int line_id){
        ResponseEntity<?> response;
        try {
            Connection connection=Function.connect();
            int user_id=JwtUtil.extractId(auth);
            ResponseState exist=checkSingleVote(poll_id,line_id,connection);
            if(exist==ResponseState.EXIST){
                Request.deleteAllVotes(poll_id,user_id,connection);
                PreparedStatement insertIntoLineUsers=connection.prepareStatement(Statement.insertIntoLineUsers+Function.toValue(1));
                insertIntoLineUsers.setInt(1,line_id);
                insertIntoLineUsers.setInt(2,user_id);
                insertIntoLineUsers.execute();
                response = ResponseEntity.ok().body(PollRequest.getPollLines(user_id,poll_id,connection));
            }
            else response = ResponseEntity.badRequest().body(new Response(exist));
            connection.close();
        } catch (SQLException e){
            response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
        }
        return response;
    }
    public static List<Integer> selectPollLines(int poll_id,Connection connection) throws SQLException {
        PreparedStatement selectPollLines=connection.prepareStatement(Statement.selectMultiplePollLines);
        selectPollLines.setInt(1,poll_id);
        List<Integer> list=new ArrayList<>();
        ResultSet resultSet=selectPollLines.executeQuery();
        while (resultSet.next()){
            list.add(resultSet.getInt(Column.line_id));
        }
        return list;
    }
    public static ResponseEntity<?> putMultipleVote(String auth,int poll_id,List<Integer> append,List<Integer> delete){
        ResponseEntity<?> response;
        if(append!=null || delete!=null){
            try {
                Connection connection=Function.connect();
                int user_id=JwtUtil.extractId(auth);
                List<Integer> listOfLines=selectPollLines(poll_id,connection);
                if(listOfLines.size()>0){
                    boolean action=false;
                    if(append!=null){
                        append=append.stream().distinct().collect(Collectors.toList());
                        List<Integer> list=Function.isContain(listOfLines,append);
                        if(list.size()>0){
                            action=true;
                            PreparedStatement insertIntoLineUsers=connection.prepareStatement(Statement.insertIntoLineUsers+Function.toValue(list.size()));
                            for (int i=0;i<list.size();i++){
                                insertIntoLineUsers.setInt(i*2+1,list.get(i));
                                insertIntoLineUsers.setInt(i*2+2,user_id);
                            }
                            insertIntoLineUsers.execute();
                        }
                    }
                    if(delete!=null){
                        delete=delete.stream().distinct().collect(Collectors.toList());
                        List<Integer> list=Function.isContain(listOfLines,delete);
                        if(list.size()>0){
                            action=true;
                            PreparedStatement deleteVotes=connection.prepareStatement(Statement.deleteVotes+Function.toValues(list.size()));
                            deleteVotes.setInt(1,user_id);
                            for (int i=0;i<list.size();i++){
                                deleteVotes.setInt(i+2,list.get(i));
                            }
                            deleteVotes.execute();
                        }
                    }
                    if(action) response = ResponseEntity.ok().body(PollRequest.getPollLines(user_id,poll_id,connection));
                    else response = ResponseEntity.badRequest().body(new Response(ResponseState.NOT_CONTAIN));
                }
                else response = ResponseEntity.badRequest().body(new Response(ResponseState.TYPE_ERROR));
                connection.close();
            } catch (SQLException e){
                response = ResponseEntity.badRequest().body(new Response(ResponseState.EXCEPTION));
            }
        }
        else response = ResponseEntity.badRequest().body(new Response(ResponseState.EMPTY));
        return response;

    }
    private static PollType getVoteType(int poll_id,Connection connection) throws SQLException {
        PreparedStatement findItemBy=connection.prepareStatement(Statement.findItemBy(TableName.polls,Column.type,Column.poll_id));
        findItemBy.setInt(1,poll_id);
        ResultSet resultSet=findItemBy.executeQuery();
        if(resultSet.next()) return PollType.valueOf(resultSet.getString(Column.type));
        else return null;
    }

    private static ResponseState checkSingleVote(int poll_id,int line_id,Connection connection) throws SQLException {
        PreparedStatement checkSingleVote=connection.prepareStatement(Statement.checkSingleVote);
        checkSingleVote.setInt(1,poll_id);
        checkSingleVote.setInt(2,line_id);
        ResultSet resultSet=checkSingleVote.executeQuery();
        if(resultSet.next()) {
            if(PollType.valueOf(resultSet.getString(Column.type)) == PollType.SINGLE) return ResponseState.EXIST;
            else return ResponseState.TYPE_ERROR;
        }
        else return ResponseState.NOT_EXIST;
    }
}
