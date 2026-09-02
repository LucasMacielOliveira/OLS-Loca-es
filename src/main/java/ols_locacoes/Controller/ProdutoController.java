package ols_locacoes.Controller;

import ols_locacoes.dto.ProdutoResponse;
import ols_locacoes.model.Produto;
import ols_locacoes.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(
            ProdutoService produtoService
    ) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProdutoPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                produtoService.buscarProdutoPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrarProduto(
            @RequestBody Produto produto
    ) {
        ProdutoResponse produtoCadastrado =
                produtoService.cadastrarProduto(produto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoCadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(
            @PathVariable Long id,
            @RequestBody Produto dadosAtualizados
    ) {
        return ResponseEntity.ok(
                produtoService.atualizarProduto(
                        id,
                        dadosAtualizados
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(
            @PathVariable Long id
    ) {
        produtoService.excluirProduto(id);

        return ResponseEntity.noContent().build();
    }
}