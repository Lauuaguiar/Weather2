package org.example.control;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        try {
            String locations = "Prediction Provider\\src\\main\\resources\\locations.csv";
            WeatherController weatherController = new WeatherController(new OpenWeatherMapSupplier(args[0]), new JmsWeatherPublisher());

            Timer timer = new Timer();
            long period = 6 * 60 * 60 * 1000;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        weatherController.processWeatherFile(locations);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, period);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}