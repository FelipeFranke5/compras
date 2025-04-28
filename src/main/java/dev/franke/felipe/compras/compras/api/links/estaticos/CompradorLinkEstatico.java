package dev.franke.felipe.compras.compras.api.links.estaticos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import dev.franke.felipe.compras.compras.api.controller.CompradorController;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import java.util.UUID;
import org.springframework.hateoas.Link;

public class CompradorLinkEstatico {

    private CompradorLinkEstatico() {}

    public static final CompradorINDTO COMPRADOR_IN_DTO;

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

    public static Link selfLink(MetodoController metodoController) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> compradoresNegativadosSelf();
            case LISTA_COMPRADORES_ATIVOS -> compradoresAtivosSelf();
            case CADASTRO -> criaCompradorSelf();
            case ALTERACAO_NOME -> alteraNomeSelf("id");
            case ALTERACAO_VALOR_DEBITO -> alteraSaldoDebitoSelf("id", "0");
            case ALTERACAO_VALOR_VALE_ALIMENTACAO -> alteraSaldoValeAlimentacaoSelf("id", "0");
            case DEFAULT -> compradoresSelf();
            default -> compradoresSelf();
        };
    }

    public static Link selfLink(MetodoController metodoController, UUID id) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> compradoresNegativadosSelf();
            case LISTA_COMPRADORES_ATIVOS -> compradoresAtivosSelf();
            case CADASTRO -> criaCompradorSelf();
            case ALTERACAO_NOME -> alteraNomeSelf(id.toString());
            case ALTERACAO_VALOR_DEBITO -> alteraSaldoDebitoSelf(id.toString(), "0");
            case ALTERACAO_VALOR_VALE_ALIMENTACAO -> alteraSaldoValeAlimentacaoSelf(id.toString(), "0");
            case POR_ID -> comIdSelf(id);
            case DEFAULT -> compradoresSelf();
            default -> compradoresSelf();
        };
    }

    public static Link[] linksSemId(MetodoController metodoController) {
        return new Link[] {
            selfLink(metodoController), compradores(), compradoresAtivos(), compradoresNegativados(), criaComprador()
        };
    }

    public static Link[] linksComId(MetodoController metodoController, UUID id) {
        return new Link[] {
            selfLink(metodoController, id),
            compradores(),
            compradoresAtivos(),
            compradoresNegativados(),
            criaComprador(),
            comId(id),
            alteraNome(id.toString()),
            alteraSaldoDebito(id.toString(), "1234"),
            alteraSaldoValeAlimentacao(id.toString(), "1234")
        };
    }
}
