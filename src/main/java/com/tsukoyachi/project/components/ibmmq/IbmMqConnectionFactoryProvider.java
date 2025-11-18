package com.tsukoyachi.project.components.ibmmq;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.jms.ConnectionFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class IbmMqConnectionFactoryProvider {

    @ConfigProperty(name = "ibmmq.host", defaultValue = "localhost")
    String host;

    @ConfigProperty(name = "ibmmq.port", defaultValue = "1414")
    int port;

    @ConfigProperty(name = "ibmmq.queueManager", defaultValue = "QM1")
    String queueManager;

    @ConfigProperty(name = "ibmmq.channel", defaultValue = "DEV.APP.SVRCONN")
    String channel;

    @ConfigProperty(name = "ibmmq.username", defaultValue = "app")
    String username;

    @ConfigProperty(name = "ibmmq.password", defaultValue = "passw0rd")
    String password;

    @Produces
    @ApplicationScoped
    @Named("ibmMQConnectionFactory")
    public ConnectionFactory createConnectionFactory() {
        try {
            MQConnectionFactory connectionFactory = new MQConnectionFactory();
            
            // Configuration de base
            connectionFactory.setHostName(host);
            connectionFactory.setPort(port);
            connectionFactory.setQueueManager(queueManager);
            connectionFactory.setChannel(channel);
            connectionFactory.setTransportType(1); // MQXPT_TCP
            
            // Configuration d'authentification
            connectionFactory.setStringProperty("XMSC_USERID", username);
            connectionFactory.setStringProperty("XMSC_PASSWORD", password);
            
            // Configuration d'authentification MQCSP
            connectionFactory.setBooleanProperty("XMSC_WMQ_USE_MQCSP_AUTHENTICATION", true);
            
            return connectionFactory;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create IBM MQ ConnectionFactory", e);
        }
    }
}