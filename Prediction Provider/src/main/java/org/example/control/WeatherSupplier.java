package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

import java.util.List;

public interface WeatherSupplier {
    List<Weather> getWeather(Location location);
}
