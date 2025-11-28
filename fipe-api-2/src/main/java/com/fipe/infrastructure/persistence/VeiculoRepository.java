package com.fipe.infrastructure.persistence;

import com.fipe.domain.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepository<Veiculo> {

    public Optional<Veiculo> buscarPorCodigo(String codigo) {
        return find("codigo", codigo).firstResultOptional();
    }

    public List<Veiculo> buscarPorMarca(String marca) {
        return find("marca", marca).list();
    }

    public List<String> buscarMarcasDistintas() {
        return getEntityManager()
                .createQuery("SELECT DISTINCT v.marca FROM Veiculo v ORDER BY v.marca", String.class)
                .getResultList();
    }

    public boolean existePorCodigo(String codigo) {
        return count("codigo", codigo) > 0;
    }
}