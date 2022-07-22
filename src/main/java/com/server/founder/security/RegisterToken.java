package com.server.founder.security;

import lombok.Data;

@Data
public class RegisterToken {
    String sub;
    long exp;
    long iat;

    public RegisterToken() {}
}
