package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@Getter
public class CustomBody<T>  implements Serializable {
    private String requestId;
    private int status;
    private String message;
    private Object data;
    private @Setter
    List<Error> errors;
    private long totalCount;
    private int page;
    private int pageSize;

    public CustomBody(Object request, int status, List<T> data, List<Error> errors, long totalCount, int page, int pageSize) {
        if (request != null) {
            if (request instanceof HttpServletRequest) {
                Object requestId = ((HttpServletRequest) request).getAttribute("requestId");
                this.requestId = ObjectUtils.isEmpty(requestId) ? null : (String) requestId;
            } else {
                this.requestId = (String) request;
            }
        }
        this.status = status;
        this.data = data;
        this.errors = errors;
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
    }



    public CustomBody(Object request, HttpStatus status, Object data, List<Error> errors) {
        if (request != null) {
            if (request instanceof HttpServletRequest) {
                Object requestId = ((HttpServletRequest) request).getAttribute("requestId");
                this.requestId = StringUtils.isEmpty(requestId) ? null : (String) requestId;
            } else {
                this.requestId = (String) request;
            }
        }

        this.status = status.value();
        this.message = status.getReasonPhrase();
        this.data = data;
        this.errors = errors;
        this.page = 1;
        this.totalCount = 0;
        this.pageSize = 0;
    }
}



