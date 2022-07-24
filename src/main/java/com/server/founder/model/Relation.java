package com.server.founder.model;

import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Relation {
    int gen;
    int number;
    boolean my_sub;
    boolean it_sub;

    public Relation() {
    }

    public Relation(ResultSet resultSet) throws SQLException {
        this.gen = resultSet.getInt(Column.gen);
        this.number = resultSet.getInt(Column.number);
        this.my_sub = resultSet.getBoolean(Column.my_sub);
        this.it_sub = resultSet.getBoolean(Column.it_sub);
    }
}
