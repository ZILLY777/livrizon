package com.server.founder.model;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class FileDb {
    int file_id;
    String url;
    String contentType;
    long size;
    byte[] bytes;

    public FileDb() {
    }

    public FileDb(ResultSet resultSet) throws SQLException {
        this.file_id = resultSet.getInt("file_id");
        this.url = resultSet.getString("url");
        this.contentType = resultSet.getString("contentType");
        this.size = resultSet.getLong("size");
        this.bytes = resultSet.getBytes("byte");
    }

}
