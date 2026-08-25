package ols_locacoes.model;

public class Produto {

    private Long id;
    private String nome;
    private String categoria;
    private int quantidadeTotal;

    // Construtor vazio utilizado na conversão do JSON para objeto.
    public Produto() {
    }

    // Construtor utilizado quando criamos um produto manualmente.
    public Produto(String nome, String categoria, int quantidadeTotal) {
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
        System.out.println("Produto: " + nome);
        System.out.println("Categoria: " + categoria);
        System.out.println("Quantidade em estoque: " + quantidadeTotal);
    }
}