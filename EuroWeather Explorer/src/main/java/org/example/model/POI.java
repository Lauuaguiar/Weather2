package org.example.model;
public class POI {
    private final String ss;
    private final String name;
    private final String kinds;
    private final double lat;
    private final double lon;
    private final Location location;
    public POI(String ss, String name, String kinds, double lat, double lon, Location location) {
        this.ss = ss;
        this.name = name;
        this.kinds = kinds;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
    }
    public String getName() {
        return name;
    }
    public String getKinds() {
        return kinds;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public Location getLocation() {
        return location;
    }
    @Override
    public String toString() {
        return "POI{" +
                "name='" + name + '\'' +
                ", kinds='" + kinds + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", location=" + location +
                '}';
    }
}