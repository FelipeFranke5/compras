package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "O preco deve ser numerico e nao pode ser zero ou negativo")
public class QueryPrecoInvalidoException extends RuntimeException {
    public QueryPrecoInvalidoException(String message) {
        super(message);
    }
}
