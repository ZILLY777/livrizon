package com.server.founder.model;

import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class Owner {
    int user_id;
    String name;
    public Owner() {
    }
    public Owner(ResultSet resultSet,String tableName) throws SQLException {
        this.user_id = resultSet.getInt(Function.concat(tableName,Column.user_id));
        this.name = resultSet.getString(Function.concat(tableName,Column.name));
    }
}
