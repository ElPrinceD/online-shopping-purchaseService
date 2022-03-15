package com.cradle.onlineshoppingpurchaseService.v1.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum OperationType {
    @JsonProperty("sendSms")
    SEND_SMS("sendSms"),

    @JsonProperty("sendEmail")
    SEND_EMAIL("sendEmail"),

    @JsonProperty("create")
    CREATE("create"),

    @JsonProperty("read")
    READ("read"),

    @JsonProperty("update")
    UPDATE("update"),

    @JsonProperty("delete")
    DELETE("delete");

    private final String type;

    OperationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static OperationType fromString(String type) {
        if (StringUtils.hasText(type)) {
            return Arrays.stream(OperationType.values())
                    .filter(
                            e -> type.equalsIgnoreCase(e.toString()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
