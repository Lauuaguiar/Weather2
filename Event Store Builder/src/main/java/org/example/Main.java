package org.example;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.BlockingQueue;


public class Main {
    public static void main(String[] args) {
        String brokerURL = "tcp://localhost:61616"; // Reemplaza con la URL correcta

        ActiveMQSubscriber subscriber = new ActiveMQSubscriber();
        try {
            BlockingQueue<String> eventsQueue = subscriber.subscribeToTopic(brokerURL);

            EventProcessor eventProcessor = new EventProcessor();

            // Simulando el procesamiento de eventos de la cola
            while (true) {
                String eventInfo = eventsQueue.take(); // Toma un evento de la cola
                eventProcessor.processEvent(eventInfo);
            }

        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    public static void main(String[] args) {
        String brokerURL = "tcp://localhost:61616"; // Reemplaza con la URL correcta
        ActiveMQSubscriber subscriber = new ActiveMQSubscriber();

        try {
            subscriber.subscribeToTopic(brokerURL);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

     */
}
