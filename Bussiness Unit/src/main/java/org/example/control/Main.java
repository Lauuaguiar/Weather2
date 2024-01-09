package org.example.control;
import jakarta.jms.JMSException;
import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) throws JMSException {
        String brokerURL = "tcp://localhost:61616";
        List<String> topics = Arrays.asList("prediction.Weather", "points.Of.Interest");
        String directoryPath = "C:\\Users\\laura\\Downloads\\WeatherApp Entrega 3\\datalake\\eventstore";
                Controller controller = new Controller();
                controller.setTopics(topics);
                controller.setBrokerURL(brokerURL);
                controller.setDirectoryPath(directoryPath);
                controller.processMessages();
        SqlitePOIStore poiStore = new SqlitePOIStore();
        SqliteWeatherStore weatherStore = new SqliteWeatherStore();
        UserInteractive userInteractive = new UserInteractive(poiStore, weatherStore);
        userInteractive.startInteraction();
            }
}