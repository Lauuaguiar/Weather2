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

    private final File directory;

    public FileEventStoreBroker(File directory) {
        this.directory = directory;
    }

    public void save(String event, String topic) {
        try {
            JsonObject eventJson = JsonParser.parseString(event).getAsJsonObject();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = dateFormat.format(new Date());
            File eventFile = new File(createDirectory(topic, eventJson.get("ss").getAsString()), currentDate + ".events");
            writeEventToFile(eventFile, event);

            System.out.println("Evento almacenado en: " + eventFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error al procesar el evento: " + e.getMessage());
        }
    }

    private void writeEventToFile(File eventFile, String eventData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(eventFile, true))) {
            writer.write(eventData + "\n"); // Agregar el evento y un salto de línea para separarlos
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createDirectory(String topic, String ss) {
        String directoryPath = "eventstore/" + topic + "/" + ss;
        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdirs();
        return directory;
    }
}

