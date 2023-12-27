package org.example.model;


public class POI {
    private final String ss;
    private String name;
    private String kinds;
    private double lat;
    private double lon;
    private Location location;

    public POI(String ss, String name, String kinds, double lat, double lon, Location location) {
        this.ss = ss;
        this.name = name;
        this.kinds = kinds;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
    }


    @Override
    public String toString() {
        return "{" +
                "ss='" + ss + '\'' +
                "name='" + name + '\'' +
                ", kinds='" + kinds + '\'' +
                ", latitude=" + lat +
                ", longitude=" + lon +
                '}';
    }
}