package dev.franke.felipe.compras.compras.api.links;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import dev.franke.felipe.compras.compras.api.controller.CompradorController;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
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

    private static CompradorINDTO compradorINDTO;

    static {
        compradorINDTO = new CompradorINDTO();
        compradorINDTO.setNome("Teste");
    }

    private static Link compradoresAtivosSelf() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresAtivos())
                .withSelfRel();
    }

    private static Link compradoresSelf() {
        return linkTo(methodOn(CompradorController.class).listaCompradores()).withSelfRel();
    }

    private static Link compradoresNegativadosSelf() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresNegativados())
                .withSelfRel();
    }

    private static Link criaCompradorSelf() {
        return linkTo(methodOn(CompradorController.class).cadastra(compradorINDTO))
                .withSelfRel();
    }

    private static Link compradoresAtivos() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresAtivos())
                .withRel("compradores-ativos");
    }

    private static Link compradores() {
        return linkTo(methodOn(CompradorController.class).listaCompradores()).withRel("compradores");
    }

    private static Link compradoresNegativados() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresNegativados())
                .withRel("compradores-negativados");
    }

    private static Link criaComprador() {
        return linkTo(methodOn(CompradorController.class).cadastra(compradorINDTO))
                .withRel("criar-comprador");
    }

    private static Link comIdSelf(UUID id) {
        return linkTo(methodOn(CompradorController.class).porId(id.toString())).withSelfRel();
    }

    private static Link comId(UUID id) {
        return linkTo(methodOn(CompradorController.class).porId(id.toString())).withRel("comprador-por-id");
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
            case LISTA_NEGATIVADOS -> compradoresNegativadosSelf();
            case LISTA_COMPRADORES_ATIVOS -> compradoresAtivosSelf();
            case CADASTRO -> criaCompradorSelf();
            case DEFAULT -> compradoresSelf();
            default -> compradoresSelf();
        };
    }

    public Link selfLink(MetodoController metodoController, UUID id) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> compradoresNegativadosSelf();
            case LISTA_COMPRADORES_ATIVOS -> compradoresAtivosSelf();
            case CADASTRO -> criaCompradorSelf();
            case POR_ID -> comIdSelf(id);
            case DEFAULT -> compradoresSelf();
            default -> compradoresSelf();
        };
    }

    public Link[] linksSemId(MetodoController metodoController) {
        return new Link[] {
            selfLink(metodoController), compradoresAtivos(), compradores(), compradoresNegativados(), criaComprador()
        };
    }

    public Link[] linksComId(MetodoController metodoController, UUID id) {
        return new Link[] {
            selfLink(metodoController, id),
            compradoresAtivos(),
            compradores(),
            compradoresNegativados(),
            criaComprador(),
            comId(id)
        };
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
