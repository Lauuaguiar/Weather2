package org.example;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQSubscriber {

    public void subscribeToTopic(String brokerURL) throws JMSException {
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
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("Mensaje recibido: " + textMessage.getText());
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        // Mantener el programa en ejecución
        System.out.println("Esperando mensajes. Presiona Ctrl + C para salir.");
        while (true) {
            // Mantener la aplicación esperando mensajes
        }
    }
}