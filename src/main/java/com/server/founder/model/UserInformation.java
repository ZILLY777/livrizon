package com.server.founder.model;

import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
public class UserInformation {
    UserProfile profile;
    ProfileStatistic statistic;
    String description;
    Date birthday;
    String city;

    public UserInformation() {
    }
    public UserInformation(ResultSet resultSet) throws SQLException {
        this.profile =new UserProfile(resultSet,TableName.users,TableName.user_avatar);
        this.statistic =new ProfileStatistic(resultSet);
        this.description = resultSet.getString(Column.description);
        this.birthday = resultSet.getDate(Column.birthday);
        this.city = resultSet.getString(Column.city);
    }
}
