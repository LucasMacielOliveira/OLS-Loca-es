package ols_locacoes.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocacaoRequest {

    private Long clienteId;
    private LocalDate dataPrevistaDevolucao;
    private List<ItemLocacaoRequest> itens = new ArrayList<>();

    public LocacaoRequest() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(
            LocalDate dataPrevistaDevolucao) {

        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public List<ItemLocacaoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemLocacaoRequest> itens) {
        this.itens = itens;
    }
}