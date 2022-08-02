package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class PageInformation {
    UserProfile profile;
    Relation relation;
    ProfileStatistic statistic;
    String description;
    Date birthday;
    String city;

    public PageInformation() {
    }
    public PageInformation(ResultSet resultSet) throws SQLException {
        this.profile =new UserProfile(resultSet,TableName.users,TableName.user_avatar);
        this.relation = new Relation(resultSet);
        this.statistic =new ProfileStatistic(resultSet);
        this.description = resultSet.getString(Column.description);
        this.birthday = resultSet.getDate(Column.birthday);
        this.city = resultSet.getString(Column.city);
    }
}
