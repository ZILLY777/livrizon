package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Relation {
    boolean my_sub;
    boolean it_sub;
    Object gen;

    public Relation() {
    }

    public Relation(ResultSet resultSet) throws SQLException {
        this.my_sub = resultSet.getBoolean(Column.my_sub);
        this.it_sub = resultSet.getBoolean(Column.it_sub);
        this.gen = resultSet.getObject(Column.gen);
    }
}
