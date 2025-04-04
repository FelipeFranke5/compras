package dev.franke.felipe.compras.compras.api.dto.in;

import dev.franke.felipe.compras.compras.api.exception.NomeProdutoObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.TamanhoProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoObrigatorioException;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoINDTO {

    private String nomeProduto;
    private BigDecimal precoProduto;

    private void validaNome() {
        if (this.getNomeProduto() == null || this.getNomeProduto().isBlank()) {
            throw new NomeProdutoObrigatorioException(null);
        }
        if (this.getNomeProduto().length() < 5 || this.getNomeProduto().length() > 30) {
            throw new TamanhoProdutoInvalidoException(null);
        }
    }

    private void validaPreco() {
        if (this.getPrecoProduto() == null) {
            throw new ValorProdutoObrigatorioException(null);
        }
        int valor = this.getPrecoProduto().intValueExact();
        if (valor <= 0 || valor > 500) {
            throw new ValorProdutoInvalidoException(null);
        }
    }

    public void validaTudo() {
        this.validaNome();
        this.validaPreco();
    }

}
