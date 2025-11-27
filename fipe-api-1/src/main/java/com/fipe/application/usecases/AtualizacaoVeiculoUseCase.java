package com.fipe.application.usecases;

import com.fipe.domain.entities.Veiculo;
import com.fipe.infrastructure.persistence.VeiculoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AtualizacaoVeiculoUseCase {

    private static final Logger LOG = Logger.getLogger(AtualizacaoVeiculoUseCase.class);

    @Inject
    VeiculoRepository veiculoRepository;

    @Transactional
    public void atualizar(Long id, String modelo, String observacoes) {
        LOG.infof("Atualizando veículo ID: %d", id);

        Veiculo veiculo = veiculoRepository.findById(id);

        if (veiculo == null) {
            LOG.warnf("Veículo não encontrado: ID %d", id);
            throw new VeiculoNaoEncontradoException("Veículo não encontrado com ID: " + id);
        }

        veiculo.atualizar(modelo, observacoes);

        veiculoRepository.persist(veiculo);

        LOG.infof("Veículo atualizado com sucesso: %s", veiculo.getModelo());
    }

    public static class VeiculoNaoEncontradoException extends RuntimeException {
        public VeiculoNaoEncontradoException(String message) {
            super(message);
        }
    }

    public record AtualizacaoVeiculoRequest(
            String modelo,
            String observacoes
    ) {
        public void validar() {
            if (modelo == null || modelo.isBlank()) {
                throw new IllegalArgumentException("Modelo é obrigatório");
            }
        }
    }
}