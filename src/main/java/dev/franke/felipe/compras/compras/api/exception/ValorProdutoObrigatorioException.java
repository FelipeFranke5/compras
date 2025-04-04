package dev.franke.felipe.compras.compras.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "O preco e obrigatório")
public class ValorProdutoObrigatorioException extends RuntimeException {
    public ValorProdutoObrigatorioException(String message) {
        super(message);
    }
}
