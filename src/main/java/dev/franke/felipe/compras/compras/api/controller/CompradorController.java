package dev.franke.felipe.compras.compras.api.controller;

import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.TipoOperacaoController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoInvalidoException;
import dev.franke.felipe.compras.compras.api.links.CompradorLink;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import dev.franke.felipe.compras.compras.api.service.CompradorService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comprador")
@CrossOrigin
@RequiredArgsConstructor
public class CompradorController {

    private final CompradorService compradorService;
    private final CompradorLink compradorLink;

    private BigDecimal valorValido(String valor) {
        return tentarFloat(valor);
    }

    private BigDecimal tentarFloat(String valor) {
        try {
            return BigDecimal.valueOf(Float.parseFloat(valor));
        } catch (NumberFormatException formataNumeroException) {
            return tentarLong(valor);
        }
    }

    private BigDecimal tentarLong(String valor) {
        try {
            return BigDecimal.valueOf(Long.parseLong(valor));
        } catch (NumberFormatException formataNumeroException) {
            throw new ValorProdutoInvalidoException("O valor nao e valido");
        }
    }

    private ResponseEntity<CollectionModel<CompradorOUTDTO>> lista(
            MetodoController metodoController, List<Comprador> compradores) {
        return ResponseEntity.ok().body(compradorLink.preparaLista(compradores, metodoController));
    }

    private ResponseEntity<EntityModel<CompradorOUTDTO>> comprador(
            MetodoController metodoController, Comprador comprador, TipoOperacaoController tipoOperacaoController) {
        return switch (tipoOperacaoController) {
            case ALTERACAO -> ResponseEntity.ok().body(compradorLink.preparaComprador(comprador, metodoController));
            case CONSULTA -> ResponseEntity.ok().body(compradorLink.preparaComprador(comprador, metodoController));
            case CRIACAO -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(compradorLink.preparaComprador(comprador, metodoController));
            default -> ResponseEntity.ok().body(compradorLink.preparaComprador(comprador, metodoController));
        };
    }

    //

    @GetMapping("/lista")
    public ResponseEntity<CollectionModel<CompradorOUTDTO>> listaCompradores() {
        return lista(MetodoController.DEFAULT, compradorService.listaCompradores());
    }

    @GetMapping("/lista_ativos")
    public ResponseEntity<CollectionModel<CompradorOUTDTO>> listaCompradoresAtivos() {
        return lista(MetodoController.LISTA_COMPRADORES_ATIVOS, compradorService.listaCompradoresAtivos());
    }

    @GetMapping("/lista_negativados")
    public ResponseEntity<CollectionModel<CompradorOUTDTO>> listaCompradoresNegativados() {
        return lista(MetodoController.LISTA_NEGATIVADOS, compradorService.listaCompradoresNegativados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> porId(@PathVariable String id) {
        return comprador(MetodoController.POR_ID, compradorService.porId(id), TipoOperacaoController.CONSULTA);
    }

    @PostMapping("/cadastra")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> cadastra(@RequestBody CompradorINDTO requisicao) {
        return comprador(
                MetodoController.CADASTRO, compradorService.cadastra(requisicao), TipoOperacaoController.CRIACAO);
    }

    @PutMapping("/altera_nome/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> alteraNome(
            @RequestBody CompradorINDTO requisicao, @PathVariable String id) {
        return comprador(
                MetodoController.ALTERACAO_NOME,
                compradorService.atualizaNome(compradorService.porId(id), requisicao),
                TipoOperacaoController.ALTERACAO);
    }

    @PatchMapping("/altera_saldo_debito/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> atualizaSaldoDebito(
            @PathVariable String id, @RequestParam("valor") String valor) {
        BigDecimal valorBigDecimal = null;
        valorBigDecimal = valorValido(valor);
        return comprador(
                MetodoController.ALTERACAO_VALOR_DEBITO,
                compradorService.atualizaSaldoDebito(compradorService.porId(id), valorBigDecimal),
                TipoOperacaoController.ALTERACAO);
    }

    @PatchMapping("/altera_saldo_va/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> atualizaSaldoValeAlimentacao(
            @PathVariable String id, @RequestParam("valor") String valor) {
        BigDecimal valorBigDecimal = null;
        valorBigDecimal = valorValido(valor);
        return comprador(
                MetodoController.ALTERACAO_VALOR_VALE_ALIMENTACAO,
                compradorService.atualizaSaldoValeAlimentacao(compradorService.porId(id), valorBigDecimal),
                TipoOperacaoController.ALTERACAO);
    }
}
