package com.server.founder.model;

import lombok.Data;

@Data
public class Jwt {
    String jwt;
    public Jwt() {}
    public Jwt(String jwt) {
        this.jwt = jwt;
    }
}
