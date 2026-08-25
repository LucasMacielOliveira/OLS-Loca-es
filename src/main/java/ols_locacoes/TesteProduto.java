package ols_locacoes;

import ols_locacoes.model.Produto;

public class TesteProduto {

    public static void main(String[] args) {

        Produto furadeira = new Produto(
                "Furadeira Bosch",
                "Ferramentas elétricas",
                5
        );

        System.out.println("Nome original: " + furadeira.getNome());

        furadeira.setNome("Furadeira Bosch Profissional");
        furadeira.setQuantidadeTotal(8);

        System.out.println();
        furadeira.exibirInformacoes();
    }
}