package com.spring.learning.jobportal.otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenTelemetryAppenderInitialized implements InitializingBean {

    private final OpenTelemetry openTelemetry;



    @Override
    public void afterPropertiesSet() {
        // Initialize the OpenTelemetry appender with the provided configuration
        OpenTelemetryAppender.install(openTelemetry);
    }
}
