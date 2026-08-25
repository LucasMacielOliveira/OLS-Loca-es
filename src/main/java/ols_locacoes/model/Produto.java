package ols_locacoes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "quantidade_total", nullable = false)
    private int quantidadeTotal;

    public Produto() {
    }

    public Produto(
            String nome,
            String categoria,
            int quantidadeTotal
    ) {

        this.nome = nome;
        this.categoria = categoria;

        setQuantidadeTotal(quantidadeTotal);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {

        if (quantidadeTotal < 0) {
            throw new IllegalArgumentException(
                    "A quantidade em estoque não pode ser negativa."
            );
        }

        this.quantidadeTotal = quantidadeTotal;
    }

    public void exibirInformacoes() {

        System.out.println("ID: " + id);
        System.out.println("Produto: " + nome);
        System.out.println("Categoria: " + categoria);
        System.out.println(
                "Quantidade em estoque: " + quantidadeTotal
        );
    }
}