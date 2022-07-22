package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class PrivetMessage {
    int message_id;
    int from;
    String text;
    long date;
    Repost repost;
    public PrivetMessage() {
    }

    public PrivetMessage(ResultSet resultSet) throws SQLException {
        this.message_id = resultSet.getInt(Column.message_id);
        this.from = resultSet.getInt(Column.user_id);
        if(resultSet.getObject(Column.repost)!=null)
            this.repost = new Repost(resultSet);
        this.text = resultSet.getString(Column.text);
        this.date = resultSet.getTimestamp(Column.date).getTime()/1000;
    }
}
