package org.example.control;

public interface EventStore {
    void processEvent(String eventData);
    String extractIsland(com.google.gson.JsonObject eventJson);
    void writeEventToFile(java.io.File eventFile, String island, String eventData);
    String createDirectory(String ss);
}
