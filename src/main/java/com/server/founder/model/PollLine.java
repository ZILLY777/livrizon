package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.sql.Column;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class PollLine {
    int line_id;
    String text;
    int number;
    boolean my_vote;
    public PollLine() {
    }

    public PollLine(int line_id, String text) {
        this.line_id = line_id;
        this.text = text;
    }

}
