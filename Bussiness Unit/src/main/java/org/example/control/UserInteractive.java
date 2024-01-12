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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class UserInteractive {
    private SqlitePOIStore poiStore;
    private SqliteWeatherStore weatherStore;
    private static final String CSV_FILE_PATH = "C:\\Users\\laura\\Downloads\\WeatherApp Entrega 3\\Bussiness Unit\\src\\main\\resources\\locations.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private List<Location> locations;
    public UserInteractive(SqlitePOIStore poiStore, SqliteWeatherStore weatherStore) {
        this.poiStore = poiStore;
        this.weatherStore = weatherStore;
        this.locations = readCSV(CSV_FILE_PATH);
    }
    public void startInteraction() {
        Scanner scanner = new Scanner(System.in);
        boolean wantsToContinue = true;
        while (wantsToContinue) {
            System.out.println("Which European city are you interested in? (Only capitals)");
            String city = scanner.nextLine().trim();
            Location location = getLocationByCity(locations, city);
            if (isValidCity(city)) {
                List<POI> pois = poiStore.getPoisByCity(city, "points.Of.Interest", location);
                if (!pois.isEmpty()) {
                    System.out.println("Points of interest in " + city + ":");
                    for (POI poi : pois) {
                        System.out.println("- " + poi.getName() + " (" + poi.getKinds() + ")");
                    }
                } else {
                    System.out.println("Sorry, no points of interest found for " + city);
                }
                Instant instant = Instant.now();
                List<Weather> weatherData = weatherStore.getWeatherByCity(city, "prediction.Weather", instant.toString(), location);
                if (!weatherData.isEmpty()) {
                    System.out.println("Do you want more details about the weather in " + city + "? (Yes/No)");
                    String moreDetails = scanner.nextLine().trim().toLowerCase();
                    if (moreDetails.equals("yes") || moreDetails.equals("y")) {
                        System.out.println("Weather information in " + city + " for the day " +
                                formatInstantToLocalDate(Instant.parse(weatherData.get(0).getPredictionTime())) + ":");
                        System.out.println("- Precipitation: " + weatherData.get(0).getPrecipitation());
                        System.out.println("- Humidity: " + weatherData.get(0).getHumidity());
                        System.out.println("- Clouds: " + weatherData.get(0).getClouds());
                        System.out.println("- Temperature: " + weatherData.get(0).getTemperature());
                        if (weatherData.get(0).getPrecipitation() > 80) {
                            System.out.println("High precipitation level! It would be better to stay at the hotel.");
                        }
                        if (weatherData.get(0).getHumidity() > 70) {
                            System.out.println("It's very humid! Bring a good coat with you.");
                        }
                        if (weatherData.get(0).getClouds() < 30) {
                            System.out.println("Few clouds! It will be a sunny day, perfect for sightseeing.");
                        }
                    }
                System.out.println("Do you want to check the weather for tomorrow or the next days? (Tomorrow/Next days)");
                String weatherQuery = scanner.nextLine().trim().toLowerCase();
                if (weatherQuery.equals("tomorrow")) {
                    if (!weatherData.isEmpty()) {
                        Weather weatherTomorrow = weatherData.get(1);
                        System.out.println("Weather information in " + city + " for tomorrow:");
                        System.out.println("- Precipitation: " + weatherTomorrow.getPrecipitation());
                        System.out.println("- Humidity: " + weatherTomorrow.getHumidity());
                        System.out.println("- Clouds: " + weatherTomorrow.getClouds());
                        System.out.println("- Temperature: " + weatherTomorrow.getTemperature());
                    } else {
                        System.out.println("No weather data for tomorrow in " + city);
                    }
                } else if (weatherQuery.equals("next days")) {
                    if (!weatherData.isEmpty()) {
                        System.out.println("Weather information in " + city + " for the next days:");
                        for (Weather weather : weatherData) {
                            System.out.println("- Day " + formatInstantToLocalDate(Instant.parse(weather.getPredictionTime())) + ":");
                            System.out.println("  - Precipitation: " + weather.getPrecipitation());
                            System.out.println("  - Humidity: " + weather.getHumidity());
                            System.out.println("  - Clouds: " + weather.getClouds());
                            System.out.println("  - Temperature: " + weather.getTemperature());
                        }
                    } else {
                        System.out.println("No weather data for the next days in " + city);
                    }
                }
            } else {
                System.out.println("Sorry, no weather data for " + city);
            }
                System.out.println("Do you want to check another city? (Yes/No)");
                String continueOption = scanner.nextLine().trim().toLowerCase();

                if (continueOption.equals("no")) {
                    wantsToContinue = false;
                }
            } else {
                System.out.println("Sorry, this application only works with capitals in Europe.");
            }
        }
        System.out.println("Thank you for using our application. Goodbye!");
    }
    private boolean isValidCity(String city) {
        try {
            String tableName = city.replaceAll("\\s", "_");
            DatabaseMetaData metaData = poiStore.connect().getMetaData();
            try (var rs = metaData.getTables(null, null, tableName, null)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking the city: " + e.getMessage());
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
    private static String formatInstantToLocalDate(Instant instant) {
        return LocalDate.ofInstant(instant, ZoneId.systemDefault()).format(DATE_FORMATTER);
    }
}