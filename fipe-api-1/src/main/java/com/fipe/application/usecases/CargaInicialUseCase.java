package com.fipe.application.usecases;

import com.fipe.infrastructure.http.FipeClient;
import com.fipe.infrastructure.messaging.MarcaPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.util.List;

@ApplicationScoped
public class CargaInicialUseCase {

    private static final Logger LOG = Logger.getLogger(CargaInicialUseCase.class);

    // Configurações de proteção
    private static final int DELAY_ENTRE_MARCAS_MS = 100;
    private static final int LOTE_TAMANHO = 10;

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    MarcaPublisher marcaPublisher;

    public int executar() {
        LOG.info("Iniciando carga inicial de marcas FIPE");

        try {
            List<FipeClient.MarcaResponse> marcas = fipeClient.buscarMarcas();
            LOG.infof("Total de marcas encontradas: %d", marcas.size());

            int contador = 0;
            for (FipeClient.MarcaResponse marca : marcas) {
                try {
                    marcaPublisher.publicarMarca(marca.codigo(), marca.nome());
                    contador++;

                    if (contador % LOTE_TAMANHO == 0) {
                        LOG.infof("Progresso: %d/%d marcas enviadas para fila",
                                contador, marcas.size());
                    }

                    // Delay para não sobrecarregar a fila
                    if (contador < marcas.size()) {
                        Thread.sleep(DELAY_ENTRE_MARCAS_MS);
                    }

                } catch (InterruptedException e) {
                    LOG.warn("Delay interrompido", e);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    LOG.errorf(e, "Erro ao publicar marca: %s", marca.nome());
                }
            }

            LOG.infof("Carga inicial concluída! %d marcas enviadas para processamento", contador);
            return contador;

        } catch (Exception e) {
            LOG.error("Erro ao executar carga inicial", e);
            throw new RuntimeException("Falha na carga inicial de dados", e);
        }
    }
}