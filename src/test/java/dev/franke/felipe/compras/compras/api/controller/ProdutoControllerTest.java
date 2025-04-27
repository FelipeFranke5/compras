package dev.franke.felipe.compras.compras.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.franke.felipe.compras.compras.api.controller.helper.produto.ProdutoControllerHelper;
import dev.franke.felipe.compras.compras.api.links.ProdutoLink;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
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
        auxiliarTestes = new ProdutoControllerHelper(mockMvc, service, mapper);
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
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.nomeExiste();
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - existePorNome - Nome nao existe")
    void testeQuandoNomeNaoExiste_ExistePorNome_RetornaFalse() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.nomeNaoExiste();
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - somaPrecos - Nenhum produto encontrado")
    void testeQuandoNenhumProdutoEncontrado_SomaPrecos_RetornaListaZerada() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.somaPrecosNenhumId();
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - somaPrecos - Produtos encontrados")
    void testeQuandoProdutosEncontrados_SomaPrecos_RetornaListaComProdutosEncontrados() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.somaPrecosAlgunsIds();
        assertTrue(testeFinalizado);
    }

    @Test
    @DisplayName("Teste - somaPrecos - Produtos encontrados/ids invalidos/nao encontrados")
    void testeQuandoMixProdutos_SomaPrecos_RetornaListaVariada() throws Exception {
        exibirNomeMetodo(nomeMetodo());
        var testeFinalizado = auxiliarTestes.somaPrecosMixIdsValidosEinvalidos();
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
