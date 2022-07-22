package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.request.PollRequest;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Poll {
    int poll_id;
    PollType type;
    PollViewType view;
    String theme;
    Owner owner;
    List<PollLine> lines;
    List<AvatarOwner> subscribes;
    int total;
    boolean my_vote;

    public Poll() {
    }
    public Poll(Object user_id, ResultSet resultSet, Connection connection) throws SQLException {
        this.poll_id = resultSet.getInt(Column.poll_id);
        this.type = PollType.valueOf(resultSet.getString(Column.type));
        this.view = PollViewType.valueOf(resultSet.getString(Column.view));
        this.theme = resultSet.getString(Column.theme);
        this.owner = new Owner(resultSet,TableName.user_poll);
        PollView pollView = PollRequest.getPollLines(user_id,resultSet.getInt(Column.poll_id),connection);
        this.lines = pollView.getLines();
        if(user_id!=null) this.subscribes = PollRequest.selectMySubOfPull(resultSet.getInt(Column.poll_id),(Integer) user_id,connection);
        this.my_vote = pollView.my_vote;
        this.total = pollView.getTotal();
    }
}
