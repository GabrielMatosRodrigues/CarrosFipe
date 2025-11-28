package com.fipe.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MarcaPublisher {

    private static final Logger LOG = Logger.getLogger(MarcaPublisher.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Channel("marcas-out")
    Emitter<String> emitter;

    public void publicarMarca(String codigo, String nome) {
        try {
            MarcaMessage msg = new MarcaMessage(codigo, nome);
            String json = mapper.writeValueAsString(msg);

            OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
                    .withRoutingKey("fipe-marcas-queue")
                    .build();

            Message<String> message = Message.of(json).addMetadata(metadata);

            emitter.send(message);
            LOG.infof("Marca publicada na fila: %s - %s", codigo, nome);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao publicar marca na fila: %s", nome);
            throw new RuntimeException("Falha ao enviar mensagem para fila", e);
        }
    }

    public record MarcaMessage(String codigo, String nome) {}
}