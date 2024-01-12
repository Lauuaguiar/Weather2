package org.example.control;
import org.example.model.Location;
import org.example.model.Weather;
public interface WeatherStore {
    void save(Weather weather, Location location, String instant);
}