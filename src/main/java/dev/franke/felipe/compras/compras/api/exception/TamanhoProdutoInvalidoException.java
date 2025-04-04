package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "O nome do produto deve conter entre 5 e 30 caracteres")
public class TamanhoProdutoInvalidoException extends RuntimeException {
    public TamanhoProdutoInvalidoException(String message) {
        super(message);
    }
}
