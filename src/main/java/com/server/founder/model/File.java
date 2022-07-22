package com.server.founder.model;

import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class File {
    int file_id;
    String url;
    String contentType;
    long size;
    public File() {
    }

    public File(ResultSet resultSet,String column) throws SQLException {
        this.file_id = resultSet.getInt(column);
        this.url = resultSet.getString(Column.url);
        this.contentType = resultSet.getString(Column.contentType);
        this.size = resultSet.getLong(Column.size);
    }

}
