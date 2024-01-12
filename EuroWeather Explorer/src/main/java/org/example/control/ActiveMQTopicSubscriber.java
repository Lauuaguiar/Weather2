package org.example.control;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;
import java.util.List;
public class ActiveMQTopicSubscriber implements TopicSubscriber {
    private final Session session;
    public ActiveMQTopicSubscriber(String brokerURL) throws JMSException {
        Connection connection = createConnection(brokerURL);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    public List<Message> subscribe(List<String> topics) {
        List<Message> receivedMessages = new ArrayList<>();
        try {
            System.out.println("Waiting for messages...");
            for (String topic : topics) {
                MessageConsumer subscriber = createSubscriber(topic);
                subscriber.setMessageListener(message -> {
                    if (message instanceof TextMessage) {
                        receivedMessages.add(message);
                    }
                });
            }
            Thread.sleep(40000);
        } catch (InterruptedException | JMSException e) {
            System.err.println("Error in subscription: " + e.getMessage());
        }
        return receivedMessages;
    }
    private Connection createConnection(String brokerURL) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("Business-unit");
        connection.start();
        return connection;
    }
    private MessageConsumer createSubscriber(String topic) throws JMSException {
        return session.createConsumer(session.createTopic(topic));
    }
}