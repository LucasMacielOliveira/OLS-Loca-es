package ols_locacoes.dto;

public record ProdutoResponse(
        Long id,
        String nome,
        String categoria,
        int quantidadeTotal,
        int quantidadeDisponivel
) {
}