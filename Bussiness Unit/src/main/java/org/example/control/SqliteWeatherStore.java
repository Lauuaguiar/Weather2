package org.example.control;
import org.example.model.Location;
import org.example.model.Weather;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SqliteWeatherStore implements WeatherStore {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:Weather.db");
    }

    private void createTable(Connection connection, Location location) {
        if (location == null) {
            System.out.println("La ubicación en Weather no es válida.");
            return;
        }

        String tableName = location.getCity();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS \"" + tableName + "\" (" +
                "\"Date\" TEXT, \"Temperature\" REAL, \"Rain\" REAL, \"Humidity\" INTEGER, \"Clouds\" INTEGER, \"Wind\" REAL);";
        executeStatement(connection, createTableQuery, "Se ha creado la tabla de la isla " + location.getCity());
    }

    public void insertWeather(Connection connection, Weather weather, Location location, String instant) {
        if (instant == null || location == null) {
            System.out.println(instant == null ? "La fecha en Weather no es válida." : "La ubicación en Weather no es válida.");
            return;
        }
        String tableName = location.getCity();
        String insertQuery = "INSERT INTO " + tableName + "(Date, Temperature, Rain, Humidity, Clouds, Wind)" +
                " SELECT ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM " + tableName + " WHERE Date = ?)";
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT 1 FROM " + tableName + " WHERE Date = ?")) {
            selectStatement.setString(1, weather.getPredictionTime());
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Los datos de clima ya existen para la fecha proporcionada y la ubicación.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al comprobar la existencia de datos de clima: " + e.getMessage());
            return;
        }

        executeInsertUpdate(connection, insertQuery, weather);
    }


    private void updateWeather(Connection connection, Weather weather, Location location) {
        if (location == null) {
            System.out.println("La ubicación en Weather no es válida.");
            return;
        }

        String tableName = location.getCity();
        String updateQuery = "UPDATE " + tableName +
                " SET Date = ?, Temperature = ?, Rain = ?, Humidity = ?, Clouds = ?, Wind = ? WHERE Date = ?";
        executeInsertUpdate(connection, updateQuery, weather);
        System.out.println("Updated");
    }

    public void save(Weather weather, Location location, String instant) {
        try {
            Connection connection = connect();
            createTable(connection, location);
            updateWeather(connection, weather, location);
            insertWeather(connection, weather, location, instant);
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al interactuar con la base de datos: " + e.getMessage());
        }
    }

    private void executeStatement(Connection connection, String query, String successMessage) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println(successMessage);
        } catch (SQLException e) {
            System.out.println("Error al ejecutar: " + e.getMessage());
        }
    }

    private void executeInsertUpdate(Connection connection, String query, Weather weather) {


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, weather.getPredictionTime());
            preparedStatement.setDouble(2, weather.getTemperature());
            preparedStatement.setDouble(3, weather.getPrecipitation());
            preparedStatement.setInt(4, weather.getHumidity());
            preparedStatement.setInt(5, weather.getClouds());
            preparedStatement.setDouble(6, weather.getWind());

            preparedStatement.executeUpdate();
            System.out.println("Datos insertados correctamente");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar: " + e.getMessage());
        }
    }

    public List<Weather> getWeatherByCity(String city, String ss, String  ts, Location location) {
        List<Weather> weatherData = new ArrayList<>();

        try (Connection connection = connect()) {
            String selectQuery = "SELECT * FROM \"" + city + "\"";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String predictionTime = resultSet.getString("Date");
                        double temperature = resultSet.getDouble("Temperature");
                        double precipitation = resultSet.getDouble("Rain");
                        int humidity = resultSet.getInt("Humidity");
                        int clouds = resultSet.getInt("Clouds");
                        double wind = resultSet.getDouble("Wind");
                        weatherData.add(new Weather(ts, ss, predictionTime, temperature, precipitation, wind, humidity, clouds, location));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener datos de clima: " + e.getMessage());
        }

        return weatherData;
    }

}