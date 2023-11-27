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
        String url = "jdbc:sqlite:DataBase.db";
        Connection connection = DriverManager.getConnection(url);
        return connection;
    }

    private void createTable(Connection connection, Location location) throws SQLException {
        if (location != null) {
            String tableName = location.getIsland();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                    "Date TEXT,\n" +
                    "Temperature REAL,\n" +
                    "Rain REAL,\n" +
                    "Humidity INTEGER, \n" +
                    "Clouds INTEGER,\n" +
                    "Wind REAL" +
                    ");";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
                System.out.println("Se ha creado la tabla de la isla " + location.getIsland());
            }
        } else {
            System.out.println("La ubicación en Weather no es válida.");
        }
    }

    public void insert(Connection connection, Weather weather, Location location, Instant instant) {
        if (instant != null) {
            LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = localDate.format(formatter);

            if (location != null) {
                String tableName = location.getIsland();
                String insertQuery = "INSERT INTO " + tableName + "(Date, Temperature, Rain, Humidity, Clouds, Wind)" +
                        " SELECT ?, ?, ?, ?, ?, ?" +
                        " WHERE NOT EXISTS (SELECT 1 FROM " + tableName + " WHERE Date = ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, formattedDate);
                    preparedStatement.setFloat(2, weather.getTemperature());
                    preparedStatement.setFloat(3, weather.getPrecipitation());
                    preparedStatement.setInt(4, weather.getHumidity());
                    preparedStatement.setInt(5, weather.getClouds());
                    preparedStatement.setFloat(6, weather.getWind());
                    preparedStatement.setString(7, formattedDate);

                    preparedStatement.executeUpdate();
                    System.out.println("Datos insertados correctamente");
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error al ejecutar: " + e.getMessage());
                }
            } else {
                System.out.println("La ubicación en Weather no es válida.");
            }
        } else {
            System.out.println("La fecha en Weather no es válida.");
        }
    }

    private void update(Connection connection, Weather weather, Location location, Instant instant) {
        if (location != null) {
            String tableName = location.getIsland();
            LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = localDateTime.format(formatter);

            String updateQuery = "UPDATE " + tableName +
                    " SET Date = ?, Temperature = ?, Rain = ?, Humidity = ?, Clouds = ?, Wind = ?" +
                    " WHERE Date = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, formattedDate);
                preparedStatement.setFloat(2, weather.getTemperature());
                preparedStatement.setFloat(3, weather.getPrecipitation());
                preparedStatement.setInt(4, weather.getHumidity());
                preparedStatement.setInt(5, weather.getClouds());
                preparedStatement.setFloat(6, weather.getWind());
                preparedStatement.setString(7, formattedDate);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error al ejecutar: " + e.getMessage());
            }

            System.out.println("Updated");
        } else {
            System.out.println("La ubicación en Weather no es válida.");
        }
    }

    public void save(Weather weather, Location location, Instant instant) {
        try {
            Connection connection = connect();
            createTable(connection, location);
            update(connection, weather, location, instant);
            insert(connection, weather, location, instant);
        } catch (SQLException e) {
            System.out.println("Error al interactuar con la base de datos: " + e.getMessage());
        }
    }
}
