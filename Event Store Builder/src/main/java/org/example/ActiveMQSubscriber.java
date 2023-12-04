package org.example;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActiveMQSubscriber {

    public BlockingQueue<String> subscribeToTopic(String brokerURL) throws JMSException {
        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("prediction.Weather");

        MessageConsumer subscriber = session.createConsumer(destination);

        subscriber.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        String receivedMessage = textMessage.getText();
                        System.out.println("Mensaje recibido");
                        messageQueue.offer(receivedMessage); // Agregar el mensaje a la cola
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        System.out.println("Esperando mensajes. Presiona Ctrl + C para salir.");
        return messageQueue;
    }

    /*
    public String subscribeToTopic(String brokerURL) throws JMSException {
        // Crear la conexión al Broker
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Crear una sesión
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Crear el destino (topic)
        Destination destination = session.createTopic("prediction.Weather");

        // Crear el suscriptor
        MessageConsumer subscriber = session.createConsumer(destination);

        // Implementar el listener para recibir mensajes
        subscriber.setMessageListener(new MessageListener() {

            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        String receivedMessage = textMessage.getText();
                        System.out.println("Mensaje recibido: " + receivedMessage);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        // Mantener el programa en ejecución
        System.out.println("Esperando mensajes. Presiona Ctrl + C para salir.");
        while (true) {
            // Mantener la aplicación esperando mensajes
        }
    }

     */

}