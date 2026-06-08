package com.spring.learning.jobportal.scopes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@Getter
@Setter
@ApplicationScope
public class ApplicationScopedBean {

    private int visitorCount;

    public  ApplicationScopedBean() {
        System.out.println("ApplicationScopedBean created");
    }

    public void incrementVisitorCount() {
        visitorCount++;
    }
}
