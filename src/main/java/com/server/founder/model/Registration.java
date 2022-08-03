package com.server.founder.model;


import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Registration {
    String password;
    UserType userType;
    String name;
    String description;
    int city_id;
    String birthday;
    Gender gender;
    List<String> skills;
    public Registration() {
    }
}
