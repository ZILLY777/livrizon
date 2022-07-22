package com.server.founder.model;

import lombok.Data;

@Data
public class Response {
    ResponseState response;

    public Response(ResponseState response) {
        this.response = response;
    }
}
