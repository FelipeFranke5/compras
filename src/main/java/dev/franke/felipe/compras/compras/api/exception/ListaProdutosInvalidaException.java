package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Envie uma lista com os IDs")
public class ListaProdutosInvalidaException extends RuntimeException {
    public ListaProdutosInvalidaException(String message) {
        super(message);
    }
}
