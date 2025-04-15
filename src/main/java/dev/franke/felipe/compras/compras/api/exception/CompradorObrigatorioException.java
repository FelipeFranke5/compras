package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "O comprador e obrigatorio")
public class CompradorObrigatorioException extends RuntimeException {
    public CompradorObrigatorioException(String message) {
        super(message);
    }
}
