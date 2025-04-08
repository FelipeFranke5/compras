package dev.franke.felipe.compras.compras.api.repository;

import dev.franke.felipe.compras.compras.api.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto novoProduto1() {
        var nome = "Pao de Forma - 2 uni";
        var preco = new BigDecimal(756).round(new MathContext(756, RoundingMode.CEILING));
        return new Produto(nome, preco);
    }

    private Produto novoProduto2() {
        var nome = "Açucar - 3 uni";
        var preco = new BigDecimal(1000).round(new MathContext(1000, RoundingMode.CEILING));
        return new Produto(nome, preco);
    }

    @Test
    @DisplayName("Teste - Metodo save()")
    void testeDadoProduto_QuandoSaveChamado_DeveRetornarProduto() {
        var produto = this.novoProduto1();
        var produtoSalvo = this.produtoRepository.save(produto);
        assertNotNull(produtoSalvo);
        assertNotNull(produtoSalvo.getDataCriacao());
        assertNotNull(produtoSalvo.getDataModificacao());
        assertTrue(produtoSalvo.getId() > 0);
    }

    @Test
    @DisplayName("Teste - Metodo findAll()")
    void testeDadoListaProdutos_QuandoFindAllChamado_DeveRetornarListaProdutos() {
        var produto1 = this.novoProduto1();
        var produto2 = this.novoProduto2();
        this.produtoRepository.save(produto1);
        this.produtoRepository.save(produto2);
        var listaProdutos = this.produtoRepository.findAll();
        assertNotNull(listaProdutos);
        assertFalse(listaProdutos.isEmpty());
        assertEquals(2, listaProdutos.size());
    }

}
