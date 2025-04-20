package dev.franke.felipe.compras.compras.api.service.lista_produtos;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "somaProdutosLista", itemRelation = "somaProdutos")
public record ResultadoSomaProdutos(
        BigDecimal soma,
        List<String> produtos,
        List<Long> idsValidos,
        List<Object> idsInvalidos,
        List<Long> idsEncontrados,
        List<Long> idsNaoEncontrados) {}
