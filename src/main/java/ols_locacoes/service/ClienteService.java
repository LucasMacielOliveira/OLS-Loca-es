package ols_locacoes.service;

import ols_locacoes.model.Cliente;
import ols_locacoes.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente Cadastrar (Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }


    public Cliente Atualizar (Long id, Cliente dadosAtualizados){

        Cliente clienteExistente = buscarPorId(id);

        if (clienteExistente == null) {
            return null;
        }

        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setCpf(dadosAtualizados.getCpf());
        clienteExistente.setEndereco(dadosAtualizados.getEndereco());

        return clienteRepository.save(clienteExistente);
    }

    public boolean excluir (Long id) {
        Cliente clienteExistente = buscarPorId(id);

        if (clienteExistente == null) {
            return false;
        }

        clienteRepository.delete(clienteExistente);
        return true;
    }

}
