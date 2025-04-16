package dev.franke.felipe.compras.compras.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    private ValidadorTestes validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorTestes();
    }

    @Test
    @DisplayName("Teste - listaTodosProdutos - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ListaTodosProdutos_RetornaListaVazia() {
        when(repository.findAll()).thenReturn(List.of());
        var resultado = service.listaTodosProdutos();
        validador.validaNuloListasVazias(resultado);
    }

    @Test
    @DisplayName("Teste - listaTodosProdutos - Lista com 2 Produtos")
    void testeQuandoExistem2Registros_ListaTodosProdutos_RetornaListaCom2Produtos() {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(700));
        var produto2 = new Produto("Sal Lebre", BigDecimal.valueOf(1000));
        when(repository.findAll()).thenReturn(List.of(produto1, produto2));
        var resultado = service.listaTodosProdutos();
        validador.validaNuloListasTamanho2(resultado);
    }

    @Test
    @DisplayName("Teste - listaTodosProdutosOrdenada - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ListaTodosProdutosOrdenada_RetornaListaVazia() {
        when(repository.findAllByOrderByDataCriacaoDesc()).thenReturn(List.of());
        var resultado = service.listaTodosProdutosOrdenada();
        validador.validaNuloListasVazias(resultado);
    }

    @Test
    @DisplayName("Teste - listaTodosProdutosOrdenada - Lista com 2 Produtos")
    void testeQuandoExistem2Registros_ListaTodosProdutosOrdenada_RetornaListaCom2Produtos() {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(700));
        produto1.setDataCriacao(LocalDateTime.now());
        produto1.setDataModificacao(produto1.getDataCriacao());
        var produto2 = new Produto("Sal Lebre", BigDecimal.valueOf(1000));
        produto2.setDataCriacao(LocalDateTime.now());
        produto2.setDataModificacao(produto2.getDataCriacao());
        when(repository.findAllByOrderByDataCriacaoDesc()).thenReturn(List.of(produto2, produto1));
        var resultado = service.listaTodosProdutosOrdenada();
        validador.validaNuloListasTamanho2(resultado);
        assertTrue(resultado.get(0).getDataCriacao().isAfter(resultado.get(1).getDataCriacao()));
    }
}
