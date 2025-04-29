package dev.franke.felipe.compras.compras.api.links.estaticos.self;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import dev.franke.felipe.compras.compras.api.controller.CompradorController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import java.util.UUID;
import org.springframework.hateoas.Link;

public class CompradorSelf {

    private static final CompradorINDTO COMPRADOR_IN_DTO;

    static {
        COMPRADOR_IN_DTO = new CompradorINDTO();
        COMPRADOR_IN_DTO.setNome("Teste");
    }

    public static Link compradoresAtivosSelf() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresAtivos())
                .withSelfRel();
    }

    public static Link compradoresSelf() {
        return linkTo(methodOn(CompradorController.class).listaCompradores()).withSelfRel();
    }

    public static Link compradoresNegativadosSelf() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresNegativados())
                .withSelfRel();
    }

    public static Link criaCompradorSelf() {
        return linkTo(methodOn(CompradorController.class).cadastra(COMPRADOR_IN_DTO))
                .withSelfRel();
    }

    public static Link comIdSelf(UUID id) {
        return linkTo(methodOn(CompradorController.class).porId(id.toString())).withSelfRel();
    }

    public static Link alteraNomeSelf(String id) {
        return linkTo(methodOn(CompradorController.class).alteraNome(COMPRADOR_IN_DTO, id))
                .withSelfRel();
    }

    public static Link alteraSaldoDebitoSelf(String id, String valor) {
        return linkTo(methodOn(CompradorController.class).atualizaSaldoDebito(id, valor))
                .withSelfRel();
    }

    public static Link alteraSaldoValeAlimentacaoSelf(String id, String valor) {
        return linkTo(methodOn(CompradorController.class).atualizaSaldoValeAlimentacao(id, valor))
                .withSelfRel();
    }

    public static Link alteraTotalComprasSelf(String id, String valor) {
        return linkTo(methodOn(CompradorController.class).atualizaTotalCompras(id, valor))
                .withSelfRel();
    }
}
