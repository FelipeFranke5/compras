package dev.franke.felipe.compras.compras.api.controller.helper.produto;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.exception.QueryPrecoInvalidoException;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import dev.franke.felipe.compras.compras.api.service.lista_produtos.ResultadoSomaProdutos;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoServiceHelper {

    private final ProdutoService service;

    public ProdutoServiceHelper(ProdutoService service) {
        this.service = service;
    }

    public List<Produto> mockaListagemPadrao(boolean ordenada) {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(1000));
        var produto2 = new Produto("PaoDeForma", BigDecimal.valueOf(3000));
        var listaOrdenada = List.of(produto2, produto1);
        var listaPadrao = List.of(produto1, produto2);

        if (ordenada) {
            when(service.listaTodosProdutosOrdenada()).thenReturn(listaOrdenada);
            return listaOrdenada;
        } else {
            when(service.listaTodosProdutos()).thenReturn(listaPadrao);
            return listaPadrao;
        }
    }

    public void mockaProdutoPorPrecoListaVazia(boolean precoAbaixo) {
        List<Produto> listaVazia = List.of();
        when(service.produtoPorPreco("5000", precoAbaixo)).thenReturn(listaVazia);
    }

    public void mockaProdutoPorNomeExiste() {
        when(service.produtoExistePorNome("PaoDeForma")).thenReturn(true);
    }

    public void mockaProdutoPorNomeNaoExiste() {
        when(service.produtoExistePorNome("PaoDeForma")).thenReturn(false);
    }

    public List<Produto> mockaProdutoPorPrecoValorValido(boolean precoAbaixo) {
        var produto1 = new Produto("Açucar", BigDecimal.valueOf(3000));
        var produto2 = new Produto("PaoDeForma", BigDecimal.valueOf(2000));

        if (!precoAbaixo) {
            produto1.setPreco(BigDecimal.valueOf(7000));
            produto2.setPreco(BigDecimal.valueOf(10000));
        }

        List<Produto> produtos = List.of(produto1, produto2);
        when(service.produtoPorPreco("5000", precoAbaixo)).thenReturn(produtos);
        return produtos;
    }

    public void mockaProdutoPorPrecoValorInvalido(boolean precoAbaixo) {
        when(service.produtoPorPreco("[]", precoAbaixo)).thenThrow(QueryPrecoInvalidoException.class);
    }

    public void mockaResultadoSomaProdutos(ResultadoSomaProdutos resultadoSomaProdutos) {
        when(service.calculaTotalProdutos(anyList())).thenReturn(resultadoSomaProdutos);
    }

    public Object[] mockaSomaProdutosNenhumIdEncontrado() {
        var listaIds = List.of(1L, 2L, 3L);
        var soma = BigDecimal.valueOf(0);
        List<String> produtos = List.of();
        List<Object> idsInvalidos = List.of();
        List<Long> idsEncontrados = List.of();
        List<Long> idsNaoEncontrados = List.of(1L, 2L, 3L);

        var resultadoSomaProdutos =
                new ResultadoSomaProdutos(soma, produtos, listaIds, idsInvalidos, idsEncontrados, idsNaoEncontrados);

        mockaResultadoSomaProdutos(resultadoSomaProdutos);
        return new Object[] {listaIds, soma, produtos};
    }

    public Object[] mockaSomaProdutosIdsEncontrados() {
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

        mockaResultadoSomaProdutos(resultadoSomaProdutos);
        return new Object[] {listaIds, soma, produtos};
    }

    public Object[] mockaSomaProdutosMixProdutos() {
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

        mockaResultadoSomaProdutos(resultadoSomaProdutos);
        return new Object[] {listaIds, soma, produtos};
    }
}
