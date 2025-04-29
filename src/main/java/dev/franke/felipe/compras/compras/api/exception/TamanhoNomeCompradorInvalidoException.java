package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.UNPROCESSABLE_ENTITY,
        reason = "O tamanho do nome do comprador deve ser entre 5 e 30 caracteres")
public class TamanhoNomeCompradorInvalidoException extends RuntimeException {
    public TamanhoNomeCompradorInvalidoException(String message) {
        super(message);
    }
}
