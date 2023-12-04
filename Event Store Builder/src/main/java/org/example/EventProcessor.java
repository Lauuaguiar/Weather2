package org.example;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventProcessor {

    public void processEvent(String eventData) {
        try {
            JsonObject eventJson = JsonParser.parseString(eventData).getAsJsonObject();

            String ts = eventJson.get("ts").getAsString();
            String ss = eventJson.get("ss").getAsString();
            String predictionTime = eventJson.get("instant").getAsString();
            double temperature = eventJson.get("temperature").getAsDouble();
            double precipitation = eventJson.get("precipitation").getAsDouble();
            double wind = eventJson.get("wind").getAsDouble();
            int humidity = eventJson.get("humidity").getAsInt();
            int clouds = eventJson.get("clouds").getAsInt();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = dateFormat.format(new Date());

            SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileDate = fileDateFormat.format(new Date());

            String directoryPath = "eventstore/prediction.Weather/" + ss + "/" + currentDate + ".events";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String eventFilePath = directoryPath + "/event_" + fileDate + ".txt";
            File eventFile = new File(eventFilePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(eventFile))) {
                writer.write("Prediction Time: " + predictionTime + "\n");
                writer.write("Temperature: " + temperature + "\n");
                writer.write("Humidity: " + humidity + "\n");
                writer.write("Clouds: " + clouds + "\n");
                writer.write("Wind: " + wind + "\n");
                writer.write("Precipitation: " + precipitation + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Evento almacenado en: " + eventFilePath);
        } catch (Exception e) {
            System.err.println("Error al procesar el evento: " + e.getMessage());
        }
    }
}