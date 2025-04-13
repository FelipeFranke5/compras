package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Comprador nao encontrado")
public class CompradorNaoEncontradoException extends RuntimeException {
    public CompradorNaoEncontradoException(String message) {
        super(message);
    }
}
