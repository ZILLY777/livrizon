package com.server.founder.model;


import lombok.Data;

import java.util.Date;


@Data
public class Registration {
    String password;
    Role role;
    String name;
    String description;
    int city_id;
    Date birthday;
    Gender gender;
    String hobbies;
    String skills;
    String qualities;
    public Registration() {
    }
}
