package com.health.tools.userservice.domain.shared;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for Domain Events in DDD.
 * Domain Events represent something that happened in the domain.
 */
public abstract class DomainEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public Instant getOccurredOn() {
        return occurredOn;
    }
    
    /**
     * Get the name of the event type.
     */
    public abstract String getEventType();
}
