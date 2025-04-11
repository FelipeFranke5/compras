package dev.franke.felipe.compras.compras.api.repository;

import dev.franke.felipe.compras.compras.api.model.Produto;
import org.junit.jupiter.api.BeforeEach;
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

    private Produto produtoSalvo;

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

    @BeforeEach
    void setUp() {
        Produto produto = this.novoProduto1();
        produtoSalvo = this.produtoRepository.save(produto);
    }

    @Test
    @DisplayName("Teste - Metodo save()")
    void testeDadoProduto_QuandoSaveChamado_DeveRetornarProduto() {
        assertNotNull(produtoSalvo);
        assertNotNull(produtoSalvo.getDataCriacao());
        assertNotNull(produtoSalvo.getDataModificacao());
        assertTrue(produtoSalvo.getId() > 0);
    }

    @Test
    @DisplayName("Teste - Metodo findAll()")
    void testeDadoListaProdutos_QuandoFindAllChamado_DeveRetornarListaProdutos() {
        var produto2 = this.novoProduto2();
        this.produtoRepository.save(produto2);
        var listaProdutos = produtoRepository.findAll();
        assertNotNull(listaProdutos);
        assertFalse(listaProdutos.isEmpty());
        assertEquals(2, listaProdutos.size());
    }

    @Test
    @DisplayName("Teste - Metodo findAllByOrderByDataCriacaoDesc()")
    void testeDadoListaProdutos_QuandoFindAllByOrderByDataCriacaoDescChamado_DeveRetornarListaOrdenada() {
        var produto2 = this.novoProduto2();
        var produtoSalvo2 = produtoRepository.save(produto2);
        var listaProdutos = produtoRepository.findAllByOrderByDataCriacaoDesc();
        assertNotNull(listaProdutos);
        assertFalse(listaProdutos.isEmpty());
        assertEquals(2, listaProdutos.size());
        assertEquals(produtoSalvo2, listaProdutos.get(0));
        assertEquals(produtoSalvo, listaProdutos.get(1));
    }

    @Test
    @DisplayName("Teste - Metodo findById()")
    void testeDadoIdValido_QuandoFindByIdChamado_DeveRetornarProduto() {
        var produto2 = this.novoProduto2();
        var produtoSalvo2 = produtoRepository.save(produto2);
        var produtoEncontrado1 = produtoRepository.findById(produtoSalvo.getId());
        var produtoEncontrado2 = produtoRepository.findById(produtoSalvo2.getId());
        assertTrue(produtoEncontrado1.isPresent());
        assertTrue(produtoEncontrado2.isPresent());
        assertEquals(produtoEncontrado1.get().getId(), produtoSalvo.getId());
        assertEquals(produtoEncontrado2.get().getId(), produtoSalvo2.getId());
        assertEquals(produtoEncontrado1.get(), produtoSalvo);
        assertEquals(produtoEncontrado2.get(), produtoSalvo2);
    }

    @Test
    @DisplayName("Teste - Metodo findByNome()")
    void testeDadoNomeValido_QuandoFindByNomeChamado_DeveRetornarProduto() {
        var stringQuery = "Pao de Forma - 2 uni";
        var produtoEncontrado = produtoRepository.findByNome(stringQuery);
        assertTrue(produtoEncontrado.isPresent());
    }

    @Test
    @DisplayName("Teste - Metodo existsByNome()")
    void testeDadoNomeValido_QUandoExistsByNomeChamado_DeveRetornarProduto() {
        var stringQuery = "Pao de Forma - 2 uni";
        var existe = produtoRepository.existsByNome(stringQuery);
        assertTrue(existe);
    }

    @Test
    @DisplayName("Teste - Metodo save() para update")
    void testeDadoProduto_QuandoSaveChamadoEmUpdate_DeveAlterarDados() {
        produtoSalvo.setNome("Outro Produto");
        var produtoAtualizado = produtoRepository.save(produtoSalvo);
        assertNotNull(produtoAtualizado);
        assertEquals(produtoAtualizado.getDataCriacao(), produtoSalvo.getDataCriacao());
        assertEquals("Outro Produto", produtoAtualizado.getNome());
    }

    @Test
    @DisplayName("Teste - Metodo delete()")
    void testeDadoProduto_QuandoDeleteChamado_DeveApagarProduto() {
        this.produtoRepository.deleteById(produtoSalvo.getId());
        var produtoOptional = produtoRepository.findById(produtoSalvo.getId());
        assertTrue(produtoOptional.isEmpty());
    }

    @Test
    @DisplayName("Teste - Metodo precoAbaixoDe()")
    void testeDadosProdutos_QuandoPrecoAbaixoDe2000_DeveRetornarProdutos1E2() {
        var produto2 = this.novoProduto2();
        produtoRepository.save(produto2);
        var valorQuery = new BigDecimal(2000);
        var produtos = produtoRepository.precoAbaixoDe(valorQuery);
        assertNotNull(produtos);
        assertEquals(2, produtos.size());
        assertTrue(produtos.get(0).getPreco().intValue() < 2000);
        assertTrue(produtos.get(1).getPreco().intValue() < 2000);
    }

    @Test
    @DisplayName("Teste - Metodo precoAbaixoDe() - Lista vazia")
    void testeDadosValoresForaDeAlcance_QuandoPrecoAbaixoDeChamado_DeveRetornarListaVazia() {
        var valorQuery = new BigDecimal(500);
        var produtos = produtoRepository.precoAbaixoDe(valorQuery);
        assertNotNull(produtos);
        assertTrue(produtos.isEmpty());
    }

    @Test
    @DisplayName("Teste - Metodo precoAcimaDe()")
    void testeDadosProdutos_QuandoPrecoAcimaDe2000_DeveRetornarProdutos1E2() {
        var valorQuery = new BigDecimal(3000);
        produtoSalvo.setPreco(valorQuery);
        produtoRepository.save(produtoSalvo);
        var produto2 = this.novoProduto2();
        produto2.setPreco(valorQuery);
        produtoRepository.save(produto2);
        var produtos = produtoRepository.precoAcimaDe(valorQuery);
        assertNotNull(produtos);
        assertEquals(2, produtos.size());
        assertTrue(produtos.get(0).getPreco().intValue() > 2000);
        assertTrue(produtos.get(1).getPreco().intValue() > 2000);
    }

    @Test
    @DisplayName("Teste - Metodo precoAcimaDe() - Lista vazia")
    void testeDadosValoresForaDeAlcance_QuandoPrecoAcimaDeChamado_DeveRetornarListaVazia() {
        var valorQuery = new BigDecimal(3000);
        var produtos = produtoRepository.precoAcimaDe(valorQuery);
        assertNotNull(produtos);
        assertTrue(produtos.isEmpty());
    }

}
