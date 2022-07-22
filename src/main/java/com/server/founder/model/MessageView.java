package com.server.founder.model;

import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class MessageView {
    int message_id;
    String text;
    long date;
    Owner from;
    public MessageView() {
    }

    public MessageView(ResultSet resultSet) throws SQLException {
        this.message_id = resultSet.getInt(Column.message_id);
        this.text = resultSet.getString(Column.text);
        this.date = resultSet.getTimestamp(Column.date).getTime()/1000;
        this.from = new Owner(resultSet, TableName.message_owner);
    }
}
