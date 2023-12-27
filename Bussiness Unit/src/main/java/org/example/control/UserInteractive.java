package org.example.control;
import org.example.model.Location;
import org.example.model.POI;
import org.example.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class UserInteractive {
    private SqlitePOIStore poiStore;
    private SqliteWeatherStore weatherStore;

    String csvFile = "C:\\Users\\laura\\Downloads\\WeatherApp Entrega 3\\Bussiness Unit\\src\\main\\resources\\locations.csv";
    List<Location> locations = readCSV(csvFile);

    public UserInteractive(SqlitePOIStore poiStore, SqliteWeatherStore weatherStore) {
        this.poiStore = poiStore;
        this.weatherStore = weatherStore;
    }
    public void startInteraction() {
        Scanner scanner = new Scanner(System.in);
        boolean wantsToContinue = true;

        while (wantsToContinue) {
            System.out.println("¿Qué ciudad de Europa te interesa? (Solo capitales)");
            String city = scanner.nextLine().trim();

            Location location = getLocationByCity(locations, city);

            if (isValidCity(city)) {

                List<POI> pois = poiStore.getPoisByCity(city, "points.Of.Interest", location);
                if (!pois.isEmpty()) {
                    System.out.println("Puntos de interés en " + city + ":");
                    for (POI poi : pois) {
                        System.out.println("- " + poi.getName() + " (" + poi.getKinds() + ")");
                    }
                } else {
                    System.out.println("Lo siento, no se encontraron puntos de interés para " + city);
                }
                Instant instant = Instant.now();
                List<Weather> weatherData = weatherStore.getWeatherByCity(city, "prediction.Weather", instant.toString(), location);
                if (!weatherData.isEmpty()) {
                    for (Weather weather : weatherData) {
                        if (weather.getPrecipitation() > 80) {
                            System.out.println("¡El nivel de precipitaciones es alto! Sería mejor quedarse en el hotel.");
                        }
                        if (weather.getHumidity() > 70) {
                            System.out.println("¡Hay mucha humedad! Llévate un buen abrigo contigo.");
                        }
                        if (weather.getClouds() < 30) {
                            System.out.println("¡Habrá pocas nubes! Será un día soleado perfecto para hacer turismo.");
                        }
                        System.out.println("¿Quieres más detalles sobre el clima en " + city + "? (Sí/No)");
                        String moreDetails = scanner.nextLine().trim().toLowerCase();
                        if (moreDetails.equals("sí") || moreDetails.equals("si")) {
                            System.out.println("Información del clima en " + city + ":");
                            System.out.println("- Precipitaciones: " + weather.getPrecipitation());
                            System.out.println("- Humedad: " + weather.getHumidity());
                            System.out.println("- Nubes: " + weather.getClouds());
                            System.out.println("- Temperatura: " + weather.getTemperature());
                        }
                    }
                } else {
                    System.out.println("Lo siento, no hay datos de clima para " + city);
                }
                System.out.println("¿Quieres consultar otra ciudad? (Sí/No)");
                String continueOption = scanner.nextLine().trim().toLowerCase();
                if (continueOption.equals("no")) {
                    wantsToContinue = false;
                }
            } else {
                System.out.println("Lo siento, esta aplicación solo trabaja con capitales de Europa.");
            }
        }

        System.out.println("Gracias por usar nuestra aplicación. ¡Hasta luego!");
    }
    private boolean isValidCity(String city) {
        try {
            String tableName = city.replaceAll("\\s", "_");
            DatabaseMetaData metaData = poiStore.connect().getMetaData();
            try (var rs = metaData.getTables(null, null, tableName, null)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la ciudad: " + e.getMessage());
        }
        return false;
    }

    private static List<Location> readCSV(String fileName) {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    double latitude = Double.parseDouble(data[0].trim());
                    double longitude = Double.parseDouble(data[1].trim());
                    String city = data[2].trim();
                    Location location = new Location(latitude, longitude, city);
                    locations.add(location);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return locations;
    }
    private static Location getLocationByCity(List<Location> locations, String city) {
        for (Location location : locations) {
            if (location.getCity().equalsIgnoreCase(city)) {
                return location;
            }
        }
        return null;
    }
}
