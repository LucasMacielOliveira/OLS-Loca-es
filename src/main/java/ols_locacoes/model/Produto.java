package ols_locacoes.model;

public class Produto {

    private String nome;
    private String categoria;
    private int QuantidadeTotal;

    public Produto(String nome, String categoria, int QuantidadeTotal) {
        this.nome = nome;
        this.categoria = categoria;
        this.QuantidadeTotal = QuantidadeTotal;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getQuantidadeTotal (){
        return  QuantidadeTotal;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public void setQuantidadeTotal (int QuantidadeTotal){

        if (getQuantidadeTotal() < 0 ){
            throw new IllegalArgumentException(
                    "A quantidade é inválida"
            );
        }

        this.QuantidadeTotal = QuantidadeTotal;
    }

    public void exibirInformacoes() {
        System.out.println("Produto: " + nome);
        System.out.println("Categoria: " + categoria);
        System.out.println("Quantidade em estoque: " + QuantidadeTotal);
    }
}


