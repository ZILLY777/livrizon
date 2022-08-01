package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Login {
    String username;
    RegistrationType registration;
    short code;

    public Login() {
    }

    public Login(ResultSet resultSet) throws SQLException {
        this.username = resultSet.getString(Column.username);
        this.registration = RegistrationType.valueOf(resultSet.getString(Column.registration));
        this.code = resultSet.getShort(Column.code);
    }
}
