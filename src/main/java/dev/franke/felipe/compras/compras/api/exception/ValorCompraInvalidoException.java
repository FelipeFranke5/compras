package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Valor invalido. Deve ser positivo e menor que o total de compras")
public class ValorCompraInvalidoException extends RuntimeException {
    public ValorCompraInvalidoException(String message) {
        super(message);
    }
}
