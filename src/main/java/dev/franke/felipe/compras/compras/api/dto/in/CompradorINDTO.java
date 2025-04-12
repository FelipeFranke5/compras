package dev.franke.felipe.compras.compras.api.dto.in;

import dev.franke.felipe.compras.compras.api.exception.NomeProdutoObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.TamanhoProdutoInvalidoException;
import lombok.Data;

@Data
public class CompradorINDTO {

    private String nome;

    private void validaNome() {
        if (this.getNome() == null || this.getNome().isBlank()) {
            throw new NomeProdutoObrigatorioException(null);
        }
        if (this.getNome().length() < 5 || this.getNome().length() > 30) {
            throw new TamanhoProdutoInvalidoException(null);
        }
    }

    private void validaTudo() {
        this.validaNome();
    }

}
