package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.models.ProjectStatusChangeDto;
import com.cradle.onlineshoppingpurchaseService.v1.models.SendEmailDto;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Objects;

@Service
public class TemplateService {
    private final SpringTemplateEngine templateEngine;

    public TemplateService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        Objects.requireNonNull(templateEngine, "template engine is required");
    }

    public SendEmailDto generateProjectStatusChangeEmail(ProjectStatusChangeDto projectStatus) {
        Context context = new Context();
        context.setVariable("fullName", projectStatus.getAuthorFullName());
        context.setVariable("productName", projectStatus.getProductName());

        return SendEmailDto
                .builder()
                .text(templateEngine.process("project-status-change.txt", context))
                .html(templateEngine.process("project-status-change.html", context))
                .build();
    }
}

