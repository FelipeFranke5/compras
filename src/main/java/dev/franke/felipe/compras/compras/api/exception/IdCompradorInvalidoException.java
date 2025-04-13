package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "O ID deve estar no formato UUID")
public class IdCompradorInvalidoException extends RuntimeException {
    public IdCompradorInvalidoException(String message) {
        super(message);
    }
}
