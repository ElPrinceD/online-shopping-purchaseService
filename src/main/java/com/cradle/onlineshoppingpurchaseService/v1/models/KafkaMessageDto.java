package com.cradle.onlineshoppingpurchaseService.v1.models;


import com.cradle.onlineshoppingpurchaseService.v1.enums.OperationType;
import lombok.Data;

@Data
public class KafkaMessageDto {
    private String requestId;
    private OperationType operationType;
    private UserResponseDto response;
}
