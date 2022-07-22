package com.server.founder.model;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class User {
    int user_id;
    String username;
    String password;
    boolean status;
    long created;
    Role role;
    String first_name;
    String last_name;

    public User() {
    }
    public User(ResultSet resultSet) throws SQLException {
        this.user_id = resultSet.getInt("user_id");
        this.username = resultSet.getString("username");
        this.password = resultSet.getString("password");
        this.status = resultSet.getBoolean("status");
        this.created = resultSet.getTimestamp("created").getTime()/1000;
        this.role = Role.valueOf(resultSet.getString("role"));
        this.first_name = resultSet.getString("first_name");
        this.last_name = resultSet.getString("last_name");
    }

    public User(Integer id, String username, String password, boolean status, long created, Role role, String first_name, String last_name, String avatar) {
        this.user_id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.created = created;
        this.role = role;
        this.first_name = first_name;
        this.last_name = last_name;
        //this.avatar = avatar;
    }
}
