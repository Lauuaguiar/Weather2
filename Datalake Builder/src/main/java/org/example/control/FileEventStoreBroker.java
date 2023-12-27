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

    private final File baseDirectory;

    public FileEventStoreBroker(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public void save(String event, String... topics) {
        try {
            JsonObject eventJson = JsonParser.parseString(event).getAsJsonObject();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = dateFormat.format(new Date());

            for (String topic : topics) {
                File eventFile = new File(createDirectory(topic, eventJson.get("ss").getAsString()), currentDate + ".events");
                writeEventToFile(eventFile, event);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar el evento: " + e.getMessage());
        }
    }
    private void writeEventToFile(File eventFile, String eventData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(eventFile, true))) {
            writer.write(eventData + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private File createDirectory(String topic, String ss) {
        String directoryPath = "datalake/eventstore/" + topic + "/" + ss;
        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdirs();
        return directory;
    }
}

