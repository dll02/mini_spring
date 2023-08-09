package com.minis.beans.event;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}