package dev.franke.felipe.compras.compras.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.franke.felipe.compras.compras.api.exception.QueryPrecoInvalidoException;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class ProdutoControllerHelper {

    private static final String URL_BASE = "/api/v1/produto";

    private final MockMvc mockMvc;
    private final ProdutoService service;

    public ProdutoControllerHelper(MockMvc mockMvc, ProdutoService produtoService) {
        this.mockMvc = mockMvc;
        this.service = produtoService;
    }

    private void execAssertionsSomaPrecos(Matcher<?>[] matchers, String uri, String requisicaoString) throws Exception {
        assert matchers != null;
        assert matchers.length == 6;

        var matcherSoma = matchers[0];
        var matcherProdutos = matchers[1];
        var matcherIdsValidos = matchers[2];
        var matcherIdsInvalidos = matchers[3];
        var matcherIdsEncontrados = matchers[4];
        var matcherIdsNaoEncontrados = matchers[5];

        mockMvc.perform(get(uri).content(requisicaoString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.soma", matcherSoma))
                .andExpect(jsonPath("$.produtos", matcherProdutos))
                .andExpect(jsonPath("$.idsValidos", matcherIdsValidos))
                .andExpect(jsonPath("$.idsInvalidos", matcherIdsInvalidos))
                .andExpect(jsonPath("$.idsEncontrados", matcherIdsEncontrados))
                .andExpect(jsonPath("$.idsNaoEncontrados", matcherIdsNaoEncontrados));
    }

    private void execAssertionsListaProdutos(Matcher<?>[] matchers, String uri) throws Exception {
        assert matchers != null;
        assert matchers.length == 3;
        var matcherTamanhoLista = matchers[0];
        var matcherNomesCadastrados = matchers[1];
        var matcherPrecosCadastrados = matchers[2];
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.produtos", matcherTamanhoLista))
                .andExpect(jsonPath("$._embedded.produtos[*].nomeProduto", matcherNomesCadastrados))
                .andExpect(jsonPath("$._embedded.produtos[*].precoProduto", matcherPrecosCadastrados));
    }

    private String obtemUrlDaListagem(boolean ordenada, List<Produto> produtos) {
        if (ordenada) {
            when(service.listaTodosProdutosOrdenada()).thenReturn(produtos);
            return URL_BASE + "/lista_ordenada";
        } else {
            when(service.listaTodosProdutos()).thenReturn(produtos);
            return URL_BASE + "/lista_padrao";
        }
    }

    public boolean listaProdutosValorValidoListaVazia(boolean precoAbaixo) throws Exception {
        List<Produto> listaVazia = List.of();

        if (precoAbaixo) {
            when(service.produtoPorPreco("5000", precoAbaixo)).thenReturn(listaVazia);
            var uri = URL_BASE + "/lista_preco_abaixo/5000";
            mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
        } else {
            when(service.produtoPorPreco("5000", precoAbaixo)).thenReturn(listaVazia);
            var uri = URL_BASE + "/lista_preco_acima/5000";
            mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
        }

        return true;
    }

    public boolean listaProdutosValorValidoListaComProdutos(boolean precoAbaixo) throws Exception {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(3000));
        var produto2 = new Produto("PaoDeForma", BigDecimal.valueOf(2000));
        List<Produto> produtos;
        String uri;

        if (precoAbaixo) {
            produtos = List.of(produto1, produto2);
            when(service.produtoPorPreco("5000", precoAbaixo)).thenReturn(produtos);
            uri = URL_BASE + "/lista_preco_abaixo/5000";
        } else {
            produto1.setPreco(BigDecimal.valueOf(7000));
            produto2.setPreco(BigDecimal.valueOf(10000));
            produtos = List.of(produto1, produto2);
            when(service.produtoPorPreco("5000", precoAbaixo)).thenReturn(produtos);
            uri = URL_BASE + "/lista_preco_acima/5000";
        }

        var matcherTamanhoLista = Matchers.hasSize(2);
        var matcherNomesProdutos = Matchers.contains(produto1.getNome(), produto2.getNome());
        var matcherPrecosProdutos = Matchers.contains(
                produto1.getPreco().intValue(), produto2.getPreco().intValue());

        var matchers = new Matcher[] {matcherTamanhoLista, matcherNomesProdutos, matcherPrecosProdutos};
        execAssertionsListaProdutos(matchers, uri);
        return true;
    }

    public boolean listaProdutosValorInvalido(boolean precoAbaixo) throws Exception {
        when(service.produtoPorPreco("[]", precoAbaixo)).thenThrow(QueryPrecoInvalidoException.class);
        String uri = "";

        if (precoAbaixo) {
            uri = URL_BASE + "/lista_preco_abaixo/[]";
        } else {
            uri = URL_BASE + "/lista_preco_acima/[]";
        }

        mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isBadRequest());
        return true;
    }

    public boolean listaProdutosPadraoVazia(boolean ordenada) throws Exception {
        List<Produto> produtos = List.of();
        String uri = obtemUrlDaListagem(ordenada, produtos);
        mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
        return true;
    }

    public boolean listaProdutosPadraoComProdutos(boolean ordenada) throws Exception {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(1000));
        var produto2 = new Produto("PaoDeForma", BigDecimal.valueOf(3000));
        var listaOrdenada = List.of(produto2, produto1);
        var listaPadrao = List.of(produto1, produto2);
        String uri = obtemUrlDaListagem(ordenada, ordenada ? listaOrdenada : listaPadrao);

        var matcherTamanhoLista = Matchers.hasSize(2);
        var matcherNomesCadastrados = Matchers.containsInAnyOrder(produto1.getNome(), produto2.getNome());
        var matcherPrecosCadastrados = Matchers.containsInAnyOrder(
                produto1.getPreco().intValue(), produto2.getPreco().intValue());
        var matchers = new Matcher[] {matcherTamanhoLista, matcherNomesCadastrados, matcherPrecosCadastrados};

        execAssertionsListaProdutos(matchers, uri);
        return true;
    }

    public boolean assertionsSomaPrecos(Matcher<?>[] matchers, String uri, String requisicaoString) throws Exception {
        assert uri != null;
        assert requisicaoString != null;
        assert !uri.isBlank();
        execAssertionsSomaPrecos(matchers, uri, requisicaoString);
        return true;
    }
}
