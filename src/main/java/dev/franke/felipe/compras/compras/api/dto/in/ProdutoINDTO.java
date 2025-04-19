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

    private String nomeProduto;
    private BigDecimal precoProduto;

    private void validaNome() {
        LOGGER.info("Iniciando validacao do nome");
        var nomeProdutoNulo = this.getNomeProduto() == null;
        var nomeProdutoVazio = this.getNomeProduto().isBlank();
        if (nomeProdutoNulo || nomeProdutoVazio) {
            LOGGER.info("Nome nulo: {}", nomeProdutoNulo);
            LOGGER.info("Nome vazio: {}", nomeProdutoVazio);
            throw new NomeProdutoObrigatorioException("Nome do produto e obrigatorio");
        }
        var nomeProdutoTamanho = this.getNomeProduto().length();
        if (nomeProdutoTamanho < 5 || nomeProdutoTamanho > 30) {
            LOGGER.info("Tamanho do nome: {}", nomeProdutoTamanho);
            throw new TamanhoProdutoInvalidoException("Nome do produto deve ter entre 5 e 30 caracteres");
        }
    }

    private void validaPreco() {
        LOGGER.info("Iniciando validacao do preco");
        var precoProdutoNulo = this.getPrecoProduto() == null;
        if (precoProdutoNulo) {
            LOGGER.info("O valor de preco e nulo");
            throw new ValorProdutoObrigatorioException("Valor do produto e obrigatorio");
        }
        int valor = this.getPrecoProduto().intValue();
        if (valor <= 0 || valor > 50000) {
            LOGGER.info("Valor do produto: {}", valor);
            throw new ValorProdutoInvalidoException("Valor do produto deve ser maior que 0 e menor que 50000");
        }
    }

    public void validaTudo() {
        LOGGER.info("Iniciando validacao dos campos");
        this.validaNome();
        this.validaPreco();
        LOGGER.info("Validacao dos campos realizada com sucesso");
    }
}
