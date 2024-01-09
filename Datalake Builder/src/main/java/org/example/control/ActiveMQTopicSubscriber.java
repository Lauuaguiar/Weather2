package org.example.control;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.List;
public class ActiveMQTopicSubscriber implements TopicSubscriber {
    private final String brokerURL;
    private final Connection connection;
    private final Session session;

    public ActiveMQTopicSubscriber(String brokerURL) throws JMSException {
        this.brokerURL = brokerURL;
        connection = createConnection(brokerURL);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public void subscribe(List<String> topics, EventStore eventStore) {
        try {
            for (String topic : topics) {
                MessageConsumer subscriber = createDurableSubscriber(topic);
                subscriber.setMessageListener(message -> {
                    if (message instanceof TextMessage) {
                        try {
                            eventStore.save(((TextMessage) message).getText(), topic);
                        } catch (JMSException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                });
                System.out.println("Subscribed to topic: " + topic);
            }
            System.out.println("Esperando mensajes...");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
    private Connection createConnection(String brokerURL) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("DatalakeBuilder");
        connection.start();
        return connection;
    }
    private MessageConsumer createDurableSubscriber(String topic) throws JMSException {
        String subscriptionName = "DatalakeBuilderDurable_" + topic; // Utiliza un nombre único basado en el tema
        return session.createDurableSubscriber(session.createTopic(topic), subscriptionName);
    }
}
