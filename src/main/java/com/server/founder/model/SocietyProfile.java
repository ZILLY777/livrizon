package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class SocietyProfile {
    int society_id;
    String name;
    String avatar;

    public SocietyProfile() {
    }

    public SocietyProfile(ResultSet resultSet) throws SQLException {
        this.society_id = resultSet.getInt(Column.public_chat_id);
        this.name = resultSet.getString(Column.name);
        this.avatar = resultSet.getString(Column.url);
    }
}
