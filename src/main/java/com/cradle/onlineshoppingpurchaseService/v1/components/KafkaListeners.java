package com.cradle.onlineshoppingpurchaseService.v1.components;

import com.cradle.onlineshoppingpurchaseService.v1.models.KafkaMessageDto;
import com.cradle.onlineshoppingpurchaseService.v1.models.UserResponseDto;
import com.cradle.onlineshoppingpurchaseService.v1.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class KafkaListeners {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public KafkaListeners(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        Objects.requireNonNull(objectMapper, "object mapper is required");
        Objects.requireNonNull(userService,"user service is required");
    }

    @KafkaListener(topics = "user", groupId = "group")
    void listener(String jsonString) throws JsonProcessingException {
        KafkaMessageDto message = objectMapper.readValue(jsonString, KafkaMessageDto.class);


        log.info("[" +message.getRequestId() + "] is about to process request to add user to the database");

        switch (message.getOperationType()){
            case CREATE:
                userService.processCreateUserMessage(message.getRequestId(), message.getResponse());
                break;
            case UPDATE:
                userService.processUpdateUserMessage(message.getRequestId(), message.getResponse());
                break;
            case DELETE:
                userService.processDeleteUserMessage(message.getRequestId(), message.getResponse());
        }

    }
}
