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
    String first_name;
    String last_name;
    public Owner() {
    }
    public Owner(ResultSet resultSet,String tableName) throws SQLException {
        this.user_id = resultSet.getInt(Function.concat(tableName,Column.user_id));
        this.first_name = resultSet.getString(Function.concat(tableName,Column.first_name));
        this.last_name = resultSet.getString(Function.concat(tableName,Column.last_name));
    }
}
