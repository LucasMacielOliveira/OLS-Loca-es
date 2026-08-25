package ols_locacoes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ols_locacoes.model.Produto;
import ols_locacoes.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService; // indica que o controller precisa utilizar os serviços de ProdutoService.

    public ProdutoController(ProdutoService produtoService) { //o Spring entrega automaticamente a instância de ProdutoService.
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(
            @RequestBody Produto produto
    ) {

        Produto produtoCadastrado = produtoService.cadastrarProduto(produto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoCadastrado);
    }
}