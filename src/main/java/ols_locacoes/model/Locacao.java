package ols_locacoes.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locacoes")
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "data_retirada", nullable = false)
    private LocalDate dataRetirada;

    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLocacao status;

    @OneToMany(
            mappedBy = "locacao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemLocacao> itens = new ArrayList<>();

    public Locacao() {
    }

    public Locacao(
            Cliente cliente,
            LocalDate dataPrevistaDevolucao) {

        this.cliente = cliente;
        this.dataRetirada = LocalDate.now();
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.status = StatusLocacao.ATIVA;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public StatusLocacao getStatus() {
        return status;
    }

    public List<ItemLocacao> getItens() {
        return itens;
    }

    public void adicionarItem(
            Produto produto,
            Integer quantidade) {

        ItemLocacao item =
                new ItemLocacao(this, produto, quantidade);

        itens.add(item);
    }

    public void registrarDevolucao() {
        this.dataDevolucao = LocalDate.now();
        this.status = StatusLocacao.DEVOLVIDA;
    }
}