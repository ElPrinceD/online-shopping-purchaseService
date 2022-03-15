package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStatusChangeDto {
    Long id;

    String productName;

    String authorEmailAddress;

    String authorFullName;
}

