package com.fipe.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class MarcaPublisher {

    private static final Logger LOG = Logger.getLogger(MarcaPublisher.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String QUEUE_NAME = "fipe-marcas-queue";

    @ConfigProperty(name = "rabbitmq-host")
    String host;

    @ConfigProperty(name = "rabbitmq-port")
    int port;

    @ConfigProperty(name = "rabbitmq-username")
    String username;

    @ConfigProperty(name = "rabbitmq-password")
    String password;

    private Connection connection;
    private Channel channel;

    @PostConstruct
    void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);

            connection = factory.newConnection();
            channel = connection.createChannel();

            // Declara a fila (cria se não existir)
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            LOG.info("RabbitMQ connection established successfully");
        } catch (Exception e) {
            LOG.error("Failed to connect to RabbitMQ", e);
            throw new RuntimeException(e);
        }
    }

    public void publicarMarca(String codigo, String nome) {
        try {
            MarcaMessage msg = new MarcaMessage(codigo, nome);
            String json = mapper.writeValueAsString(msg);

            // Publica DIRETO na fila (sem exchange)
            channel.basicPublish("", QUEUE_NAME, null, json.getBytes(StandardCharsets.UTF_8));

            LOG.infof("Marca publicada na fila: %s - %s", codigo, nome);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao publicar marca na fila: %s", nome);
            throw new RuntimeException("Falha ao enviar mensagem para fila", e);
        }
    }

    @PreDestroy
    void cleanup() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            LOG.error("Error closing RabbitMQ connection", e);
        }
    }

    public record MarcaMessage(String codigo, String nome) {}
}