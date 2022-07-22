package com.server.founder.model;

import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class FileInformation {
    UserProfile user;
    Statistic statistic;

    public FileInformation() {
    }

    public FileInformation(ResultSet resultSet) throws SQLException {
        this.user = new UserProfile(resultSet, TableName.users,TableName.user_avatar);
        this.statistic = new Statistic(resultSet);
    }
}
