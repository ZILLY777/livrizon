package com.server.founder.model;

import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class CommentProfile {
    int comment_id;
    long date;
    String text;
    UserProfile userProfile;
    public CommentProfile() {
    }
    public CommentProfile(ResultSet resultSet) throws SQLException {
        this.comment_id = resultSet.getInt(Column.comment_id);
        this.date = resultSet.getTimestamp(Column.date).getTime()/1000;
        this.text = resultSet.getString(Column.text);
        this.userProfile = new UserProfile(resultSet,TableName.users,TableName.user_avatar);
    }
}
