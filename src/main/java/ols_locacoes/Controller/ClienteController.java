package ols_locacoes.Controller;

import ols_locacoes.model.Cliente;
import ols_locacoes.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        Cliente clienteCadastrado = clienteService.cadastrar(cliente);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCadastrado);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity <Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dadosAtualizados) {
        Cliente clienteAtualizado = clienteService.atualizar(id, dadosAtualizados);

        if (clienteAtualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}") // mapeia a requisição DELETE para o endpoint /clientes/{id}, onde excluirá o cliente pelo ID
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean clienteExcluido = clienteService.excluir(id);

        if (!clienteExcluido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}