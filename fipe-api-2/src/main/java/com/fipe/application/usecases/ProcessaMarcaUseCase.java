package com.fipe.application.usecases;

import com.fipe.domain.entities.Veiculo;
import com.fipe.infrastructure.http.FipeClient;
import com.fipe.infrastructure.persistence.VeiculoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ProcessaMarcaUseCase {

    private static final Logger LOG = Logger.getLogger(ProcessaMarcaUseCase.class);

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Transactional
    public void executar(String codigoMarca, String nomeMarca) {
        LOG.infof("Processando marca: %s (%s)", nomeMarca, codigoMarca);

        try {
            // Busca os modelos da marca na API FIPE
            FipeClient.ModelosResponse response = fipeClient.buscarModelos(codigoMarca);
            List<FipeClient.ModeloDTO> modelos = response.modelos();

            LOG.infof("Encontrados %d modelos para marca %s", modelos.size(), nomeMarca);

            int salvos = 0;
            int duplicados = 0;

            // Salva cada modelo no banco
            for (FipeClient.ModeloDTO modelo : modelos) {
                String codigoVeiculo = codigoMarca + "-" + modelo.codigo();

                // Verifica se já existe
                if (veiculoRepository.existePorCodigo(codigoVeiculo)) {
                    duplicados++;
                    continue;
                }

                // Cria e salva o veículo
                Veiculo veiculo = new Veiculo(codigoVeiculo, nomeMarca, modelo.nome());
                veiculoRepository.persist(veiculo);
                salvos++;
            }

            LOG.infof("Marca %s processada: %d salvos, %d duplicados",
                    nomeMarca, salvos, duplicados);

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao processar marca %s", nomeMarca);
            throw e;
        }
    }
}