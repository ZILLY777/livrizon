package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class UserProfile {
    int user_id;
    String name;
    String avatar;
    boolean confirm;

    public UserProfile() {
    }
    public UserProfile(ResultSet resultSet, String userTable, String avatarTable) throws SQLException {
        this.user_id = resultSet.getInt(Function.concat(userTable,Column.user_id));
        this.name = resultSet.getString(Function.concat(userTable,Column.name));
        this.avatar = resultSet.getString(Function.concat(avatarTable,Column.url));
        this.confirm = resultSet.getBoolean(Function.concat(userTable,Column.confirm));
    }
}
