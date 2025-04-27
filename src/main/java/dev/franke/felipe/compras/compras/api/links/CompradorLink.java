package dev.franke.felipe.compras.compras.api.links;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import dev.franke.felipe.compras.compras.api.controller.CompradorController;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.mapper.CompradorMapper;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import java.util.List;
import java.util.UUID;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class CompradorLink {

    private static final CompradorMapper MAPPER = CompradorMapper.INSTANCIA;

    private static final Link COMPRADORES_ATIVOS_SELF =
            linkTo(methodOn(CompradorController.class).listaCompradoresAtivos()).withSelfRel();
    private static final Link COMPRADORES_SELF =
            linkTo(methodOn(CompradorController.class).listaCompradores()).withSelfRel();
    private static final Link COMPRADORES_NEGATIVADOS_SELF = linkTo(
                    methodOn(CompradorController.class).listaCompradoresNegativados())
            .withSelfRel();
    private static final Link COMPRADORES_ATIVOS =
            linkTo(methodOn(CompradorController.class).listaCompradoresAtivos()).withRel("compradores-ativos");
    private static final Link COMPRADORES =
            linkTo(methodOn(CompradorController.class).listaCompradores()).withRel("compradores");
    private static final Link COMPRADORES_NEGATIVADOS = linkTo(
                    methodOn(CompradorController.class).listaCompradoresNegativados())
            .withRel("compradores-negativados");

    private static Link comIdSelf(UUID id) {
        return linkTo(methodOn(CompradorController.class).porId(id.toString())).withSelfRel();
    }

    //

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

    //

    public Link selfLink(MetodoController metodoController) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> COMPRADORES_NEGATIVADOS_SELF;
            case LISTA_COMPRADORES_ATIVOS -> COMPRADORES_ATIVOS_SELF;
            case DEFAULT -> COMPRADORES_SELF;
            default -> COMPRADORES_SELF;
        };
    }

    public Link selfLink(MetodoController metodoController, UUID id) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> COMPRADORES_NEGATIVADOS_SELF;
            case LISTA_COMPRADORES_ATIVOS -> COMPRADORES_ATIVOS_SELF;
            case POR_ID -> comIdSelf(id);
            case DEFAULT -> COMPRADORES_SELF;
            default -> COMPRADORES_SELF;
        };
    }

    public Link[] linksSemId(MetodoController metodoController) {
        return new Link[] {selfLink(metodoController), COMPRADORES_ATIVOS, COMPRADORES, COMPRADORES_NEGATIVADOS};
    }

    public Link[] linksComId(MetodoController metodoController, UUID id) {
        return new Link[] {selfLink(metodoController, id), COMPRADORES_ATIVOS, COMPRADORES, COMPRADORES_NEGATIVADOS};
    }

    public CollectionModel<CompradorOUTDTO> preparaLista(
            List<Comprador> compradores, MetodoController metodoController) {
        var colecao = CollectionModel.of(
                compradores.stream().map(MAPPER::compradorParaCompradorOUTDTO).toList());
        return adicionaLinks(colecao, linksSemId(metodoController));
    }

    @SuppressWarnings("null")
    public EntityModel<CompradorOUTDTO> preparaComprador(Comprador comprador, MetodoController metodoController) {
        var entidadeModelo = EntityModel.of(MAPPER.compradorParaCompradorOUTDTO(comprador));
        return adicionaLinks(
                entidadeModelo,
                linksComId(metodoController, entidadeModelo.getContent().getId()));
    }
}
