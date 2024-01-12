package org.example.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileEventStoreBroker implements EventStore {

    private final String rootPath;

    public FileEventStoreBroker(String rootPath) {
        this.rootPath = rootPath;
    }

    public void save(String event, String... topics) {
        try {
            JsonObject eventJson = JsonParser.parseString(event).getAsJsonObject();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String ts = dateFormat.format(new Date());

            for (String topic : topics) {
                File eventFile = new File(createDirectory(topic, eventJson.get("ss").getAsString()), ts + ".events");
                writeEventToFile(eventFile, event);
            }
        } catch (Exception e) {
            System.err.println("Error processing the event: " + e.getMessage());
        }
    }
    private void writeEventToFile(File eventFile, String events) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(eventFile, true))) {
            writer.write(events + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private File createDirectory(String topic, String ss) {
        String directoryPath = rootPath + "\\datalake\\eventstore\\" + topic + "\\" + ss;
        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdirs();
        return directory;
    }

}

