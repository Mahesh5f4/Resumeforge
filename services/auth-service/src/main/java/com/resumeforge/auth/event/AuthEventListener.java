package com.resumeforge.auth.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEventListener {

    @Async("auditTaskExecutor")
    @EventListener
    public void handleAuthEvent(AuthEvent event) {
        // In a real production system, this could save to an audit table or push to Kafka
        log.info("AUDIT EVENT: [{}] - User: {} - Details: {}", event.getEventType(), event.getEmail(), event.getDetails());
    }
}
