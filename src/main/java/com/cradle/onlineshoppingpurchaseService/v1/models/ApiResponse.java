package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public class ApiResponse<T> extends ResponseEntity<T> {

    public ApiResponse(HttpServletRequest request, HttpStatus status, Object body, List<Error> errors) {
        super((T) new CustomBody<T>(request, status, body, errors), status);
    }
}
