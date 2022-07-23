package com.server.founder.model;

import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class MySubscribe {
    int subscribe_id;
    UserProfile userProfile;

    public MySubscribe() {
    }

    public MySubscribe(ResultSet resultSet) throws SQLException {
        this.subscribe_id = resultSet.getInt(Column.subscribe_id);
        this.userProfile = new UserProfile(resultSet, TableName.users,TableName.user_avatar);
    }
}
