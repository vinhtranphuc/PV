package com.so.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilderService {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(final String templateNameOrUrl, final Map<String, Object> details) {
        return templateEngine.process(templateNameOrUrl, buildContext(details));
    }

    public String buildPlainTextMail(String mailBody, final Map<String, Object> details) {
        for (Map.Entry<String, Object> entry : details.entrySet()) {
            mailBody = mailBody.replace("${".concat(entry.getKey()).concat("}"), entry.getValue().toString());
        }
        return mailBody;
    }

    private Context buildContext(Map<String, Object> details) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : details.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }
}