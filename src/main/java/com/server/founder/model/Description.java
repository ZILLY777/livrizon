package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Description {
    String status;
    Date birthday;
    String city;

    public Description() {
    }

    public Description(ResultSet resultSet) throws SQLException {
        this.status = resultSet.getString("user_status");
        this.birthday = resultSet.getDate("birthday");
        this.city = resultSet.getString("city");
    }
}
