package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ProfileStatistic {
    int followers;
    int subscribes;
    int post_number;
    public ProfileStatistic() {
    }
    public ProfileStatistic(ResultSet resultSet) throws SQLException {
        this.followers = resultSet.getInt(Column.followers);
        this.subscribes = resultSet.getInt(Column.subscribes);
        this.post_number = resultSet.getInt(Column.post_number);
    }

}
