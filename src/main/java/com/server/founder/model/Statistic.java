package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class Statistic {
    int likes;
    int comments;
    int reposts;
    boolean my_like;

    public Statistic() {
    }

    public Statistic(ResultSet resultSet) throws SQLException {
        this.likes = resultSet.getInt(Column.likes);
        this.comments = resultSet.getInt(Column.comments);
        this.reposts = resultSet.getInt(Column.reposts);
        this.my_like = resultSet.getBoolean(Column.my_like);
    }
}
