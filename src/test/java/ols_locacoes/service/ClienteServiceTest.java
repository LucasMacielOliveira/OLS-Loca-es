package ols_locacoes.service;

import ols_locacoes.exception.RegraNegocioException;
import ols_locacoes.exception.RecursoNaoEncontradoException;
import ols_locacoes.model.Cliente;
import ols_locacoes.repository.ClienteRepository;
import ols_locacoes.repository.LocacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private LocacaoRepository locacaoRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveCadastrarClienteValido() {
        Cliente cliente = new Cliente(
                "Maria Oliveira",
                "12345678901",
                "Rua das Flores, 100"
        );

        when(clienteRepository.existsByCpf(cliente.getCpf()))
                .thenReturn(false);

        when(clienteRepository.save(cliente))
                .thenReturn(cliente);

        Cliente resultado =
                clienteService.cadastrar(cliente);

        assertSame(cliente, resultado);

        verify(clienteRepository).save(cliente);
    }

    @Test
    void naoDeveCadastrarCpfInvalido() {
        Cliente cliente = new Cliente(
                "Maria Oliveira",
                "123",
                "Rua das Flores, 100"
        );

        assertThrows(
                RegraNegocioException.class,
                () -> clienteService.cadastrar(cliente)
        );

        verify(clienteRepository, never())
                .save(any(Cliente.class));
    }

    @Test
    void naoDeveCadastrarCpfDuplicado() {
        Cliente cliente = new Cliente(
                "Maria Oliveira",
                "12345678901",
                "Rua das Flores, 100"
        );

        when(clienteRepository.existsByCpf(cliente.getCpf()))
                .thenReturn(true);

        assertThrows(
                RegraNegocioException.class,
                () -> clienteService.cadastrar(cliente)
        );

        verify(clienteRepository, never())
                .save(any(Cliente.class));
    }

    @Test
    void naoDeveExcluirClienteComLocacao() {
        Cliente cliente = new Cliente(
                "Maria Oliveira",
                "12345678901",
                "Rua das Flores, 100"
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(locacaoRepository.existsByCliente_Id(1L))
                .thenReturn(true);

        assertThrows(
                RegraNegocioException.class,
                () -> clienteService.excluir(1L)
        );

        verify(clienteRepository, never())
                .delete(any(Cliente.class));
    }

    @Test
    void deveInformarQuandoClienteNaoExiste() {
        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> clienteService.buscarPorId(99L)
        );
    }
}