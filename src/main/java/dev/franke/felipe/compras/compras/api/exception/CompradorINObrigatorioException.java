package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "A requisicao e obrigatoria")
public class CompradorINObrigatorioException extends RuntimeException {
    public CompradorINObrigatorioException(String message) {
        super(message);
    }
}
