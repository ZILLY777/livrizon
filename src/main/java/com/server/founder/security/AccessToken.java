package com.server.founder.security;

import com.server.founder.model.Role;
import lombok.Data;

@Data
public class AccessToken {
    int id;
    String sub;
    Role role;
    long exp;
    long iat;

    public AccessToken() {
    }
}
