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
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.MockMvc;

public class ProdutoControllerHelper {

    private static final String URL_BASE = "/api/v1/produto";

    private MockMvc mockMvc;
    private ProdutoService service;

    public ProdutoControllerHelper(MockMvc mockMvc, ProdutoService produtoService) {
        this.mockMvc = mockMvc;
        this.service = produtoService;
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
        List<Produto> produtos = null;
        String uri = "";

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

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.produtos", matcherTamanhoLista))
                .andExpect(jsonPath("$._embedded.produtos[*].nomeProduto", matcherNomesProdutos))
                .andExpect(jsonPath("$._embedded.produtos[*].precoProduto", matcherPrecosProdutos));

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
        String uri = "";

        if (ordenada) {
            uri = URL_BASE + "/lista_ordenada";
            when(service.listaTodosProdutos()).thenReturn(produtos);
        } else {
            uri = URL_BASE + "/lista_padrao";
            when(service.listaTodosProdutosOrdenada()).thenReturn(produtos);
        }

        mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
        return true;
    }
}
