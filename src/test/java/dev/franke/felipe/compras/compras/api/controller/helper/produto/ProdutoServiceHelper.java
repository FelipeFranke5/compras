package dev.franke.felipe.compras.compras.api.controller.helper.produto;

import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.exception.QueryPrecoInvalidoException;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
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
}
