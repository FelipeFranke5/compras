package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "O nome e obrigatorio")
public class NomeProdutoObrigatorioException extends RuntimeException {
    public NomeProdutoObrigatorioException(String message) {
        super(message);
    }
}
