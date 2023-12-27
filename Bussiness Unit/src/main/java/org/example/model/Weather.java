package org.example.model;


import java.time.Instant;

public class Weather {

    private String ts;
    private String ss;
    private String predictionTime;
    private double temperature;
    private double precipitation;
    private double wind;
    private int humidity;
    private int clouds;
    private Location location;

    public Weather(String ts, String ss, String predictionTime, double temperature, double precipitation, double wind, int humidity, int clouds, Location location) {
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = location;
    }


    public String getPredictionTime() {
        return predictionTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public double getWind() {
        return wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "predictionTime=" + predictionTime +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", wind=" + wind +
                ", humidity=" + humidity +
                ", clouds=" + clouds +
                ", location=" + location +
                '}';
    }

    public String getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
