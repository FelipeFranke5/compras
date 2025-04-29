package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "O nome do comprador e obrigatorio")
public class NomeCompradorObrigatorioException extends RuntimeException {
    public NomeCompradorObrigatorioException(String message) {
        super(message);
    }
}
