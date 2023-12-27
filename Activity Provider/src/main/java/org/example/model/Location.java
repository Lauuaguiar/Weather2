package org.example.model;

public class Location {

    private final double lat;
    private final double lon;
    private final String city;

    public Location(double lat, double lon, String city) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", city='" + city + '\'' +
                '}';
    }
}
