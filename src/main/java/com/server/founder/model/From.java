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
public class From {
    long date;
    UserProfile userProfile;
    public From() {
    }
    public From(ResultSet resultSet, String tableName1,String tableName2,String tableName3) throws SQLException {
        this.date = resultSet.getTimestamp(Function.concat(tableName2, Column.date)).getTime()/1000;
        this.userProfile =new UserProfile(resultSet,tableName1,tableName3);
    }
}
