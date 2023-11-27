package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {
    private OpenWeatherMapSupplier openWeatherMapSupplier;
    private SqliteWeatherStore sqliteWeatherStore;

    public WeatherController(OpenWeatherMapSupplier openWeatherMapSupplier, SqliteWeatherStore sqliteWeatherStore) {
        this.openWeatherMapSupplier = openWeatherMapSupplier;
        this.sqliteWeatherStore = sqliteWeatherStore;
    }

    private List<Location> readCSV(String csvFilePath) throws IOException {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    double lat = Double.parseDouble(parts[0]);
                    double lon = Double.parseDouble(parts[1]);
                    String name = parts[2].trim();
                    locations.add(new Location(lat, lon, name));
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir valores en el archivo CSV: " + e.getMessage());

                }
            }
        }
        return locations;
    }

    public void weather(String csvFile) {
        try {
            List<Location> listLocation = readCSV(csvFile);
            for (Location location : listLocation) {
                List<Weather> weatherList = openWeatherMapSupplier.getWeather(location);
                try {
                    for (Weather weather : weatherList) {
                        sqliteWeatherStore.save(weather, location, weather.getInstant());
                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar datos meteorológicos: " + e.getMessage(), e);
        }
    }
}
