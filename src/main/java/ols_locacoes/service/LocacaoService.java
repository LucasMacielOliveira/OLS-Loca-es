package ols_locacoes.service;

import ols_locacoes.dto.ItemLocacaoRequest;
import ols_locacoes.dto.LocacaoRequest;
import ols_locacoes.dto.LocacaoResponse;
import ols_locacoes.exception.RegraNegocioException;
import ols_locacoes.exception.RecursoNaoEncontradoException;
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
import java.util.List;

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
            ItemLocacaoRepository itemLocacaoRepository) {

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
                        new RecursoNaoEncontradoException(
                                "Cliente não encontrado: "
                                        + request.getClienteId()
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
                throw new RegraNegocioException(
                        "O mesmo produto não pode aparecer duas vezes"
                );
            }

            Produto produto = produtoRepository
                    .findById(produtoId)
                    .orElseThrow(() ->
                            new RecursoNaoEncontradoException(
                                    "Produto não encontrado: "
                                            + produtoId
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

                throw new RegraNegocioException(
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
    private Locacao buscarEntidadePorId(Long id) {
        return locacaoRepository
                .findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Locação não encontrada: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<LocacaoResponse> listarTodas() {
        return locacaoRepository
                .findAll()
                .stream()
                .map(LocacaoResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocacaoResponse buscarPorId(Long id) {
        Locacao locacao = buscarEntidadePorId(id);

        return LocacaoResponse.fromEntity(locacao);
    }

    @Transactional
    public LocacaoResponse registrarDevolucao(Long id) {
        Locacao locacao = buscarEntidadePorId(id);

        if (locacao.getStatus() == StatusLocacao.DEVOLVIDA) {
            throw new RegraNegocioException(
                    "A locação " + id + " já foi devolvida"
            );
        }

        locacao.registrarDevolucao();

        Locacao locacaoSalva =
                locacaoRepository.save(locacao);

        return LocacaoResponse.fromEntity(locacaoSalva);
    }


    private void validarRequisicao(LocacaoRequest request) {
        if (request == null) {
            throw new RegraNegocioException(
                    "Os dados da locação são obrigatórios"
            );
        }

        if (request.getClienteId() == null) {
            throw new RegraNegocioException(
                    "O cliente é obrigatório"
            );
        }

        if (request.getDataPrevistaDevolucao() == null) {
            throw new RegraNegocioException(
                    "A data prevista é obrigatória"
            );
        }

        if (request.getDataPrevistaDevolucao()
                .isBefore(LocalDate.now())) {

            throw new RegraNegocioException(
                    "A data prevista não pode estar no passado"
            );
        }

        if (request.getItens() == null
                || request.getItens().isEmpty()) {

            throw new RegraNegocioException(
                    "A locação deve possuir pelo menos um item"
            );
        }
    }

    private void validarItem(ItemLocacaoRequest item) {
        if (item == null || item.getProdutoId() == null) {
            throw new RegraNegocioException(
                    "O produto é obrigatório"
            );
        }

        if (item.getQuantidade() == null
                || item.getQuantidade() <= 0) {

            throw new RegraNegocioException(
                    "A quantidade deve ser maior que zero"
            );
        }
    }
}