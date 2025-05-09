package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "A requisicao nao e valida")
public class ProdutoRequisicaoInvalidaException extends RuntimeException {
    public ProdutoRequisicaoInvalidaException(String message) {
        super(message);
    }
}
