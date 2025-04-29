package dev.franke.felipe.compras.compras.api.links.estaticos.normal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import dev.franke.felipe.compras.compras.api.controller.CompradorController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import java.util.UUID;
import org.springframework.hateoas.Link;

public class CompradorNormal {

    private static final CompradorINDTO COMPRADOR_IN_DTO;

    static {
        COMPRADOR_IN_DTO = new CompradorINDTO();
        COMPRADOR_IN_DTO.setNome("Teste");
    }

    public static Link compradoresAtivos() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresAtivos())
                .withRel("compradores-ativos");
    }

    public static Link compradores() {
        return linkTo(methodOn(CompradorController.class).listaCompradores()).withRel("compradores");
    }

    public static Link compradoresNegativados() {
        return linkTo(methodOn(CompradorController.class).listaCompradoresNegativados())
                .withRel("compradores-negativados");
    }

    public static Link criaComprador() {
        return linkTo(methodOn(CompradorController.class).cadastra(COMPRADOR_IN_DTO))
                .withRel("criar-comprador");
    }

    public static Link comId(UUID id) {
        return linkTo(methodOn(CompradorController.class).porId(id.toString())).withRel("comprador-por-id");
    }

    public static Link alteraNome(String id) {
        return linkTo(methodOn(CompradorController.class).alteraNome(COMPRADOR_IN_DTO, id))
                .withRel("altera-nome");
    }

    public static Link alteraSaldoDebito(String id, String valor) {
        return linkTo(methodOn(CompradorController.class).atualizaSaldoDebito(id, valor))
                .withRel("altera-saldo-debito");
    }

    public static Link alteraSaldoValeAlimentacao(String id, String valor) {
        return linkTo(methodOn(CompradorController.class).atualizaSaldoValeAlimentacao(id, valor))
                .withRel("altera-saldo-vale-alimentacao");
    }

    public static Link alteraTotalCompras(String id, String valor) {
        return linkTo(methodOn(CompradorController.class).atualizaTotalCompras(id, valor))
                .withRel("altera-total-compras");
    }
}
