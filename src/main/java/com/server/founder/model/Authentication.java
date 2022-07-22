package com.server.founder.model;

import lombok.Data;

@Data
public class Authentication {
    String username;
    String password;
    public Authentication() {}
    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
