package com.server.founder.model;

import lombok.Data;

import java.util.List;
@Data
public class PollView {
    int total;
    boolean my_vote;
    List<PollLine> lines;

    public PollView() {
    }

    public PollView(int total, boolean my_vote, List<PollLine> lines) {
        this.total = total;
        this.my_vote = my_vote;
        this.lines = lines;
    }
}
