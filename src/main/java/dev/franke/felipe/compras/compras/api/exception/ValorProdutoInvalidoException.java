package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "O valor informado nao e valido")
public class ValorProdutoInvalidoException extends RuntimeException {
    public ValorProdutoInvalidoException(String message) {
        super(message);
    }
}
