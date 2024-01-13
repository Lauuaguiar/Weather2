package org.example.control;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        try {
            String locations = "Activity Provider\\src\\main\\resources\\locations.csv";
            POIController poiController = new POIController(new OpenTripMapSupplier(args[0]), new JmsPOIPublisher());

            Timer timer = new Timer();
            long period = 6 * 60 * 60 * 1000;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        poiController.processPOIFile(locations);
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