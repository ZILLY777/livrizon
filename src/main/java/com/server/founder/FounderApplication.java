package com.server.founder;

import com.server.founder.request.Request;
import com.server.founder.sql.Statement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class FounderApplication {
    public static void main(String[] args) throws SQLException {
        Request.createTables();
        SpringApplication.run(FounderApplication.class, args);
    }
}
