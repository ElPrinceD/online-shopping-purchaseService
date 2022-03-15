package com.cradle.onlineshoppingpurchaseService.v1.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("Admin")
    ADMIN,

    @JsonProperty("Customer")
    USER,

    @JsonProperty("Producer")
    PRODUCER
}
