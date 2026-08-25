package ols_locacoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ols_locacoes.model.Produto;

public interface ProdutoRepository
        extends JpaRepository<Produto, Long> {

}