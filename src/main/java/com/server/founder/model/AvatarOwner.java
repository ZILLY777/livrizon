package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class AvatarOwner {
    int user_id;
    String url;

    public AvatarOwner() {
    }

    public AvatarOwner(String column,ResultSet resultSet) throws SQLException {
        this.user_id = resultSet.getInt(column);
        this.url = resultSet.getString(Column.url);
    }
}
