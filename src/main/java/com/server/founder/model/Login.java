package com.server.founder.model;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Login {
    String username;
    Registration registration;
    short code;

    public Login() {
    }

    public Login(ResultSet resultSet) throws SQLException {
        this.username = resultSet.getString("username");
        this.registration = Registration.valueOf(resultSet.getString("registration"));
        this.code = resultSet.getShort("code");
    }
}
