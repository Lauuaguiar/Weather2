package org.example.model;


public class POI {
    private final String ss;
    private String name;
    private String kinds;
    private double lat;
    private double lon;

    public POI(String ss, String name, String kinds, double lat, double lon) {
        this.ss = ss;
        this.name = name;
        this.kinds = kinds;
        this.lat = lat;
        this.lon = lon;
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