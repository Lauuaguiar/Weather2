package org.example.model;

public class Location {
    private double lat;
    private double lon;
    private String island;

    public Location(double lat, double lon, String island) {
        this.lat = lat;
        this.lon = lon;
        this.island = island;
    }

    public Location(String island) {
        this.island = island;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getIsland() {
        return island;
    }

}
