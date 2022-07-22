package com.server.founder.model;

import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Repost {
    int repost;
    Owner forward_from;
    public Repost() {
    }

    public Repost(ResultSet resultSet) throws SQLException {
        this.repost = resultSet.getInt(Column.repost);
        this.forward_from = new Owner(resultSet, TableName.forward_from);
    }
}
