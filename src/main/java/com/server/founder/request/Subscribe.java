package com.server.founder.request;

import com.server.founder.model.UserProfile;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Subscribe {
    int subscribe_id;
    UserProfile userProfile;

    public Subscribe() {
    }

    public Subscribe(ResultSet resultSet) throws SQLException {
        this.subscribe_id = resultSet.getInt(Column.subscribe_id);
        this.userProfile = new UserProfile(resultSet, TableName.users,TableName.user_avatar);
    }
}
