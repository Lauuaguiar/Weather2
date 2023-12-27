package org.example.control;

import org.example.model.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class POIController {
    private final OpenTripMapSupplier openTripMapSupplier;
    private final JmsPOIPublisher jmsPOIPublisher;

    public POIController(OpenTripMapSupplier openTripMapSupplier, JmsPOIPublisher jmsPOIPublisher) {
        this.openTripMapSupplier = openTripMapSupplier;
        this.jmsPOIPublisher = jmsPOIPublisher;
    }

    private List<Location> readCSV(String csvFilePath) {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            reader.lines().forEach(line -> parseCSVLine(line, locations));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV: " + e.getMessage(), e);
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
            System.err.println("Error al convertir valores en el archivo CSV: " + e.getMessage());
        }
    }

    private void processPOI(List<Location> listLocation) {
        listLocation.forEach(location -> {
            try {
                openTripMapSupplier.getPOIs(location)
                        .forEach(jmsPOIPublisher::publishPOI);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void POI(String csvFile) {
        try {
            List<Location> listLocation = readCSV(csvFile);
            processPOI(listLocation);
            System.out.println("The task is finished");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al procesar datos: " + e.getMessage(), e);
        }
    }
}
