package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.function.Function;
import com.server.founder.request.PollRequest;
import com.server.founder.request.PostRequest;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Post {
    int post_id;
    From from;
    From forward_from;
    PostBody body;
    Statistic statistic;
    Poll poll;
    CommentOwner comment;
    List<AvatarOwner> subscribes;
    public Post() {
    }
    public Post(Object user_id,ResultSet resultSet,Connection connection) throws SQLException {
        this.post_id = resultSet.getInt(Function.concat(TableName.user_posts, Column.user_post_id));
        this.from = new From(resultSet,TableName.users, TableName.user_posts,TableName.user_avatar);
        if(resultSet.getObject(Function.concat(TableName.forward_from, Column.user_id))!=null)
            this.forward_from = new From(resultSet, TableName.forward_from,TableName.repost,TableName.forward_avatar);
        this.body = new PostBody(resultSet,connection);
        this.statistic = new Statistic(resultSet);
        if(resultSet.getObject(Column.poll_id)!=null)
            this.poll = new Poll(user_id,resultSet,connection);
        if(resultSet.getObject(Column.comment_id)!=null)
            this.comment = new CommentOwner(resultSet);
        if(user_id!=null) this.subscribes = PostRequest.selectMySubOfPost(resultSet.getInt(Function.concat(TableName.user_posts, Column.user_post_id)),(Integer) user_id,connection);
    }
}
