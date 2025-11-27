package com.fipe.application.usecases;

import com.fipe.domain.entities.Veiculo;
import com.fipe.infrastructure.persistence.VeiculoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.util.List;

public class ConsultaVeiculosUseCase {

    private static final Logger LOG = Logger.getLogger(ConsultaVeiculosUseCase.class);

    @Inject
    VeiculoRepository veiculoRepository;

    public List<String> buscarMarcas() {
        LOG.info("Buscando marcas cadastradas");
        return veiculoRepository.buscarMarcasDistintas();
    }

    public List<VeiculoDTO> buscarVeiculosPorMarca(String marca) {
        LOG.infof("Buscando veículos da marca: %s", marca);

        List<Veiculo> veiculos = veiculoRepository.buscarPorMarca(marca);

        return veiculos.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private VeiculoDTO converterParaDTO(Veiculo veiculo) {
        return new VeiculoDTO(
                veiculo.getId(),
                veiculo.getCodigo(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getObservacoes()
        );
    }

    public record VeiculoDTO(
            Long id,
            String codigo,
            String marca,
            String modelo,
            String observacoes
    ) {}
}