package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.Data;

@Data
public class Error {
    private String field;

    private String message;

    public Error(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
