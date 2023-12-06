package org.example.control;

import jakarta.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActiveMQTopicSubscriber implements TopicSubscriber {
    @Override
    public BlockingQueue<String> subscribeToTopic(String brokerURL) throws JMSException {
        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
        Connection connection = createConnection(brokerURL);
        MessageConsumer subscriber = createSubscriber(connection);
        startListening(subscriber, messageQueue);
        return messageQueue;
    }


    private Connection createConnection(String brokerURL) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    private MessageConsumer createSubscriber(Connection connection) throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("prediction.Weather");
        return session.createConsumer(destination);
    }

    private void startListening(MessageConsumer subscriber, BlockingQueue<String> messageQueue) throws JMSException {
        subscriber.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                processTextMessage((TextMessage) message, messageQueue);
            }
        });
        System.out.println("Esperando mensajes. Presiona Ctrl + C para salir.");
    }

    private void processTextMessage(TextMessage message, BlockingQueue<String> messageQueue) {
        try {
            String receivedMessage = message.getText();
            System.out.println("Mensaje recibido");
            messageQueue.offer(receivedMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}