package ols_locacoes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ols_locacoes.model.Produto;
import ols_locacoes.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository
    ) {

        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto cadastrarProduto(Produto produto) {

        produto.setId(null);

        return produtoRepository.save(produto);
    }

    public Produto buscarProdutoPorId(Long id) {

        return produtoRepository
                .findById(id)
                .orElse(null);
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

        return produtoRepository.save(
                produtoExistente
        );
    }

    public boolean excluirProduto(Long id) {

        if (!produtoRepository.existsById(id)) {
            return false;
        }

        produtoRepository.deleteById(id);

        return true;
    }
}