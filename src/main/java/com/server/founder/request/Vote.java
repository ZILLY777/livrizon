package com.server.founder.request;

import com.server.founder.model.UserProfile;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Vote {
    int vote_id;
    UserProfile userProfile;

    public Vote() {
    }

    public Vote(ResultSet resultSet) throws SQLException {
        this.vote_id = resultSet.getInt(Column.vote_id);
        this.userProfile = new UserProfile(resultSet, TableName.users,TableName.user_avatar);
    }
}
