package com.server.founder.model;

import lombok.Data;

import java.util.List;
@Data
public class EditPoll {
    String theme;
    List<String> append;
    List<EditLine> update;
    List<Integer> delete;

    public EditPoll() {
    }
}
