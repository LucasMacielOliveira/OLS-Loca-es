package ols_locacoes.dto;

import ols_locacoes.model.ItemLocacao;

public record ItemLocacaoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade
) {

    public static ItemLocacaoResponse fromEntity(
            ItemLocacao item) {

        return new ItemLocacaoResponse(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade()
        );
    }
}