package com.server.founder.request;

import com.server.founder.model.UserProfile;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Like {
    int like_id;
    UserProfile user;

    public Like() {
    }

    public Like(ResultSet resultSet) throws SQLException {
        this.like_id = resultSet.getInt(Column.like_id);
        this.user = new UserProfile(resultSet, TableName.users,TableName.user_avatar);
    }
}
