package dev.franke.felipe.compras.compras.api.links;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import dev.franke.felipe.compras.compras.api.controller.ProdutoController;
import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.ProdutoOUTDTO;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ProdutoLink {

    private ProdutoINDTO obtemRequisicaoExemplo() {
        var requisicaoExemplo = new ProdutoINDTO();
        requisicaoExemplo.setNomeProduto("Nome Produto");
        requisicaoExemplo.setPrecoProduto(new BigDecimal(100));
        return requisicaoExemplo;
    }

    public List<Link> linksSemId(String nome) {
        var requisicaoExemplo = this.obtemRequisicaoExemplo();
        var id = ThreadLocalRandom.current().nextLong(0, 20);
        return List.of(
                this.selfLink(nome, false),
                linkTo(methodOn(ProdutoController.class).existePorNome("nome")).withRel("existe-por-nome"),
                linkTo(methodOn(ProdutoController.class).listaOrdenada()).withRel("lista-ordenada"),
                linkTo(methodOn(ProdutoController.class).produtoPorId(id)).withRel("produto-por-id"),
                linkTo(methodOn(ProdutoController.class).produtoPorNome("nome")).withRel("produto-por-nome"),
                linkTo(methodOn(ProdutoController.class).salvaProduto(requisicaoExemplo))
                        .withRel("salva-produto"),
                linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicaoExemplo))
                        .withRel("altera-produto"),
                linkTo(methodOn(ProdutoController.class).deletaProduto(id)).withRel("deleta-produto"));
    }

    public List<Link> linksSemId(String metodo, List<Object> ids) {
        if (ids == null) {
            ids = List.of();
        }
        var requisicaoExemplo = this.obtemRequisicaoExemplo();
        var id = ThreadLocalRandom.current().nextLong(0, 20);
        return List.of(
                this.selfLink(metodo, id, ids, null),
                linkTo(methodOn(ProdutoController.class).existePorNome("nome")).withRel("existe-por-nome"),
                linkTo(methodOn(ProdutoController.class).listaOrdenada()).withRel("lista-ordenada"),
                linkTo(methodOn(ProdutoController.class).produtoPorId(id)).withRel("produto-por-id"),
                linkTo(methodOn(ProdutoController.class).produtoPorNome("nome")).withRel("produto-por-nome"),
                linkTo(methodOn(ProdutoController.class).salvaProduto(requisicaoExemplo))
                        .withRel("salva-produto"),
                linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicaoExemplo))
                        .withRel("altera-produto"),
                linkTo(methodOn(ProdutoController.class).deletaProduto(id)).withRel("deleta-produto"));
    }

    public List<Link> linksComId(String metodo, ProdutoOUTDTO produto) {
        var requisicaoExemplo = this.obtemRequisicaoExemplo();
        var ids = List.of(produto.getIdProduto());
        return List.of(
                this.selfLink(metodo, produto.getIdProduto(), Collections.singletonList(ids), requisicaoExemplo),
                linkTo(methodOn(ProdutoController.class).existePorNome(produto.getNomeProduto()))
                        .withRel("existe-por-nome"),
                linkTo(methodOn(ProdutoController.class).listaOrdenada()).withRel("lista-ordenada"),
                linkTo(methodOn(ProdutoController.class).produtoPorId(produto.getIdProduto()))
                        .withRel("produto-por-id"),
                linkTo(methodOn(ProdutoController.class).produtoPorNome(produto.getNomeProduto()))
                        .withRel("produto-por-nome"),
                linkTo(methodOn(ProdutoController.class).salvaProduto(requisicaoExemplo))
                        .withRel("salva-produto"),
                linkTo(methodOn(ProdutoController.class).alteraProduto(produto.getIdProduto(), requisicaoExemplo))
                        .withRel("altera-produto"),
                linkTo(methodOn(ProdutoController.class).deletaProduto(produto.getIdProduto()))
                        .withRel("deleta-produto"));
    }

    public List<Link> linksComId(String nome, Long id) {
        var requisicaoExemplo = this.obtemRequisicaoExemplo();
        return List.of(
                this.selfLink(nome, true),
                linkTo(methodOn(ProdutoController.class).existePorNome(nome)).withRel("existe-por-nome"),
                linkTo(methodOn(ProdutoController.class).listaOrdenada()).withRel("lista-ordenada"),
                linkTo(methodOn(ProdutoController.class).produtoPorId(id)).withRel("produto-por-id"),
                linkTo(methodOn(ProdutoController.class).produtoPorNome(nome)).withRel("produto-por-nome"),
                linkTo(methodOn(ProdutoController.class).salvaProduto(requisicaoExemplo))
                        .withRel("salva-produto"),
                linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicaoExemplo))
                        .withRel("altera-produto"),
                linkTo(methodOn(ProdutoController.class).deletaProduto(id)).withRel("deleta-produto"));
    }

    public Link selfLink(String metodo, Long id, List<Object> ids, ProdutoINDTO requisicao) {
        return switch (metodo) {
            case "somaPrecos" -> linkTo(methodOn(ProdutoController.class).somaPrecos(ids))
                    .withSelfRel();
            case "listaProdutosPrecoAcima" -> linkTo(
                            methodOn(ProdutoController.class).listaProdutosPrecoAcima(500))
                    .withSelfRel();
            case "listaProdutosPrecoAbaixo" -> linkTo(
                            methodOn(ProdutoController.class).listaProdutosPrecoAbaixo(500))
                    .withSelfRel();
            case "listaOrdenada" -> linkTo(methodOn(ProdutoController.class).listaOrdenada())
                    .withSelfRel();
            case "produtoPorId" -> linkTo(methodOn(ProdutoController.class).produtoPorId(id))
                    .withSelfRel();
            case "salvaProduto" -> linkTo(methodOn(ProdutoController.class).salvaProduto(requisicao))
                    .withSelfRel();
            case "alteraProduto" -> linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicao))
                    .withSelfRel();
            case "deletaProduto" -> linkTo(methodOn(ProdutoController.class).deletaProduto(id))
                    .withSelfRel();
            default -> linkTo(methodOn(ProdutoController.class).listaProdutos()).withSelfRel();
        };
    }

    public Link selfLink(String nome, boolean produtoPorNome) {
        if (produtoPorNome) {
            return linkTo(methodOn(ProdutoController.class).produtoPorNome(nome))
                    .withSelfRel();
        }
        return linkTo(methodOn(ProdutoController.class).existePorNome(nome)).withSelfRel();
    }
}
