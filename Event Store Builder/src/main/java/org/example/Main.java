package org.example;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;



public class Main {

    public static void main(String[] args) {
        String brokerURL = "tcp://localhost:61616"; // Reemplaza con la URL correcta
        ActiveMQSubscriber subscriber = new ActiveMQSubscriber();

        try {
            subscriber.subscribeToTopic(brokerURL);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
