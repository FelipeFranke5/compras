package dev.franke.felipe.compras.compras.api.links;

import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.links.estaticos.CompradorLinkEstatico;
import dev.franke.felipe.compras.compras.api.mapper.CompradorMapper;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class CompradorLink {

    private static final CompradorMapper MAPPER = CompradorMapper.INSTANCIA;

    private CollectionModel<EntityModel<CompradorOUTDTO>> adicionaLinks(
            CollectionModel<EntityModel<CompradorOUTDTO>> listaOriginal, Link[] links) {
        assert listaOriginal != null;
        assert links != null;
        listaOriginal.add(links);
        return listaOriginal;
    }

    private EntityModel<CompradorOUTDTO> adicionaLinks(EntityModel<CompradorOUTDTO> comprador, Link[] links) {
        assert comprador != null;
        comprador.add(links);
        return comprador;
    }

    //

    public CollectionModel<EntityModel<CompradorOUTDTO>> preparaLista(
            List<Comprador> compradores, MetodoController metodoController) {
        assert metodoController != null;
        assert compradores != null;
        var colecao = CollectionModel.of(compradores.stream()
                .map(comprador -> preparaComprador(comprador, MetodoController.POR_ID))
                .toList());
        return adicionaLinks(colecao, CompradorLinkEstatico.linksSemId(metodoController));
    }

    @SuppressWarnings("null")
    public EntityModel<CompradorOUTDTO> preparaComprador(Comprador comprador, MetodoController metodoController) {
        var entidadeModelo = EntityModel.of(MAPPER.compradorParaCompradorOUTDTO(comprador));
        assert entidadeModelo.getContent() != null;
        return adicionaLinks(
                entidadeModelo,
                CompradorLinkEstatico.linksComId(
                        metodoController, entidadeModelo.getContent().getId()));
    }
}
