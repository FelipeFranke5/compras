package dev.franke.felipe.compras.compras.api.controller.helper.produto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.franke.felipe.compras.compras.api.model.Produto;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class ResponseAssertionHelper {

    private final MockMvc mockMvc;

    public ResponseAssertionHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public Matcher<?>[] matchersProdutos(List<Produto> produtos) {
        var matcherTamanhoLista = Matchers.hasSize(2);
        var matcherNomesProdutos =
                Matchers.contains(produtos.get(0).getNome(), produtos.get(1).getNome());
        var matcherPrecosProdutos = Matchers.contains(
                produtos.get(0).getPreco().intValue(),
                produtos.get(1).getPreco().intValue());
        return new Matcher[] {matcherTamanhoLista, matcherNomesProdutos, matcherPrecosProdutos};
    }

    public void execAssertionsPrecoInvalidoProduto(String uri) throws Exception {
        mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isBadRequest());
    }

    public void execAssertionsSomaPrecos(Matcher<?>[] matchers, String uri, String requisicaoString) throws Exception {
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

    public void execAssertionsListaProdutos(Matcher<?>[] matchers, String uri) throws Exception {
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

    public void execAssertionsListaProdutosVazia(String uri) throws Exception {
        mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
    }
}
