package ols_locacoes.controller;

import ols_locacoes.dto.LocacaoRequest;
import ols_locacoes.dto.LocacaoResponse;
import ols_locacoes.service.LocacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;

    public LocacaoController(
            LocacaoService locacaoService) {

        this.locacaoService = locacaoService;
    }

    @PostMapping
    public ResponseEntity<LocacaoResponse> cadastrar(
            @RequestBody LocacaoRequest request) {

        LocacaoResponse locacaoCadastrada =
                locacaoService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locacaoCadastrada);
    }

    @GetMapping
    public ResponseEntity<List<LocacaoResponse>> listarTodas() {
        return ResponseEntity.ok(
                locacaoService.listarTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocacaoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                locacaoService.buscarPorId(id)
        );
    }

    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<LocacaoResponse> registrarDevolucao(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                locacaoService.registrarDevolucao(id)
        );
    }
}