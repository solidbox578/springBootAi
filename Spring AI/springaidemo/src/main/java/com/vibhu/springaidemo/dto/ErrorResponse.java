package com.vibhu.springaidemo.dto;

import lombok.Data;

@Data
public class ErrorResponse {

    public record ErrorDetail(String type, String message) {
        public String getType() { return type(); }
        public String getMessage() { return message(); }
    }

    private ErrorDetail error;
    private String message;
    private String type;
    private String param;
    private int code;
    private String request_id;
}
