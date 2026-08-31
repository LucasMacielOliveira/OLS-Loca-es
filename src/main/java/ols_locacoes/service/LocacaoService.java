package ols_locacoes.service;

import ols_locacoes.dto.LocacaoResponse;
import ols_locacoes.dto.ItemLocacaoRequest;
import ols_locacoes.dto.LocacaoRequest;
import ols_locacoes.model.Cliente;
import ols_locacoes.model.Locacao;
import ols_locacoes.model.Produto;
import ols_locacoes.model.StatusLocacao;
import ols_locacoes.repository.ClienteRepository;
import ols_locacoes.repository.ItemLocacaoRepository;
import ols_locacoes.repository.LocacaoRepository;
import ols_locacoes.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class LocacaoService {
    private final LocacaoRepository locacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemLocacaoRepository itemLocacaoRepository;

    public LocacaoService(
            LocacaoRepository locacaoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository,
            ItemLocacaoRepository itemLocacaoRepository){

        this.locacaoRepository = locacaoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.itemLocacaoRepository = itemLocacaoRepository;
    }
    @Transactional
    public LocacaoResponse cadastrar(LocacaoRequest request) {
        validarRequisicao(request);

        Cliente cliente = clienteRepository
                .findById(request.getClienteId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cliente não encontrado"
                        )
                );

        Locacao locacao = new Locacao(
                cliente,
                request.getDataPrevistaDevolucao()
        );

        Set<Long> produtosAdicionados = new HashSet<>();

        for (ItemLocacaoRequest itemRequest : request.getItens()) {
            validarItem(itemRequest);

            Long produtoId = itemRequest.getProdutoId();

            if (!produtosAdicionados.add(produtoId)) {
                throw new IllegalArgumentException(
                        "O mesmo produto não pode aparecer duas vezes"
                );
            }

            Produto produto = produtoRepository
                    .findById(produtoId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Produto não encontrado: " + produtoId
                            )
                    );

            Long quantidadeAlugada = itemLocacaoRepository
                    .somarQuantidadePorProdutoEStatus(
                            produtoId,
                            StatusLocacao.ATIVA
                    );

            int quantidadeDisponivel =
                    produto.getQuantidadeTotal()
                            - quantidadeAlugada.intValue();

            if (itemRequest.getQuantidade()
                    > quantidadeDisponivel) {

                throw new IllegalArgumentException(
                        "Estoque insuficiente para o produto "
                                + produto.getNome()
                                + ". Disponível: "
                                + quantidadeDisponivel
                );
            }

            locacao.adicionarItem(
                    produto,
                    itemRequest.getQuantidade()
            );
        }

        Locacao locacaoSalva =
                locacaoRepository.save(locacao);

        return LocacaoResponse.fromEntity(locacaoSalva);
    }

    private void validarRequisicao(LocacaoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Os dados da locação são obrigatórios"
            );
        }

        if (request.getClienteId() == null) {
            throw new IllegalArgumentException(
                    "O cliente é obrigatório"
            );
        }

        if (request.getDataPrevistaDevolucao() == null) {
            throw new IllegalArgumentException(
                    "A data prevista é obrigatória"
            );
        }

        if (request.getDataPrevistaDevolucao()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "A data prevista não pode estar no passado"
            );
        }

        if (request.getItens() == null
                || request.getItens().isEmpty()) {

            throw new IllegalArgumentException(
                    "A locação deve possuir pelo menos um item"
            );
        }
    }

    private void validarItem(ItemLocacaoRequest item) {
        if (item == null || item.getProdutoId() == null) {
            throw new IllegalArgumentException(
                    "O produto é obrigatório"
            );
        }

        if (item.getQuantidade() == null
                || item.getQuantidade() <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero"
            );
        }
    }
}


