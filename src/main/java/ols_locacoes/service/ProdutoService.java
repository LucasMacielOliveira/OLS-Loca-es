package ols_locacoes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ols_locacoes.model.Produto;

@Service
public class ProdutoService {

    private final List<Produto> produtos = new ArrayList<>();

    private Long proximoId = 1L;

    public List<Produto> listarProdutos() {
        return produtos;
    }

    public Produto cadastrarProduto(Produto produto) {

        produto.setId(proximoId);

        proximoId++;

        produtos.add(produto);

        return produto;
    }

    public Produto buscarProdutoPorId(Long id) {

        for (Produto produto : produtos) {

            if (produto.getId().equals(id)) {
                return produto;
            }
        }

        return null;
    }

    public Produto atualizarProduto(
            Long id,
            Produto dadosAtualizados
    ) {

        Produto produtoExistente = buscarProdutoPorId(id);

        if (produtoExistente == null) {
            return null;
        }

        produtoExistente.setNome(
                dadosAtualizados.getNome()
        );

        produtoExistente.setCategoria(
                dadosAtualizados.getCategoria()
        );

        produtoExistente.setQuantidadeTotal(
                dadosAtualizados.getQuantidadeTotal()
        );

        return produtoExistente;
    }
}