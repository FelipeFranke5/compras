package dev.franke.felipe.compras.compras.api.dto.in;

import dev.franke.felipe.compras.compras.api.exception.NomeProdutoObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.TamanhoProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoObrigatorioException;
import java.math.BigDecimal;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
public class ProdutoINDTO {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final int PRECO_MAX = 50000;

    private String nomeProduto;
    private BigDecimal precoProduto;

    private void validaNome() {
        LOGGER.info("Iniciando validacao do nome do produto");
        var nomeProdutoNulo = this.getNomeProduto() == null;
        if (nomeProdutoNulo) {
            LOGGER.warn("Nome do Produto nulo");
            throw new NomeProdutoObrigatorioException("Nome do produto e obrigatorio");
        }
        var nomeProdutoVazio = this.getNomeProduto().isBlank();
        if (nomeProdutoVazio) {
            LOGGER.warn("Nome do Produto em branco");
            throw new NomeProdutoObrigatorioException("Nome do produto nao pode estar em branco");
        }
        var nomeProdutoTamanho = this.getNomeProduto().length();
        LOGGER.info("Tamanho relacionado ao nome do produto: {}", nomeProdutoTamanho);
        if (nomeProdutoTamanho < 5 || nomeProdutoTamanho > 30) {
            LOGGER.warn("Tamanho relacionado ao nome do produto nao e valido");
            throw new TamanhoProdutoInvalidoException("Nome do produto deve ter entre 5 e 30 caracteres");
        }
    }

    private void validaPreco() {
        LOGGER.info("Iniciando validacao do preco do produto");
        var precoValido = true;
        var precoProdutoNulo = this.getPrecoProduto() == null;

        if (precoProdutoNulo) {
            LOGGER.warn("O valor informado para o preco do produto e nulo");
            throw new ValorProdutoObrigatorioException("Valor do produto e obrigatorio");
        }

        int valor = this.getPrecoProduto().intValue();
        LOGGER.info("Preco definido para o produto: {}", valor);

        var precoProdutoZeroOuNegativo = valor <= 0;
        var precoProdutoMaiorQue50000 = valor > PRECO_MAX;

        if (precoProdutoZeroOuNegativo) {
            precoValido = false;
            LOGGER.warn("O valor definido para o preco ({}) e menor ou igual a zero", valor);
        }

        if (precoProdutoMaiorQue50000) {
            precoValido = false;
            LOGGER.warn("O valor definido para o preco ({}) e maior que o permitido", valor);
        }

        if (!precoValido) {
            LOGGER.warn("Lancando excecao pois o valor de preco para o produto nao e valido");
            throw new ValorProdutoInvalidoException("Valor invalido para o preco do produto");
        }
    }

    public void validaTudo() {
        LOGGER.info("Iniciando validacao dos campos do produto");
        this.validaNome();
        this.validaPreco();
        LOGGER.info("Validacao dos campos do produto realizada com sucesso");
    }
}
