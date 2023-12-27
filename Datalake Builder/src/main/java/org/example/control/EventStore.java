package org.example.control;

public interface EventStore {
    void save(String event, String... topics);
}
