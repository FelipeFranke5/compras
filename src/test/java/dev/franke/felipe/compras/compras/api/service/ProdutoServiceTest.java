package dev.franke.felipe.compras.compras.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.exception.ListaProdutosInvalidaException;
import dev.franke.felipe.compras.compras.api.exception.NomeProdutoJaCadastradoException;
import dev.franke.felipe.compras.compras.api.exception.NomeProdutoObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.ProdutoNaoEncontradoException;
import dev.franke.felipe.compras.compras.api.exception.ProdutoRequisicaoInvalidaException;
import dev.franke.felipe.compras.compras.api.exception.QueryPrecoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.TamanhoProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoObrigatorioException;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
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

    @Test
    @DisplayName("Teste - cadastroProduto - Produto Nulo")
    void testeQuandoRequisicaoNula_CadastraProduto_LancaProdutoRequisicaoInvalidaException() {
        assertThrowsExactly(ProdutoRequisicaoInvalidaException.class, () -> service.cadastraProduto(null));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com nome nulo")
    void testeQuandoNomeProdutoNulo_CadastraProduto_LancaNomeProdutoObrigatorioException() {
        var requisicao = new ProdutoINDTO();
        assertThrowsExactly(NomeProdutoObrigatorioException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com nome em branco")
    void testeQuandoNomeProdutoEmBranco_CadastraProduto_LancaNomeProdutoObrigatorioException() {
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("");
        assertThrowsExactly(NomeProdutoObrigatorioException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com nome em tamanho invalido 1")
    void testeQuandoNomeProdutoTemTamanhoInvalido_CadastraProduto_LancaTamanhoProdutoInvalidoException() {
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("No");
        assertThrowsExactly(TamanhoProdutoInvalidoException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com nome em tamanho invalido 2")
    void testeQuandoNomeProdutoTemTamanhoInvalido2_CadastraProduto_LancaTamanhoProdutoInvalidoException() {
        var requisicao = new ProdutoINDTO();
        var stringInvalida = "b".repeat(35);
        requisicao.setNomeProduto(stringInvalida);
        assertThrowsExactly(TamanhoProdutoInvalidoException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com BigDecimal nulo no preco")
    void testeQuandoPrecoProdutoBigDecimalNulo_CadastraProduto_LancaValorProdutoObrigatorioException() {
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("Açucar");
        assertThrowsExactly(ValorProdutoObrigatorioException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com valor preco negativo")
    void testeQuandoPrecoProdutoNegativo_CadastraProduto_LancaValorProdutoInvalidoException() {
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("Açucar");
        requisicao.setPrecoProduto(BigDecimal.valueOf(-1));
        assertThrowsExactly(ValorProdutoInvalidoException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com valor preco zerado")
    void testeQuandoPrecoProdutoZerado_CadastraProduto_LancaValorProdutoInvalidoException() {
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("Açucar");
        requisicao.setPrecoProduto(BigDecimal.valueOf(0));
        assertThrowsExactly(ValorProdutoInvalidoException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com valor preco maior que o permitido")
    void testeQuandoPrecoProdutoMaiorQuePermitido_CadastraProduto_LancaValorProdutoInvalidoException() {
        var precoMax = ProdutoINDTO.PRECO_MAX;
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("Açucar");
        requisicao.setPrecoProduto(BigDecimal.valueOf(precoMax + 1));
        assertThrowsExactly(ValorProdutoInvalidoException.class, () -> service.cadastraProduto(requisicao));
        verify(repository, never()).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - cadastroProduto - Produto com o nome ja existe no banco")
    void testeQuandoNomeJaCadastrado_CadastraProduto_LancaNomeProdutoJaCadastradoException() {
        var precoMax = ProdutoINDTO.PRECO_MAX;
        var requisicao = new ProdutoINDTO();
        requisicao.setNomeProduto("Açucar");
        requisicao.setPrecoProduto(BigDecimal.valueOf(precoMax - 1));
        when(repository.existsByNome("Açucar")).thenReturn(true);
        assertThrowsExactly(NomeProdutoJaCadastradoException.class, () -> service.cadastraProduto(requisicao));
        verify(repository).existsByNome(anyString());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Teste - calculaTotalProdutos - Lista nula")
    void testeQuandoListaNula_CalculaTotalProdutos_LancaListaProdutosInvalidaException() {
        assertThrowsExactly(ListaProdutosInvalidaException.class, () -> service.calculaTotalProdutos(null));
        verify(repository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Teste - calculaTotalProdutos - Lista vazia")
    void testeQuandoListaVazia_CalculaTotalProdutos_LancaListaProdutosInvalidaException() {
        var listaVazia = List.of();
        assertThrowsExactly(ListaProdutosInvalidaException.class, () -> service.calculaTotalProdutos(listaVazia));
        verify(repository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Teste - calculaTotalProdutos - Apenas valores desconsiderados")
    void testeQuandoApenasIdsInvalidos_CalculaTotalProdutos_RetornaListaComValoresDesconsiderados() {
        var listaComIdsInvalidos = new ArrayList<>();
        listaComIdsInvalidos.add("1");
        listaComIdsInvalidos.add("2");
        listaComIdsInvalidos.add("3");
        var listaResultadoEsperado = List.of("1", "2", "3");
        var resultado = service.calculaTotalProdutos(listaComIdsInvalidos);
        assertEquals(listaResultadoEsperado, resultado.idsInvalidos());
        assertEquals(List.of(), resultado.idsEncontrados());
        assertEquals(List.of(), resultado.idsNaoEncontrados());
        assertEquals(List.of(), resultado.idsValidos());
        verify(repository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Teste - calculaTotalProdutos - Apenas valores considerados")
    void testeQuandoApenasIdsValidos_CalculaTotalProdutos_RetornaListaComValoresConsiderados() {
        var precoProduto1 = ThreadLocalRandom.current().nextInt(2, 50000);
        var precoProduto2 = ThreadLocalRandom.current().nextInt(2, 50000);
        var precoProduto3 = ThreadLocalRandom.current().nextInt(2, 50000);
        var nomeProduto1 = "Açucar";
        var nomeProduto2 = "Leite";
        var nomeProduto3 = "Morango";
        var listaComValoresValidos = new ArrayList<>();
        listaComValoresValidos.add(1);
        listaComValoresValidos.add(2);
        listaComValoresValidos.add(3);
        var listaResultadoEsperado = List.of(1L, 2L, 3L);
        var nomesProdutos = List.of(
                nomeProduto1 + " - " + precoProduto1,
                nomeProduto2 + " - " + precoProduto2,
                nomeProduto3 + " - " + precoProduto3);
        var somaEsperada = BigDecimal.valueOf(precoProduto1 + precoProduto2 + precoProduto3);

        var optionalProduto1 = Optional.of(new Produto(
                1L, nomeProduto1, BigDecimal.valueOf(precoProduto1), LocalDateTime.now(), LocalDateTime.now()));
        var optionalProduto2 = Optional.of(new Produto(
                2L, nomeProduto2, BigDecimal.valueOf(precoProduto2), LocalDateTime.now(), LocalDateTime.now()));
        var optionalProduto3 = Optional.of(new Produto(
                3L, nomeProduto3, BigDecimal.valueOf(precoProduto3), LocalDateTime.now(), LocalDateTime.now()));

        when(repository.findById(1L)).thenReturn(optionalProduto1);
        when(repository.findById(2L)).thenReturn(optionalProduto2);
        when(repository.findById(3L)).thenReturn(optionalProduto3);

        var resultado = service.calculaTotalProdutos(listaComValoresValidos);

        assertEquals(listaResultadoEsperado, resultado.idsValidos());
        assertEquals(listaResultadoEsperado, resultado.idsEncontrados());
        assertEquals(somaEsperada, resultado.soma());
        assertEquals(nomesProdutos, resultado.produtos());
        assertEquals(List.of(), resultado.idsInvalidos());
        assertEquals(List.of(), resultado.idsNaoEncontrados());
        verify(repository, times(3)).findById(anyLong());
    }

    @Test
    @DisplayName("Teste - calculaTotalProdutos - Mix de valores considerados/desconsiderados")
    void testeQuandoIdsMixados_CalculaTotalProdutos_RetornaListaMixada() {
        var precoProduto1 = ThreadLocalRandom.current().nextInt(2, 50000);
        var precoProduto2 = ThreadLocalRandom.current().nextInt(2, 50000);
        var precoProduto3 = ThreadLocalRandom.current().nextInt(2, 50000);
        var nomeProduto1 = "Açucar";
        var nomeProduto2 = "Leite";
        var nomeProduto3 = "Morango";
        var listaInput = new ArrayList<>();
        listaInput.add(1L);
        listaInput.add(2L);
        listaInput.add(3L);
        listaInput.add("1");
        listaInput.add("2");
        listaInput.add("3");
        var listaResultadoEsperadoIdsValidos = List.of(1L, 2L, 3L);
        var listaResultadoEsperadoIdsInvalidos = List.of("1", "2", "3");
        var nomesProdutos = List.of(
                nomeProduto1 + " - " + precoProduto1,
                nomeProduto2 + " - " + precoProduto2,
                nomeProduto3 + " - " + precoProduto3);
        var somaEsperada = BigDecimal.valueOf(precoProduto1 + precoProduto2 + precoProduto3);

        var optionalProduto1 = Optional.of(new Produto(
                1L, nomeProduto1, BigDecimal.valueOf(precoProduto1), LocalDateTime.now(), LocalDateTime.now()));
        var optionalProduto2 = Optional.of(new Produto(
                2L, nomeProduto2, BigDecimal.valueOf(precoProduto2), LocalDateTime.now(), LocalDateTime.now()));
        var optionalProduto3 = Optional.of(new Produto(
                3L, nomeProduto3, BigDecimal.valueOf(precoProduto3), LocalDateTime.now(), LocalDateTime.now()));

        when(repository.findById(1L)).thenReturn(optionalProduto1);
        when(repository.findById(2L)).thenReturn(optionalProduto2);
        when(repository.findById(3L)).thenReturn(optionalProduto3);

        var resultado = service.calculaTotalProdutos(listaInput);

        assertEquals(listaResultadoEsperadoIdsValidos, resultado.idsValidos());
        assertEquals(listaResultadoEsperadoIdsValidos, resultado.idsEncontrados());
        assertEquals(somaEsperada, resultado.soma());
        assertEquals(nomesProdutos, resultado.produtos());
        assertEquals(listaResultadoEsperadoIdsInvalidos, resultado.idsInvalidos());
        assertEquals(List.of(), resultado.idsNaoEncontrados());
        verify(repository, times(3)).findById(anyLong());
    }

    @Test
    @DisplayName("Teste - calculaTotalProdutos - Mix de valores considerados/desconsiderados/nao encontrados")
    void testeQuandoIdsMixados2_CalculaTotalProdutos_RetornaListaMixada2() {
        var precoProduto1 = ThreadLocalRandom.current().nextInt(2, 50000);
        var precoProduto2 = ThreadLocalRandom.current().nextInt(2, 50000);
        var precoProduto3 = ThreadLocalRandom.current().nextInt(2, 50000);
        var nomeProduto1 = "Açucar";
        var nomeProduto2 = "Leite";
        var nomeProduto3 = "Morango";
        var listaInput = new ArrayList<>();
        listaInput.add(1L);
        listaInput.add(2L);
        listaInput.add(3L);
        listaInput.add(4L);
        listaInput.add(5L);
        listaInput.add(6L);
        listaInput.add("1");
        listaInput.add("2");
        listaInput.add("3");
        var listaResultadoEsperadoIdsValidos = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        var listaResultadoEsperadoIdsInvalidos = List.of("1", "2", "3");
        var listaResultadoEsperadoIdsEncontrados = List.of(1L, 2L, 3L);
        var listaResultadoEsperadoIdsNaoEncontrados = List.of(4L, 5L, 6L);
        var nomesProdutos = List.of(
                nomeProduto1 + " - " + precoProduto1,
                nomeProduto2 + " - " + precoProduto2,
                nomeProduto3 + " - " + precoProduto3);
        var somaEsperada = BigDecimal.valueOf(precoProduto1 + precoProduto2 + precoProduto3);

        var optionalProduto1 = Optional.of(new Produto(
                1L, nomeProduto1, BigDecimal.valueOf(precoProduto1), LocalDateTime.now(), LocalDateTime.now()));
        var optionalProduto2 = Optional.of(new Produto(
                2L, nomeProduto2, BigDecimal.valueOf(precoProduto2), LocalDateTime.now(), LocalDateTime.now()));
        var optionalProduto3 = Optional.of(new Produto(
                3L, nomeProduto3, BigDecimal.valueOf(precoProduto3), LocalDateTime.now(), LocalDateTime.now()));

        when(repository.findById(1L)).thenReturn(optionalProduto1);
        when(repository.findById(2L)).thenReturn(optionalProduto2);
        when(repository.findById(3L)).thenReturn(optionalProduto3);
        when(repository.findById(4L)).thenThrow(ProdutoNaoEncontradoException.class);
        when(repository.findById(5L)).thenThrow(ProdutoNaoEncontradoException.class);
        when(repository.findById(6L)).thenThrow(ProdutoNaoEncontradoException.class);

        var resultado = service.calculaTotalProdutos(listaInput);

        assertEquals(listaResultadoEsperadoIdsValidos, resultado.idsValidos());
        assertEquals(listaResultadoEsperadoIdsEncontrados, resultado.idsEncontrados());
        assertEquals(somaEsperada, resultado.soma());
        assertEquals(nomesProdutos, resultado.produtos());
        assertEquals(listaResultadoEsperadoIdsInvalidos, resultado.idsInvalidos());
        assertEquals(listaResultadoEsperadoIdsNaoEncontrados, resultado.idsNaoEncontrados());
        verify(repository, times(6)).findById(anyLong());
    }
}
