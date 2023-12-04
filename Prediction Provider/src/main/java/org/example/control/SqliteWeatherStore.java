package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class SqliteWeatherStore implements WeatherStore {

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:DataBase.db");
    }

    private void createTable(Connection connection, Location location) {
        if (location == null) {
            System.out.println("La ubicación en Weather no es válida.");
            return;
        }

        String tableName = location.getIsland();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "Date TEXT, Temperature REAL, Rain REAL, Humidity INTEGER, Clouds INTEGER, Wind REAL);";
        executeStatement(connection, createTableQuery, "Se ha creado la tabla de la isla " + location.getIsland());
    }

    public void insert(Connection connection, Weather weather, Location location, Instant instant) {
        if (instant == null || location == null) {
            System.out.println(instant == null ? "La fecha en Weather no es válida." : "La ubicación en Weather no es válida.");
            return;
        }

        String tableName = location.getIsland();
        String insertQuery = "INSERT INTO " + tableName + "(Date, Temperature, Rain, Humidity, Clouds, Wind)" +
                " SELECT ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM " + tableName + " WHERE Date = ?)";
        executeInsertUpdate(connection, insertQuery, weather, instant);
    }

    private void update(Connection connection, Weather weather, Location location, Instant instant) {
        if (location == null) {
            System.out.println("La ubicación en Weather no es válida.");
            return;
        }

        String tableName = location.getIsland();
        String updateQuery = "UPDATE " + tableName +
                " SET Date = ?, Temperature = ?, Rain = ?, Humidity = ?, Clouds = ?, Wind = ? WHERE Date = ?";
        executeInsertUpdate(connection, updateQuery, weather, instant);
        System.out.println("Updated");
    }

    public void save(Weather weather, Location location, Instant instant) {
        try {
            Connection connection = connect();
            createTable(connection, location);
            update(connection, weather, location, instant);
            insert(connection, weather, location, instant);
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

    private void executeInsertUpdate(Connection connection, String query, Weather weather, Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = localDateTime.format(formatter);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, formattedDate);
            preparedStatement.setFloat(2, weather.getTemperature());
            preparedStatement.setFloat(3, weather.getPrecipitation());
            preparedStatement.setInt(4, weather.getHumidity());
            preparedStatement.setInt(5, weather.getClouds());
            preparedStatement.setFloat(6, weather.getWind());
            preparedStatement.setString(7, formattedDate);

            preparedStatement.executeUpdate();
            System.out.println("Datos insertados correctamente");
        } catch (SQLException e) {
            System.out.println("Error al ejecutar: " + e.getMessage());
        }
    }
}
