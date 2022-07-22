package com.server.founder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.founder.response.PreviewFiles;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@JsonInclude(NON_NULL)
public class Page {
    UserInformation user;
    List<Post> posts;
    public Page() {
    }
    public Page(UserInformation information,  List<Post> posts) {
        this.user = information;
        if(posts.size()>0) this.posts = posts;
    }
}
