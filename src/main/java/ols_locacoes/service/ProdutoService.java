package ols_locacoes.service;

import ols_locacoes.dto.ProdutoResponse;
import ols_locacoes.exception.RegraNegocioException;
import ols_locacoes.exception.RecursoNaoEncontradoException;
import ols_locacoes.model.Produto;
import ols_locacoes.model.StatusLocacao;
import ols_locacoes.repository.ItemLocacaoRepository;
import ols_locacoes.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ItemLocacaoRepository itemLocacaoRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            ItemLocacaoRepository itemLocacaoRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.itemLocacaoRepository = itemLocacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarProdutos() {
        return produtoRepository
                .findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional
    public ProdutoResponse cadastrarProduto(Produto produto) {
        validarDados(produto);

        produto.setId(null);

        Produto produtoSalvo =
                produtoRepository.save(produto);

        return converterParaResponse(produtoSalvo);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarProdutoPorId(Long id) {
        return converterParaResponse(
                buscarEntidadePorId(id)
        );
    }

    @Transactional
    public ProdutoResponse atualizarProduto(
            Long id,
            Produto dadosAtualizados
    ) {
        validarDados(dadosAtualizados);

        Produto produtoExistente =
                buscarEntidadePorId(id);

        int quantidadeAlugada =
                calcularQuantidadeAlugada(id);

        if (dadosAtualizados.getQuantidadeTotal()
                < quantidadeAlugada) {

            throw new RegraNegocioException(
                    "A quantidade total não pode ser menor "
                            + "que a quantidade alugada: "
                            + quantidadeAlugada
            );
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

        Produto produtoSalvo =
                produtoRepository.save(produtoExistente);

        return converterParaResponse(produtoSalvo);
    }

    @Transactional
    public void excluirProduto(Long id) {
        Produto produto = buscarEntidadePorId(id);

        if (itemLocacaoRepository.existsByProduto_Id(id)) {
            throw new RegraNegocioException(
                    "O produto não pode ser excluído "
                            + "porque possui histórico de locações"
            );
        }

        produtoRepository.delete(produto);
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Produto não encontrado: " + id
                        )
                );
    }

    private ProdutoResponse converterParaResponse(
            Produto produto
    ) {
        int quantidadeAlugada =
                calcularQuantidadeAlugada(produto.getId());

        int quantidadeDisponivel =
                produto.getQuantidadeTotal()
                        - quantidadeAlugada;

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getQuantidadeTotal(),
                quantidadeDisponivel
        );
    }

    private int calcularQuantidadeAlugada(
            Long produtoId
    ) {
        Long quantidade = itemLocacaoRepository
                .somarQuantidadePorProdutoEStatus(
                        produtoId,
                        StatusLocacao.ATIVA
                );

        return quantidade == null
                ? 0
                : quantidade.intValue();
    }

    private void validarDados(Produto produto) {
        if (produto == null) {
            throw new RegraNegocioException(
                    "Os dados do produto são obrigatórios"
            );
        }

        if (produto.getNome() == null
                || produto.getNome().isBlank()) {

            throw new RegraNegocioException(
                    "O nome do produto é obrigatório"
            );
        }

        if (produto.getCategoria() == null
                || produto.getCategoria().isBlank()) {

            throw new RegraNegocioException(
                    "A categoria do produto é obrigatória"
            );
        }
    }
}