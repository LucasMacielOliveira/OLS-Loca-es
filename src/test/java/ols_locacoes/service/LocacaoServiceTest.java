//Esse teste verifica se o LocacaoService aplica corretamente as regras principais, sem precisar acessar o PostgreSQL.
//
//Ele simula cinco situações:
//
//Há estoque disponível → cria a locação.
//Estoque insuficiente → impede a locação.
//Produto repetido → impede o cadastro.
//Primeira devolução → muda o status para DEVOLVIDA.
//Segunda devolução → gera erro.
//
//O Mockito cria repositories falsos com @Mock. O when() define respostas simuladas, o assertEquals() confere resultados e o assertThrows() confirma que uma regra bloqueou uma operação inválida.
//
//Resumindo: o teste comprova automaticamente que o coração da lógica de locações está funcionando.

package ols_locacoes.service;


import ols_locacoes.dto.ItemLocacaoRequest;
import ols_locacoes.dto.LocacaoRequest;
import ols_locacoes.dto.LocacaoResponse;
import ols_locacoes.exception.RegraNegocioException;
import ols_locacoes.model.Cliente;
import ols_locacoes.model.Locacao;
import ols_locacoes.model.Produto;
import ols_locacoes.model.StatusLocacao;
import ols_locacoes.repository.ClienteRepository;
import ols_locacoes.repository.ItemLocacaoRepository;
import ols_locacoes.repository.LocacaoRepository;
import ols_locacoes.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocacaoServiceTest {

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemLocacaoRepository itemLocacaoRepository;

    @InjectMocks
    private LocacaoService locacaoService;

    @Test
    void deveCadastrarLocacaoComEstoqueDisponivel() {
        Cliente cliente = criarCliente();
        Produto produto = criarProduto();
        LocacaoRequest request = criarRequest(2);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(itemLocacaoRepository
                .somarQuantidadePorProdutoEStatus(
                        1L,
                        StatusLocacao.ATIVA
                ))
                .thenReturn(1L);

        when(locacaoRepository.save(any(Locacao.class)))
                .thenAnswer(invocacao ->
                        invocacao.getArgument(0)
                );

        LocacaoResponse resposta =
                locacaoService.cadastrar(request);

        assertEquals(
                StatusLocacao.ATIVA,
                resposta.status()
        );

        assertEquals(1, resposta.itens().size());
        assertEquals(2, resposta.itens().get(0).quantidade());
        assertEquals(1L, resposta.itens().get(0).produtoId());

        verify(locacaoRepository)
                .save(any(Locacao.class));
    }

    @Test
    void naoDeveCadastrarQuandoEstoqueForInsuficiente() {
        Cliente cliente = criarCliente();
        Produto produto = criarProduto();
        LocacaoRequest request = criarRequest(4);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(itemLocacaoRepository
                .somarQuantidadePorProdutoEStatus(
                        1L,
                        StatusLocacao.ATIVA
                ))
                .thenReturn(2L);

        assertThrows(
                RegraNegocioException.class,
                () -> locacaoService.cadastrar(request)
        );

        verify(locacaoRepository, never())
                .save(any(Locacao.class));
    }

    @Test
    void naoDeveAceitarProdutoDuplicado() {
        Cliente cliente = criarCliente();
        Produto produto = criarProduto();
        LocacaoRequest request = criarRequest(1);

        request.setItens(List.of(
                criarItem(1L, 1),
                criarItem(1L, 1)
        ));

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(itemLocacaoRepository
                .somarQuantidadePorProdutoEStatus(
                        1L,
                        StatusLocacao.ATIVA
                ))
                .thenReturn(0L);

        assertThrows(
                RegraNegocioException.class,
                () -> locacaoService.cadastrar(request)
        );

        verify(locacaoRepository, never())
                .save(any(Locacao.class));
    }

    @Test
    void deveRegistrarDevolucao() {
        Locacao locacao = new Locacao(
                criarCliente(),
                LocalDate.now().plusDays(5)
        );

        when(locacaoRepository.findById(1L))
                .thenReturn(Optional.of(locacao));

        when(locacaoRepository.save(locacao))
                .thenReturn(locacao);

        LocacaoResponse resposta =
                locacaoService.registrarDevolucao(1L);

        assertEquals(
                StatusLocacao.DEVOLVIDA,
                resposta.status()
        );

        assertEquals(
                LocalDate.now(),
                resposta.dataDevolucao()
        );

        verify(locacaoRepository).save(locacao);
    }

    @Test
    void naoDeveRegistrarDevolucaoDuasVezes() {
        Locacao locacao = new Locacao(
                criarCliente(),
                LocalDate.now().plusDays(5)
        );

        locacao.registrarDevolucao();

        when(locacaoRepository.findById(1L))
                .thenReturn(Optional.of(locacao));

        assertThrows(
                RegraNegocioException.class,
                () -> locacaoService.registrarDevolucao(1L)
        );

        verify(locacaoRepository, never())
                .save(any(Locacao.class));
    }

    private LocacaoRequest criarRequest(
            Integer quantidade
    ) {
        LocacaoRequest request = new LocacaoRequest();

        request.setClienteId(1L);
        request.setDataPrevistaDevolucao(
                LocalDate.now().plusDays(5)
        );

        request.setItens(List.of(
                criarItem(1L, quantidade)
        ));

        return request;
    }

    private ItemLocacaoRequest criarItem(
            Long produtoId,
            Integer quantidade
    ) {
        ItemLocacaoRequest item =
                new ItemLocacaoRequest();

        item.setProdutoId(produtoId);
        item.setQuantidade(quantidade);

        return item;
    }

    private Cliente criarCliente() {
        return new Cliente(
                "Maria Oliveira",
                "12345678901",
                "Rua das Flores, 100"
        );
    }

    private Produto criarProduto() {
        Produto produto = new Produto(
                "Furadeira Bosch",
                "Ferramentas elétricas",
                5
        );

        produto.setId(1L);

        return produto;
    }
}