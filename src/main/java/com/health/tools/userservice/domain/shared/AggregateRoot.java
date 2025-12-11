package com.health.tools.userservice.domain.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for Aggregate Roots in DDD.
 * An Aggregate Root is the main entry point to an aggregate.
 */
public abstract class AggregateRoot<ID> {
    
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    /**
     * Get the unique identifier of this aggregate.
     */
    public abstract ID getId();
    
    /**
     * Register a domain event.
     */
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    /**
     * Get all domain events and clear the list.
     */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return Collections.unmodifiableList(events);
    }
    
    /**
     * Get domain events without clearing.
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }
    
    /**
     * Clear all domain events.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
