package dev.franke.felipe.compras.compras.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.franke.felipe.compras.compras.api.controller.helper.produto.ProdutoControllerHelper;
import dev.franke.felipe.compras.compras.api.links.ProdutoLink;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import dev.franke.felipe.compras.compras.api.service.lista_produtos.ResultadoSomaProdutos;
import java.math.BigDecimal;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    private static final String URL_BASE = "/api/v1/produto";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ProdutoService service;

    @MockitoBean
    private ProdutoLink link;

    private ProdutoControllerHelper auxiliarTestes;

    @BeforeEach
    void setUp() {
        auxiliarTestes = new ProdutoControllerHelper(mockMvc, service);
    }

    private void exibirNomeMetodo(String nomeMetodo) {
        LOGGER.info("Metodo == {}", nomeMetodo);
    }

    private String nomeMetodo() {
        return StackWalker.getInstance()
                .walk(s -> s.skip(1).findFirst())
                .orElseThrow(() -> new RuntimeException("Erro ao obter nome do metodo"))
                .getMethodName();
    }

    @Test
    @DisplayName("Teste - existePorNome - Nome existe")
    void testeQuandoNomeExiste_ExistePorNome_RetornaTrue() throws Exception {
        when(service.produtoExistePorNome("PaoDeForma")).thenReturn(true);
        var uri = URL_BASE + "/existe/PaoDeForma";
        exibirNomeMetodo(nomeMetodo());
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existe", Matchers.is(true)));
    }

    @Test
    @DisplayName("Teste - existePorNome - Nome nao existe")
    void testeQuandoNomeNaoExiste_ExistePorNome_RetornaFalse() throws Exception {
        when(service.produtoExistePorNome("PaoDeForma")).thenReturn(false);
        var uri = URL_BASE + "/existe/PaoDeForma";
        exibirNomeMetodo(nomeMetodo());
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existe", Matchers.is(false)));
    }

    @Test
    @DisplayName("Teste - somaPrecos - Nenhum produto encontrado")
    void testeQuandoNenhumProdutoEncontrado_SomaPrecos_RetornaListaZerada() throws Exception {
        var listaIds = List.of(1L, 2L, 3L);
        var soma = BigDecimal.valueOf(0);
        List<String> produtos = List.of();
        List<Object> idsInvalidos = List.of();
        List<Long> idsEncontrados = List.of();
        List<Long> idsNaoEncontrados = List.of(1L, 2L, 3L);

        var resultadoSomaProdutos =
                new ResultadoSomaProdutos(soma, produtos, listaIds, idsInvalidos, idsEncontrados, idsNaoEncontrados);

        when(service.calculaTotalProdutos(anyList())).thenReturn(resultadoSomaProdutos);

        var uri = URL_BASE + "/soma_preco_produtos";
        var requisicaoString = mapper.writeValueAsString(listaIds);

        var matcherSoma = Matchers.is(soma.intValue());
        var matcherProdutos = Matchers.is(produtos);
        var matcherIdsValidos = Matchers.contains(1, 2, 3);
        var matcherIdsInvalidos = Matchers.hasSize(0);
        var matcherIdsEncontrados = Matchers.hasSize(0);
        var matcherIdsNaoEncontrados = Matchers.contains(1, 2, 3);
        var matchers = new Matcher[] {
            matcherSoma,
            matcherProdutos,
            matcherIdsValidos,
            matcherIdsInvalidos,
            matcherIdsEncontrados,
            matcherIdsNaoEncontrados
        };

        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.assertionsSomaPrecos(matchers, uri, requisicaoString);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - somaPrecos - Produtos encontrados")
    void testeQuandoProdutosEncontrados_SomaPrecos_RetornaListaComProdutosEncontrados() throws Exception {
        var nomeProduto1 = "Açucar";
        var precoProduto1 = BigDecimal.valueOf(1000);
        var descricaoProduto1 = nomeProduto1 + " - " + precoProduto1;
        var nomeProduto2 = "PaoDeForma";
        var precoProduto2 = BigDecimal.valueOf(2000);
        var descricaoProduto2 = nomeProduto2 + " - " + precoProduto2;
        var listaIds = List.of(1L, 2L);
        var soma = BigDecimal.valueOf(precoProduto1.intValue() + precoProduto2.intValue());

        List<String> produtos = List.of(descricaoProduto1, descricaoProduto2);
        List<Object> idsInvalidos = List.of();
        List<Long> idsEncontrados = List.of(1L, 2L);
        List<Long> idsNaoEncontrados = List.of();

        var resultadoSomaProdutos =
                new ResultadoSomaProdutos(soma, produtos, listaIds, idsInvalidos, idsEncontrados, idsNaoEncontrados);

        when(service.calculaTotalProdutos(anyList())).thenReturn(resultadoSomaProdutos);

        var uri = URL_BASE + "/soma_preco_produtos";
        var requisicaoString = mapper.writeValueAsString(listaIds);

        var matcherSoma = Matchers.is(soma.intValue());
        var matcherProdutos = Matchers.is(produtos);
        var matcherIdsValidos = Matchers.contains(1, 2);
        var matcherIdsInvalidos = Matchers.hasSize(0);
        var matcherIdsEncontrados = Matchers.contains(1, 2);
        var matcherIdsNaoEncontrados = Matchers.hasSize(0);
        var matchers = new Matcher[] {
            matcherSoma,
            matcherProdutos,
            matcherIdsValidos,
            matcherIdsInvalidos,
            matcherIdsEncontrados,
            matcherIdsNaoEncontrados
        };

        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.assertionsSomaPrecos(matchers, uri, requisicaoString);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - somaPrecos - Produtos encontrados/ids invalidos/nao encontrados")
    void testeQuandoMixProdutos_SomaPrecos_RetornaListaVariada() throws Exception {
        var nomeProduto1 = "Açucar";
        var precoProduto1 = BigDecimal.valueOf(1000);
        var descricaoProduto1 = nomeProduto1 + " - " + precoProduto1;
        var nomeProduto2 = "PaoDeForma";
        var precoProduto2 = BigDecimal.valueOf(2000);
        var descricaoProduto2 = nomeProduto2 + " - " + precoProduto2;
        var listaIds = List.of(1L, 2L, 3L, 4L, "invalido-id1", "invalido-id2");
        var soma = BigDecimal.valueOf(precoProduto1.intValue() + precoProduto2.intValue());

        List<String> produtos = List.of(descricaoProduto1, descricaoProduto2);
        List<Object> idsInvalidos = List.of(listaIds.get(4), listaIds.get(5));
        List<Long> idsValidos = List.of(1L, 2L, 3L, 4L);
        List<Long> idsEncontrados = List.of(1L, 2L);
        List<Long> idsNaoEncontrados = List.of(3L, 4L);

        var resultadoSomaProdutos =
                new ResultadoSomaProdutos(soma, produtos, idsValidos, idsInvalidos, idsEncontrados, idsNaoEncontrados);

        when(service.calculaTotalProdutos(anyList())).thenReturn(resultadoSomaProdutos);

        var uri = URL_BASE + "/soma_preco_produtos";
        var requisicaoString = mapper.writeValueAsString(listaIds);

        var matcherSoma = Matchers.is(soma.intValue());
        var matcherProdutos = Matchers.is(produtos);
        var matcherIdsValidos = Matchers.contains(1, 2, 3, 4);
        var matcherIdsInvalidos = Matchers.contains(listaIds.get(4), listaIds.get(5));
        var matcherIdsEncontrados = Matchers.contains(1, 2);
        var matcherIdsNaoEncontrados = Matchers.contains(3, 4);
        var matchers = new Matcher[] {
            matcherSoma,
            matcherProdutos,
            matcherIdsValidos,
            matcherIdsInvalidos,
            matcherIdsEncontrados,
            matcherIdsNaoEncontrados
        };

        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.assertionsSomaPrecos(matchers, uri, requisicaoString);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutosPrecoAbaixo - Valor valido/lista vazia")
    void testeQuandoValorValido_ListaProdutosPrecoAbaixo_RetornaListaVazia() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosValorValidoListaVazia(true);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutosPrecoAbaixo - Valor valido/lista com produtos")
    void testeQuandoValorValido_ListaProdutosPrecoAbaixo_RetornaListaDeProdutos() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosValorValidoListaComProdutos(true);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutosPrecoAbaixo - Valor invalido")
    void testeQuandoValorInvalido_ListaProdutosPrecoAbaixo_RetornaBadRequest() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosValorInvalido(true);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutosPrecoAcima - Valor valido/lista vazia")
    void testeQuandoValorValido_ListaProdutosPrecoAcima_RetornaListaVazia() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosValorValidoListaVazia(false);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutosPrecoAcima - Valor valido/lista com produtos")
    void testeQuandoValorValido_ListaProdutosPrecoAcima_RetornaListaDeProdutos() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosValorValidoListaComProdutos(false);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutosPrecoAcima - Valor invalido")
    void testeQuandoValorInvalido_ListaProdutosPrecoAcima_RetornaBadRequest() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosValorInvalido(false);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutos - Lista Vazia")
    void testeQuandoNaoHaProdutos_ListaProdutos_RetornaListaVazia() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosPadraoVazia(false);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaProdutos - Lista com Produtos")
    void testeQuandoProdutosExistem_ListaProdutos_RetornaListaComProdutos() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosPadraoComProdutos(false);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaOrdenada - Lista Vazia")
    void testeQuandoNaoHaProdutos_ListaOrdenada_RetornaListaVazia() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosPadraoVazia(true);
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - listaOrdenada - Lista com Produtos")
    void testeQuandoProdutosExistem_ListaOrdenada_RetornaListaComProdutos() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.listaProdutosPadraoComProdutos(true);
        assertTrue(testeFinalizado);
    }
}
