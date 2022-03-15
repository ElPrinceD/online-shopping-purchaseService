package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDto {
    private String text;
    private String html;
    private String email;

}
