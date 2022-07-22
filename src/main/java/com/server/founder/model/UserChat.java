package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.function.Function;
import com.server.founder.sql.Column;
import com.server.founder.sql.TableName;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class UserChat {
    int chat_id;
    ChatType type;
    ChatRole role;
    UserProfile user;
    SocietyProfile society;
    MessageView message;
    public UserChat() {
    }

    public UserChat(ResultSet resultSet) throws SQLException {
        this.chat_id = resultSet.getInt(Column.chat_id);
        this.type = ChatType.valueOf(resultSet.getString(Column.type));
        this.role = ChatRole.valueOf(resultSet.getString(Column.role));
        if(resultSet.getObject(Function.concat(TableName.users,Column.user_id))!=null)
            this.user = new UserProfile(resultSet, TableName.users,TableName.user_avatar);
        if(resultSet.getObject(Column.public_chat_id)!=null)
            this.society = new SocietyProfile(resultSet);
        this.message = new MessageView(resultSet);
    }
}
