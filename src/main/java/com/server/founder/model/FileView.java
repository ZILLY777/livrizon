package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class FileView {
    int post_id;
    File file;

    public FileView() {
    }

    public FileView(ResultSet resultSet) throws SQLException {
        this.post_id = resultSet.getInt(Column.user_post_id);
        this.file = new File(resultSet,Column.post_file_id);
    }
}
