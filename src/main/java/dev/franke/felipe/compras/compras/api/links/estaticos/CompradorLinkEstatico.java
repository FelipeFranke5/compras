package dev.franke.felipe.compras.compras.api.links.estaticos;

import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.links.estaticos.normal.CompradorNormal;
import dev.franke.felipe.compras.compras.api.links.estaticos.self.CompradorSelf;
import java.util.UUID;
import org.springframework.hateoas.Link;

public class CompradorLinkEstatico {

    private CompradorLinkEstatico() {}

    public static Link selfLink(MetodoController metodoController) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> CompradorSelf.compradoresNegativadosSelf();
            case LISTA_COMPRADORES_ATIVOS -> CompradorSelf.compradoresAtivosSelf();
            case CADASTRO -> CompradorSelf.criaCompradorSelf();
            case ALTERACAO_NOME -> CompradorSelf.alteraNomeSelf("id");
            case ALTERACAO_VALOR_DEBITO -> CompradorSelf.alteraSaldoDebitoSelf("id", "0");
            case ALTERACAO_VALOR_VALE_ALIMENTACAO -> CompradorSelf.alteraSaldoValeAlimentacaoSelf("id", "0");
            case ALTERACAO_TOTAL_COMPRAS -> CompradorSelf.alteraTotalComprasSelf("id", "1");
            case DEFAULT -> CompradorSelf.compradoresSelf();
            default -> CompradorSelf.compradoresSelf();
        };
    }

    public static Link selfLink(MetodoController metodoController, UUID id) {
        return switch (metodoController) {
            case LISTA_NEGATIVADOS -> CompradorSelf.compradoresNegativadosSelf();
            case LISTA_COMPRADORES_ATIVOS -> CompradorSelf.compradoresAtivosSelf();
            case CADASTRO -> CompradorSelf.criaCompradorSelf();
            case ALTERACAO_NOME -> CompradorSelf.alteraNomeSelf(id.toString());
            case ALTERACAO_VALOR_DEBITO -> CompradorSelf.alteraSaldoDebitoSelf(id.toString(), "0");
            case ALTERACAO_VALOR_VALE_ALIMENTACAO -> CompradorSelf.alteraSaldoValeAlimentacaoSelf(id.toString(), "0");
            case ALTERACAO_TOTAL_COMPRAS -> CompradorSelf.alteraTotalComprasSelf(id.toString(), "1");
            case POR_ID -> CompradorSelf.comIdSelf(id);
            case DEFAULT -> CompradorSelf.compradoresSelf();
            default -> CompradorSelf.compradoresSelf();
        };
    }

    public static Link[] linksSemId(MetodoController metodoController) {
        return new Link[] {
            selfLink(metodoController),
            CompradorNormal.compradores(),
            CompradorNormal.compradoresAtivos(),
            CompradorNormal.compradoresNegativados(),
            CompradorNormal.criaComprador()
        };
    }

    public static Link[] linksComId(MetodoController metodoController, UUID id) {
        return new Link[] {
            selfLink(metodoController, id),
            CompradorNormal.compradores(),
            CompradorNormal.compradoresAtivos(),
            CompradorNormal.compradoresNegativados(),
            CompradorNormal.criaComprador(),
            CompradorNormal.comId(id),
            CompradorNormal.alteraNome(id.toString()),
            CompradorNormal.alteraSaldoDebito(id.toString(), "1234"),
            CompradorNormal.alteraSaldoValeAlimentacao(id.toString(), "1234"),
            CompradorNormal.alteraTotalCompras(id.toString(), "1")
        };
    }
}
