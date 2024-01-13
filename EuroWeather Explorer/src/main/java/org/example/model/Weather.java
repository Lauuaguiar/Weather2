package org.example.model;
public class Weather {
    private final String ts;
    private final String ss;
    private final String predictionTime;
    private final double temperature;
    private final double precipitation;
    private final double wind;
    private final int humidity;
    private final int clouds;
    private final Location location;
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
}
