package com.server.founder.model;

import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class Recommendation {
    int mutual;
    UserProfile user;

    public Recommendation() {
    }

    public Recommendation(ResultSet resultSet) throws SQLException {
        this.mutual = resultSet.getInt(Column.mutual);
        this.user = new UserProfile(resultSet, TableName.users,TableName.users);
    }
}
