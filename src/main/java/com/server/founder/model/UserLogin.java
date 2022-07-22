package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
public class UserLogin {
    int user_id;
    String password;
    Role role;

    public UserLogin() {
    }
    public UserLogin(ResultSet resultSet) throws SQLException {
        this.user_id = resultSet.getInt("user_id");
        this.password = resultSet.getString("password");
        this.role = Role.valueOf(resultSet.getString("role"));
    }

    public UserLogin(Integer id, String password, Role role) {
        this.user_id = id;
        this.password = password;
        this.role = role;
    }
}
