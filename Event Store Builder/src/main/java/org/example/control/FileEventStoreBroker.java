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

    public void processEvent(String eventData) {
        try {
            JsonObject eventJson = JsonParser.parseString(eventData).getAsJsonObject();

            String eventFilePath = createDirectory(eventJson.get("ss").getAsString()) + "/event_" + System.currentTimeMillis() + ".events";
            File eventFile = new File(eventFilePath);

            writeEventToFile(eventFile, extractIsland(eventJson), eventData);

            System.out.println("Evento almacenado en: " + eventFilePath);
        } catch (Exception e) {
            System.err.println("Error al procesar el evento: " + e.getMessage());
        }
    }

    public String extractIsland(JsonObject eventJson) {
        JsonObject location = eventJson.getAsJsonObject("location");
        return location.get("island").getAsString();
    }

    public void writeEventToFile(File eventFile, String island, String eventData) {
        JsonObject eventJson = JsonParser.parseString(eventData).getAsJsonObject();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(eventFile))) {
            writer.write("Island: " + island + "\n");
            writer.write("Time Stamp: " + eventJson.get("ts").getAsString() + "\n");
            writer.write("Prediction Time: " + eventJson.get("ss").getAsString() + "\n");
            writer.write("Temperature: " + eventJson.get("temperature").getAsDouble() + "\n");
            writer.write("Humidity: " + eventJson.get("humidity").getAsInt() + "\n");
            writer.write("Clouds: " + eventJson.get("clouds").getAsInt() + "\n");
            writer.write("Wind: " + eventJson.get("wind").getAsDouble() + "\n");
            writer.write("Precipitation: " + eventJson.get("precipitation").getAsDouble() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String createDirectory(String ss) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());

        String directoryPath = "eventstore/prediction.Weather/" + ss + "/" + currentDate + ".events";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }


        return directoryPath;
    }
}

