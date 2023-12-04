package org.example.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.model.Weather;

import java.time.Instant;


public class WeatherDataPublisher implements Publisher {
    // Método para obtener datos de OpenWeatherMap y enviar al broker
    public void publishWeatherData(Weather weather) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantAdapter());
        Gson gson = gsonBuilder.create();

        String jsonData = gson.toJson(weather);

        // Publish the message to the broker
        sendMessageToBroker(jsonData);
        System.out.println("Message sent to the Broker");
    }

    private void sendMessageToBroker(String jsonData) {
        try {
            // Establish connection with the broker (ActiveMQ)
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            System.out.println("Connection established");

            // Create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Session created");

            // Create a producer for the topic 'prediction.Weather'
            Destination destination = session.createTopic("prediction.Weather");
            MessageProducer producer = session.createProducer(destination);
            System.out.println("Producer created");

            // Create a message with JSON data
            TextMessage message = session.createTextMessage(jsonData);
            System.out.println("JSON Message created");

            // Send the message to the broker's topic
            producer.send(message);
            System.out.println("Message sent to the topic");

            // Close connections
            producer.close();
            session.close();
            connection.close();
            System.out.println("Connection closed");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}