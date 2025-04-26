package dev.franke.felipe.compras.compras.api.controller.helper.produto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.MockMvc;

public class ProdutoControllerHelper {

    private static final String URL_BASE = "/api/v1/produto";

    private final MockMvc mockMvc;
    private final ProdutoService service;
    private final ProdutoServiceHelper serviceHelper;
    private final ResponseAssertionHelper assertionHelper;

    public ProdutoControllerHelper(MockMvc mockMvc, ProdutoService produtoService) {
        this.mockMvc = mockMvc;
        this.service = produtoService;
        this.serviceHelper = new ProdutoServiceHelper(produtoService);
        this.assertionHelper = new ResponseAssertionHelper(mockMvc);
    }

    private String obtemUrlDaListagem(boolean ordenada) {
        return ordenada ? URL_BASE + "/lista_ordenada" : URL_BASE + "/lista_padrao";
    }

    private String obtemUrlPreco(boolean precoAbaixo) {
        return precoAbaixo ? URL_BASE + "/lista_preco_abaixo/5000" : URL_BASE + "/lista_preco_acima/5000";
    }

    private String obtemUrlPrecoValorInvalido(boolean precoAbaixo) {
        return precoAbaixo ? URL_BASE + "/lista_preco_abaixo/[]" : URL_BASE + "/lista_preco_acima/[]";
    }

    public boolean listaProdutosValorValidoListaVazia(boolean precoAbaixo) throws Exception {
        serviceHelper.mockaProdutoPorPrecoListaVazia(precoAbaixo);
        assertionHelper.execAssertionsListaProdutosVazia(obtemUrlPreco(precoAbaixo));
        return true;
    }

    public boolean listaProdutosValorValidoListaComProdutos(boolean precoAbaixo) throws Exception {
        var produtos = serviceHelper.mockaProdutoPorPrecoValorValido(precoAbaixo);
        assertionHelper.execAssertionsListaProdutos(
                assertionHelper.matchersProdutos(produtos), obtemUrlPreco(precoAbaixo));
        return true;
    }

    public boolean listaProdutosValorInvalido(boolean precoAbaixo) throws Exception {
        serviceHelper.mockaProdutoPorPrecoValorInvalido(precoAbaixo);
        assertionHelper.execAssertionsPrecoInvalidoProduto(obtemUrlPrecoValorInvalido(precoAbaixo));
        return true;
    }

    public boolean listaProdutosPadraoVazia(boolean ordenada) throws Exception {
        serviceHelper.mockaListagemPadrao(ordenada);
        mockMvc.perform(get(obtemUrlDaListagem(ordenada))).andDo(print()).andExpect(status().isOk());
        return true;
    }

    public boolean listaProdutosPadraoComProdutos(boolean ordenada) throws Exception {
        var produtos = serviceHelper.mockaListagemPadrao(ordenada);
        assertionHelper.execAssertionsListaProdutos(
                assertionHelper.matchersProdutos(produtos), obtemUrlDaListagem(ordenada));
        return true;
    }

    public boolean assertionsSomaPrecos(Matcher<?>[] matchers, String uri, String requisicaoString) throws Exception {
        assert uri != null;
        assert requisicaoString != null;
        assert !uri.isBlank();
        assertionHelper.execAssertionsSomaPrecos(matchers, uri, requisicaoString);
        return true;
    }
}
