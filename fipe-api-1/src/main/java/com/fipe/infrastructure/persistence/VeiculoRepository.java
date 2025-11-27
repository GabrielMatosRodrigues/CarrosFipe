package com.fipe.infrastructure.persistence;

import com.fipe.domain.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepository<Veiculo> {

    public List<Veiculo> buscarPorMarca(String marca) {
        return list("marca", marca);
    }

    public Veiculo buscarPorCodigo(String codigo) {
        return find("codigo", codigo).firstResult();
    }

    public List<String> buscarMarcasDistintas() {
        return getEntityManager()
                .createQuery("SELECT DISTINCT v.marca FROM Veiculo v ORDER BY v.marca", String.class)
                .getResultList();
    }

    public boolean existePorCodigo(String codigo) {
        return count("codigo", codigo) > 0;
    }

    public void atualizar(Veiculo veiculo) {
        persist(veiculo);
    }
}
