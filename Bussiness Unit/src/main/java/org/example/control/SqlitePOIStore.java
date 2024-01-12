package org.example.control;
import org.example.model.Location;
import org.example.model.POI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class SqlitePOIStore implements POIStore {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:POI.db");
    }
    private boolean tableExist(Connection connection, Location location) throws SQLException {
        String tableName = location.getCity();
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        }
    }
    private void createTable(Connection connection, Location location) {
        try {
            if (tableExist(connection, location)) {
                String tableName = location.getCity();
                String createTableQuery = "CREATE TABLE \"" + tableName + "\" (" +
                        "\"name\" TEXT, \"kinds\" TEXT, \"lat\" REAL, \"lon\" REAL);";
                executeStatement(connection, createTableQuery);
            }
        } catch (SQLException e) {
            System.out.println("Error creating the table: " + e.getMessage());
        }
    }
    public void insertPOI(Connection connection, POI poi, Location location) {
        if (poi == null || location == null) {
            return;
        }
        try {
            createTable(connection, location);
            String tableName = location.getCity();
            String selectQuery = "SELECT * FROM \"" + tableName + "\" WHERE name=?";
            String insertQuery = "INSERT INTO \"" + tableName + "\" (\"name\", \"kinds\", \"lat\", \"lon\") VALUES (?, ?, ?, ?)";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, poi.getName());
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return;
                    }
                }
            }
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, poi.getName());
                insertStatement.setString(2, poi.getKinds());
                insertStatement.setDouble(3, poi.getLat());
                insertStatement.setDouble(4, poi.getLon());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error interacting with the database: " + e.getMessage());
        }
    }
    public void save(POI poi, Location location) {
        try {
            Connection connection = connect();
            insertPOI(connection, poi, location);
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error interacting with the database: " + e.getMessage());
        }
    }
    private void executeStatement(Connection connection, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error executing: " + e.getMessage());
        }
    }
    public List<POI> getPoisByCity(String city, String ss, Location location) {
        List<POI> pois = new ArrayList<>();
        try (Connection connection = connect()) {
            String selectQuery = "SELECT * FROM \"" + city + "\"";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String kinds = resultSet.getString("kinds");
                        double lat = resultSet.getDouble("lat");
                        double lon = resultSet.getDouble("lon");
                        pois.add(new POI(ss, name, kinds, lat, lon, location));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting points of interest: " + e.getMessage());
        }
        return pois;
    }
}