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

    private CollectionModel<CompradorOUTDTO> adicionaLinks(
            CollectionModel<CompradorOUTDTO> listaOriginal, Link[] links) {
        var listaModificada = listaOriginal;
        listaModificada.add(links);
        return listaModificada;
    }

    private EntityModel<CompradorOUTDTO> adicionaLinks(EntityModel<CompradorOUTDTO> comprador, Link[] links) {
        comprador.add(links);
        return comprador;
    }

    public CollectionModel<CompradorOUTDTO> preparaLista(
            List<Comprador> compradores, MetodoController metodoController) {
        var colecao = CollectionModel.of(
                compradores.stream().map(MAPPER::compradorParaCompradorOUTDTO).toList());
        return adicionaLinks(colecao, CompradorLinkEstatico.linksSemId(metodoController));
    }

    @SuppressWarnings("null")
    public EntityModel<CompradorOUTDTO> preparaComprador(Comprador comprador, MetodoController metodoController) {
        var entidadeModelo = EntityModel.of(MAPPER.compradorParaCompradorOUTDTO(comprador));
        return adicionaLinks(
                entidadeModelo,
                CompradorLinkEstatico.linksComId(
                        metodoController, entidadeModelo.getContent().getId()));
    }
}
