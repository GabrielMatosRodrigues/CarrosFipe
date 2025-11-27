package com.fipe.infrastructure.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MarcaPublisher {

    private static final Logger LOG = Logger.getLogger(MarcaPublisher.class);

    @Channel("marcas-out")
    Emitter<MarcaMessage> emitter;

    public void publicarMarca(String codigo, String nome) {
        try {
            MarcaMessage message = new MarcaMessage(codigo, nome);
            emitter.send(message);
            LOG.infof("Marca publicada na fila: %s - %s", codigo, nome);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao publicar marca na fila: %s", nome);
            throw new RuntimeException("Falha ao enviar mensagem para fila", e);
        }
    }

    public record MarcaMessage(
            String codigo,
            String nome
    ) {}
}
