package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class Link {
    int gen;
    int number;

    public Link(ResultSet resultSet) throws SQLException {
        this.gen = resultSet.getInt(Column.gen);
        this.number = resultSet.getInt(Column.number);
    }
}
