package ols_locacoes.dto;

import ols_locacoes.model.Locacao;
import ols_locacoes.model.StatusLocacao;

import java.time.LocalDate;
import java.util.List;

public record LocacaoResponse(
        Long id,
        Long clienteId,
        String nomeCliente,
        LocalDate dataRetirada,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao,
        StatusLocacao status,
        List<ItemLocacaoResponse> itens
) {

    public static LocacaoResponse fromEntity(
            Locacao locacao) {

        List<ItemLocacaoResponse> itens =
                locacao.getItens()
                        .stream()
                        .map(ItemLocacaoResponse::fromEntity)
                        .toList();

        return new LocacaoResponse(
                locacao.getId(),
                locacao.getCliente().getId(),
                locacao.getCliente().getNome(),
                locacao.getDataRetirada(),
                locacao.getDataPrevistaDevolucao(),
                locacao.getDataDevolucao(),
                locacao.getStatus(),
                itens
        );
    }
}