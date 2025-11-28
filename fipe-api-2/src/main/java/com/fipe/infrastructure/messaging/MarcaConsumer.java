package com.fipe.infrastructure.messaging;

import com.fipe.application.usecases.ProcessaMarcaUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class MarcaConsumer {

    private static final Logger LOG = Logger.getLogger(MarcaConsumer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    ProcessaMarcaUseCase processaMarcaUseCase;

    @Incoming("marcas-in")
    @Blocking
    public void consumirMarca(byte[] payload) {
        try {
            String json = new String(payload, StandardCharsets.UTF_8);

            LOG.infof("========================================");
            LOG.infof("MENSAGEM RECEBIDA: %s", json);
            LOG.infof("========================================");

            MarcaMessage message = mapper.readValue(json, MarcaMessage.class);

            LOG.infof("Processando marca: %s (%s)", message.nome(), message.codigo());

            processaMarcaUseCase.executar(message.codigo(), message.nome());

            LOG.infof("Marca %s processada com sucesso!", message.nome());

        } catch (Exception e) {
            LOG.errorf(e, "ERRO ao processar mensagem");
            throw new RuntimeException(e);
        }
    }

    public record MarcaMessage(String codigo, String nome) {}
}