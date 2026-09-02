package ols_locacoes.service;

import ols_locacoes.exception.RegraNegocioException;
import ols_locacoes.exception.RecursoNaoEncontradoException;
import ols_locacoes.model.Cliente;
import ols_locacoes.repository.ClienteRepository;
import ols_locacoes.repository.LocacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final LocacaoRepository locacaoRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            LocacaoRepository locacaoRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.locacaoRepository = locacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        validarDados(cliente);

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RegraNegocioException(
                    "Já existe um cliente com esse CPF"
            );
        }

        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Cliente não encontrado: " + id
                        )
                );
    }

    @Transactional
    public Cliente atualizar(
            Long id,
            Cliente dadosAtualizados
    ) {
        validarDados(dadosAtualizados);

        Cliente clienteExistente = buscarPorId(id);

        boolean cpfPertenceAOutroCliente =
                clienteRepository.existsByCpfAndIdNot(
                        dadosAtualizados.getCpf(),
                        id
                );

        if (cpfPertenceAOutroCliente) {
            throw new RegraNegocioException(
                    "Já existe outro cliente com esse CPF"
            );
        }

        clienteExistente.setNome(
                dadosAtualizados.getNome()
        );

        clienteExistente.setCpf(
                dadosAtualizados.getCpf()
        );

        clienteExistente.setEndereco(
                dadosAtualizados.getEndereco()
        );

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);

        if (locacaoRepository.existsByCliente_Id(id)) {
            throw new RegraNegocioException(
                    "O cliente não pode ser excluído "
                            + "porque possui histórico de locações"
            );
        }

        clienteRepository.delete(cliente);
    }

    private void validarDados(Cliente cliente) {
        if (cliente == null) {
            throw new RegraNegocioException(
                    "Os dados do cliente são obrigatórios"
            );
        }

        if (cliente.getNome() == null
                || cliente.getNome().isBlank()) {

            throw new RegraNegocioException(
                    "O nome do cliente é obrigatório"
            );
        }

        if (cliente.getCpf() == null
                || !cliente.getCpf().matches("\\d{11}")) {

            throw new RegraNegocioException(
                    "O CPF deve possuir exatamente 11 números"
            );
        }

        if (cliente.getEndereco() == null
                || cliente.getEndereco().isBlank()) {

            throw new RegraNegocioException(
                    "O endereço do cliente é obrigatório"
            );
        }
    }
}