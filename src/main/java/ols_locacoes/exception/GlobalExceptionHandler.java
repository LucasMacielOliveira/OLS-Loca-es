package ols_locacoes.exception;

import jakarta.servlet.http.HttpServletRequest;
import ols_locacoes.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErroResponse erro = new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                "Recurso não encontrado",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler({
            RegraNegocioException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErroResponse> tratarRegraNegocio(
            RuntimeException exception,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErroResponse erro = new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                "Solicitação inválida",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErroResponse erro = new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                "JSON inválido",
                "Confira os campos e seus formatos",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erro);
    }
}