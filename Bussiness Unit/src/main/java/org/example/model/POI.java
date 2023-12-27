package org.example.model;


public class POI {
    private String ss;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public String getSs() {
        return ss;
    }
}