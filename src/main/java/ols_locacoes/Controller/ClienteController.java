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

    public ClienteController(
            ClienteService clienteService
    ) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(
                clienteService.listarTodos()
        );
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(
            @RequestBody Cliente cliente
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.cadastrar(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                clienteService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Long id,
            @RequestBody Cliente dadosAtualizados
    ) {
        return ResponseEntity.ok(
                clienteService.atualizar(
                        id,
                        dadosAtualizados
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {
        clienteService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}