package dev.franke.felipe.compras.compras.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.exception.ProdutoNaoEncontradoException;
import dev.franke.felipe.compras.compras.api.exception.QueryPrecoInvalidoException;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

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
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Teste - listaTodosProdutos - Lista com 2 Produtos")
    void testeQuandoExistem2Registros_ListaTodosProdutos_RetornaListaCom2Produtos() {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(700));
        var produto2 = new Produto("Sal Lebre", BigDecimal.valueOf(1000));
        when(repository.findAll()).thenReturn(List.of(produto1, produto2));
        var resultado = service.listaTodosProdutos();
        validador.validaNuloListasTamanho2(resultado);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Teste - listaTodosProdutosOrdenada - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ListaTodosProdutosOrdenada_RetornaListaVazia() {
        when(repository.findAllByOrderByDataCriacaoDesc()).thenReturn(List.of());
        var resultado = service.listaTodosProdutosOrdenada();
        validador.validaNuloListasVazias(resultado);
        verify(repository).findAllByOrderByDataCriacaoDesc();
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
        verify(repository).findAllByOrderByDataCriacaoDesc();
    }

    @Test
    @DisplayName("Teste - produtoPorId - Produto Encontrado")
    void testeQuandoProdutoExiste_ProdutoPorId_RetornaProduto() {
        var produto = new Produto("Açucar", BigDecimal.valueOf(700));
        produto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        var resultado = service.produtoPorId(1L);
        validador.validaNuloOptionalProduto(resultado);
        assertEquals(produto, resultado);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Teste - produtoPorId - Produto Não Encontrado")
    void testeQuandoProdutoNaoExiste_ProdutoPorId_RetornaProdutoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProdutoNaoEncontradoException.class, () -> service.produtoPorId(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Teste - produtoPorNome - Produto Encontrado")
    void testeQuandoProdutoExiste_ProdutoPorNome_RetornaProduto() {
        var produto = new Produto("Açucar", BigDecimal.valueOf(700));
        when(repository.findByNome("Açucar")).thenReturn(Optional.of(produto));
        var resultado = service.produtoPorNome("Açucar");
        validador.validaNuloOptionalProduto(resultado);
        assertEquals(produto, resultado);
        verify(repository).findByNome("Açucar");
    }

    @Test
    @DisplayName("Teste - produtoPorNome - Produto Não Encontrado")
    void testeQuandoProdutoNaoExiste_ProdutoPorNome_RetornaProdutoNaoEncontrado() {
        when(repository.findByNome("Açucar")).thenReturn(Optional.empty());
        assertThrows(ProdutoNaoEncontradoException.class, () -> service.produtoPorNome("Açucar"));
        verify(repository).findByNome("Açucar");
    }

    @Test
    @DisplayName("Teste - produtoExistePorNome - Produto Encontrado")
    void testeQuandoProdutoExiste_ProdutoExistePorNome_RetornaTrue() {
        when(repository.existsByNome("Açucar")).thenReturn(true);
        var resultado = service.produtoExistePorNome("Açucar");
        assertTrue(resultado);
        verify(repository).existsByNome("Açucar");
    }

    @Test
    @DisplayName("Teste - produtoExistePorNome - Produto Não Encontrado")
    void testeQuandoProdutoNaoExiste_ProdutoExistePorNome_RetornaFalse() {
        when(repository.existsByNome("Açucar")).thenReturn(false);
        var resultado = service.produtoExistePorNome("Açucar");
        assertFalse(resultado);
        verify(repository).existsByNome("Açucar");
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAbaixo - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ProdutoPorPrecoAbaixo_RetornaListaVazia() {
        var preco = "35";
        var precoAbaixo = true;
        when(repository.precoAbaixoDe(any(BigDecimal.class))).thenReturn(List.of());
        var resultado = service.produtoPorPreco(preco, precoAbaixo);
        validador.validaNuloListasVazias(resultado);
        verify(repository).precoAbaixoDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Testes - produtoPorPreco/precoAbaixo - Lista com 2 Produtos")
    void testeQuandoExistem2Registros_ProdutoPorPrecoAbaixo_RetornaListaCom2Produtos() {
        var preco = "3000";
        var precoAbaixo = true;
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(700));
        var produto2 = new Produto("Sal Lebre", BigDecimal.valueOf(1000));
        when(repository.precoAbaixoDe(any(BigDecimal.class))).thenReturn(List.of(produto1, produto2));
        var resultado = service.produtoPorPreco(preco, precoAbaixo);
        validador.validaNuloListasTamanho2(resultado);
        verify(repository).precoAbaixoDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAbaixo - Valor invalido")
    void testeQuandoValorInvalido_ProdutoPorPrecoAbaixo_LancaQueryPrecoInvalidoException() {
        var preco = "inparsiavel";
        var precoAbaixo = true;
        assertThrowsExactly(QueryPrecoInvalidoException.class, () -> service.produtoPorPreco(preco, precoAbaixo));
        verify(repository, never()).precoAbaixoDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAbaixo - Valor invalido 2")
    void testeQuandoValorInvalido2_ProdutoPorPrecoAbaixo_LancaQueryPrecoInvalidoException() {
        var preco = 34f;
        var precoAbaixo = true;
        assertThrowsExactly(QueryPrecoInvalidoException.class, () -> service.produtoPorPreco(preco, precoAbaixo));
        verify(repository, never()).precoAbaixoDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAcima - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ProdutoPorPrecoAcima_RetornaListaVazia() {
        var preco = "35";
        var precoAbaixo = false;
        when(repository.precoAcimaDe(any(BigDecimal.class))).thenReturn(List.of());
        var resultado = service.produtoPorPreco(preco, precoAbaixo);
        validador.validaNuloListasVazias(resultado);
        verify(repository).precoAcimaDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAcima - Lista com 2 Produtos")
    void testeQuandoExistem2Registros_ProdutoPorPrecoAcima_RetornaListaCom2Produtos() {
        var preco = "3000";
        var precoAbaixo = false;
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(700));
        var produto2 = new Produto("Sal Lebre", BigDecimal.valueOf(1000));
        when(repository.precoAcimaDe(any(BigDecimal.class))).thenReturn(List.of(produto1, produto2));
        var resultado = service.produtoPorPreco(preco, precoAbaixo);
        validador.validaNuloListasTamanho2(resultado);
        verify(repository).precoAcimaDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAcima - Valor invalido")
    void testeQuandoValorInvalido_ProdutoPorPrecoAcima_LancaQueryPrecoInvalidoException() {
        var preco = "inparsiavel";
        var precoAbaixo = false;
        assertThrowsExactly(QueryPrecoInvalidoException.class, () -> service.produtoPorPreco(preco, precoAbaixo));
        verify(repository, never()).precoAcimaDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - produtoPorPreco/precoAcima - Valor invalido 2")
    void testeQuandoValorInvalido2_ProdutoPorPrecoAcima_LancaQueryPrecoInvalidoException() {
        var preco = 34f;
        var precoAbaixo = false;
        assertThrowsExactly(QueryPrecoInvalidoException.class, () -> service.produtoPorPreco(preco, precoAbaixo));
        verify(repository, never()).precoAcimaDe(any(BigDecimal.class));
    }

    @Test
    @DisplayName("Teste - cadastraProduto - Produto Cadastrado")
    void testeQuandoRequisicaoValida_CadastraProduto_RetornaProdutoCadastrado() {
        var requisicao = new ProdutoINDTO();
        var produto = new Produto("Açucar", BigDecimal.valueOf(700));
        requisicao.setNomeProduto("Açucar");
        requisicao.setPrecoProduto(BigDecimal.valueOf(700));
        when(repository.existsByNome("Açucar")).thenReturn(false);
        when(repository.save(any(Produto.class))).thenReturn(produto);
        var resultado = service.cadastraProduto(requisicao);
        validador.validaNuloOptionalProduto(resultado);
        assertEquals(produto, resultado);
        verify(repository).existsByNome("Açucar");
        verify(repository).save(any(Produto.class));
    }
}
