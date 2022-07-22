package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.function.Function;
import com.server.founder.request.Request;
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
public class PostBody {
    String text;
    List<File> files;
    public PostBody() {
    }
    public PostBody(ResultSet resultSet, Connection connection) throws SQLException {
        this.text = resultSet.getString(Function.concat(TableName.posts, Column.text));
        List<File> list=Request.getPostPostFilesById(resultSet.getInt(Column.post_id),connection);
        if(list.size()>0) this.files = list;
    }
}
