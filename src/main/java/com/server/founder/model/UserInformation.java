package com.server.founder.model;

import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class UserInformation {
    UserProfile userProfile;
    ProfileStatistic statistic;
    Description description;

    public UserInformation() {
    }
    public UserInformation(ResultSet resultSet) throws SQLException {
        this.userProfile =new UserProfile(resultSet,TableName.users,TableName.user_avatar);
        this.statistic =new ProfileStatistic(resultSet);
        this.description=new Description(resultSet);
    }
}
