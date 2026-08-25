package ols_locacoes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ols_locacoes.model.Produto;

@Service
public class ProdutoService {

    private final List<Produto> produtos = new ArrayList<>();

    public List<Produto> listarProdutos() {
        return produtos;
    }

    public Produto cadastrarProduto(Produto produto) {
        produtos.add(produto);
        return produto;
    }
}