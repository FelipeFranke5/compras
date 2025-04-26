package dev.franke.felipe.compras.compras.api.controller.helper.produto;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.MockMvc;

public class ProdutoControllerHelper {

    private static final String URL_BASE = "/api/v1/produto";

    private final ProdutoServiceHelper serviceHelper;
    private final ResponseAssertionHelper assertionHelper;
    private final ObjectMapper mapper;

    public ProdutoControllerHelper(MockMvc mockMvc, ProdutoService produtoService, ObjectMapper mapper) {
        this.serviceHelper = new ProdutoServiceHelper(produtoService);
        this.assertionHelper = new ResponseAssertionHelper(mockMvc);
        this.mapper = mapper;
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

    private String obtemUrlSomaPreco() {
        return URL_BASE + "/soma_preco_produtos";
    }

    private String obtemUrlExistePorNome() {
        return URL_BASE + "/existe/PaoDeForma";
    }

    public boolean nomeExiste() throws Exception {
        serviceHelper.mockaProdutoPorNomeExiste();
        assertionHelper.execAssertionsNomeExiste(obtemUrlExistePorNome());
        return true;
    }

    public boolean nomeNaoExiste() throws Exception {
        serviceHelper.mockaProdutoPorNomeNaoExiste();
        assertionHelper.execAssertionsNomeNaoExiste(obtemUrlExistePorNome());
        return true;
    }

    public boolean somaPrecos(Object[] mocks, Matcher<?>[] matchers) throws Exception {
        var requisicaoString = mapper.writeValueAsString(mocks[0]);
        return assertionsSomaPrecos(matchers, obtemUrlSomaPreco(), requisicaoString);
    }

    @SuppressWarnings("unchecked")
    public boolean somaPrecosNenhumId() throws Exception {
        var mocks = serviceHelper.mockaSomaProdutosNenhumIdEncontrado();
        var soma = (BigDecimal) mocks[1];
        var produtos = (List<String>) mocks[2];
        var matchers = assertionHelper.matchersNenhumProdutoEncontrado(soma, produtos);
        return somaPrecos(mocks, matchers);
    }

    @SuppressWarnings("unchecked")
    public boolean somaPrecosAlgunsIds() throws Exception {
        var mocks = serviceHelper.mockaSomaProdutosIdsEncontrados();
        var soma = (BigDecimal) mocks[1];
        var produtos = (List<String>) mocks[2];
        var matchers = assertionHelper.matchersAlgunsProdutosEncontrados(soma, produtos);
        return somaPrecos(mocks, matchers);
    }

    @SuppressWarnings("unchecked")
    public boolean somaPrecosMixIdsValidosEinvalidos() throws Exception {
        var mocks = serviceHelper.mockaSomaProdutosMixProdutos();
        var ids = (List<?>) mocks[0];
        var soma = (BigDecimal) mocks[1];
        var produtos = (List<String>) mocks[2];
        var matchers = assertionHelper.matchersMixProdutos(soma, produtos, ids);
        return somaPrecos(mocks, matchers);
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
        assertionHelper.execAssertionsListaProdutosVazia(obtemUrlDaListagem(ordenada));
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
