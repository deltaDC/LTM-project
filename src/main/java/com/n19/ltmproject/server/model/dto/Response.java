package com.n19.ltmproject.server.model.dto;

import lombok.Data;

@Data
public class Response {

    private String status;
    private String message;
    private Object data;
}
