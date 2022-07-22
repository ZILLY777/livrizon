package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class PostFile {
    int post_id;
    int file_id;
    File file;

    public PostFile() {
    }

    public PostFile(ResultSet resultSet) throws SQLException {
        this.post_id = resultSet.getInt(Column.post_id);
        this.file_id = resultSet.getInt(Column.file_id);
        this.file = new File(resultSet,Column.post_file_id);
    }
}
