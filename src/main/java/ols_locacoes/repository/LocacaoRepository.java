package ols_locacoes.repository;

import ols_locacoes.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocacaoRepository
        extends JpaRepository<Locacao, Long> {
}