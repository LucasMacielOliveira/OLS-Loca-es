package ols_locacoes.repository;

import ols_locacoes.model.ItemLocacao;
import ols_locacoes.model.StatusLocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemLocacaoRepository
        extends JpaRepository<ItemLocacao, Long> {

    boolean existsByProduto_Id(Long produtoId);
    @Query("""
            SELECT COALESCE(SUM(item.quantidade), 0)
            FROM ItemLocacao item
            WHERE item.produto.id = :produtoId
              AND item.locacao.status = :status
            """)
    Long somarQuantidadePorProdutoEStatus(
            @Param("produtoId") Long produtoId,
            @Param("status") StatusLocacao status
    );
}