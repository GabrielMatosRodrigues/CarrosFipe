package com.fipe.infrastructure.messaging;

import com.fipe.application.usecases.ProcessaMarcaUseCase;
import io.vertx.core.json.JsonObject;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MarcaConsumer {

    private static final Logger LOG = Logger.getLogger(MarcaConsumer.class);

    @Inject
    ProcessaMarcaUseCase processaMarcaUseCase;

    @Incoming("marcas-in")
    @Blocking
    public void consumirMarca(JsonObject json) {
        try {
            String codigo = json.getString("codigo");
            String nome = json.getString("nome");

            LOG.infof("========================================");
            LOG.infof("MENSAGEM RECEBIDA: %s (%s)", nome, codigo);
            LOG.infof("========================================");

            processaMarcaUseCase.executar(codigo, nome);

            LOG.infof("Marca %s processada com sucesso!", nome);

        } catch (Exception e) {
            LOG.errorf(e, "ERRO ao processar mensagem");
            throw new RuntimeException(e);
        }
    }

    public record MarcaMessage(String codigo, String nome) {}
}