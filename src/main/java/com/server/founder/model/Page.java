package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.SQLException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@JsonInclude(NON_NULL)
public class Page {
    PageInformation information;
    List<Post> posts;
    public Page() {
    }
    public Page(PageInformation information, List<Post> posts) throws SQLException {
        this.information = information;
        if(posts.size()>0) this.posts = posts;
    }
}
