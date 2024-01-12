package org.example.control;

import org.example.model.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {
    private final OpenWeatherMapSupplier openWeatherMapSupplier;
    private final JmsWeatherPublisher jmsWeatherPublisher;

    public WeatherController(OpenWeatherMapSupplier openWeatherMapSupplier, JmsWeatherPublisher jmsWeatherPublisher) {
        this.openWeatherMapSupplier = openWeatherMapSupplier;
        this.jmsWeatherPublisher = jmsWeatherPublisher;
    }

    private List<Location> readCSV(String csvFilePath) {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            reader.lines().forEach(line -> parseCSVLine(line, locations));
        } catch (IOException e) {
            throw new RuntimeException("Error reading the CSV file: " + e.getMessage(), e);
        }
        return locations;
    }

    private void parseCSVLine(String line, List<Location> locations) {
        String[] parts = line.split(",");
        try {
            double lat = Double.parseDouble(parts[0]);
            double lon = Double.parseDouble(parts[1]);
            locations.add(new Location(lat, lon, parts[2].trim()));
        } catch (NumberFormatException e) {
            System.err.println("Error converting values in the CSV file: " + e.getMessage());
        }
    }

    private void processWeather(List<Location> listLocation) {
        listLocation.forEach(location -> {
            try {
                openWeatherMapSupplier.getWeather(location)
                        .forEach(jmsWeatherPublisher::publishWeather);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void processWeatherFile(String csvFile) {
        try {
            List<Location> listLocation = readCSV(csvFile);
            processWeather(listLocation);
            System.out.println("The task is finished");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error processing weather data: " + e.getMessage(), e);
        }
    }
}
