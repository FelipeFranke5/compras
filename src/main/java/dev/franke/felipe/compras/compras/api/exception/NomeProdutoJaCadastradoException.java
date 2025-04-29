package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Produto ja cadastrado")
public class NomeProdutoJaCadastradoException extends RuntimeException {
    public NomeProdutoJaCadastradoException(String message) {
        super(message);
    }
}
