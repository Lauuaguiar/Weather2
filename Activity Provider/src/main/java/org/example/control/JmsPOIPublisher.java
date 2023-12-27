package org.example.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.model.POI;


public class JmsPOIPublisher implements POIPublisher {

    public void publishPOI(POI poi) {
        String jsonData = convertPOIToJson(poi);
        sendMessageToBroker(jsonData);
    }

    private String convertPOIToJson(POI poi) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(poi);
    }

    private void sendMessageToBroker(String jsonData) {
        try {
            Connection connection = establishConnectionWithBroker();
            Session session = createSession(connection);
            MessageProducer producer = createProducerForTopic(session);
            TextMessage message = createTextMessage(session, jsonData);
            sendMessage(producer, message);
            closeConnections(producer, session, connection);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private Connection establishConnectionWithBroker() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private MessageProducer createProducerForTopic(Session session) throws JMSException {
        Destination destination = session.createTopic("points.Of.Interest");
        return session.createProducer(destination);
    }

    private TextMessage createTextMessage(Session session, String jsonData) throws JMSException {
        return session.createTextMessage(jsonData);
    }

    private void sendMessage(MessageProducer producer, TextMessage message) throws JMSException {
        producer.send(message);
    }

    private void closeConnections(MessageProducer producer, Session session, Connection connection) throws JMSException {
        producer.close();
        session.close();
        connection.close();
    }
}

