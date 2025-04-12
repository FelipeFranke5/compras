package dev.franke.felipe.compras.compras.api.service.lista_produtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "somaProdutosLista", itemRelation = "somaProdutos")
public record ResultadoSomaProdutos(
        BigDecimal soma,
        ArrayList<String> produtos,
        ArrayList<Long> idsValidos,
        ArrayList<Object> idsInvalidos,
        ArrayList<Long> idsEncontrados,
        ArrayList<Long> idsNaoEncontrados) {}
