package dev.franke.felipe.compras.compras.api.controller;

import dev.franke.felipe.compras.compras.api.controller.helper.comprador.CompradorResponseUtil;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.ListaUtil;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.NumberUtils;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.TipoOperacaoController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.links.CompradorLink;
import dev.franke.felipe.compras.compras.api.service.CompradorService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(
        path = "/api/v1/comprador",
        produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
@CrossOrigin
@RequiredArgsConstructor
public class CompradorController {

    private final CompradorService compradorService;
    private final CompradorLink compradorLink;

    @GetMapping(path = "/lista")
    public ResponseEntity<CollectionModel<EntityModel<CompradorOUTDTO>>> listaCompradores() {
        return ListaUtil.lista(MetodoController.DEFAULT, compradorService.listaCompradores(), compradorLink);
    }

    @GetMapping(path = "/lista_ativos")
    public ResponseEntity<CollectionModel<EntityModel<CompradorOUTDTO>>> listaCompradoresAtivos() {
        return ListaUtil.lista(
                MetodoController.LISTA_COMPRADORES_ATIVOS, compradorService.listaCompradoresAtivos(), compradorLink);
    }

    @GetMapping(path = "/lista_negativados")
    public ResponseEntity<CollectionModel<EntityModel<CompradorOUTDTO>>> listaCompradoresNegativados() {
        return ListaUtil.lista(
                MetodoController.LISTA_NEGATIVADOS, compradorService.listaCompradoresNegativados(), compradorLink);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> porId(@PathVariable String id) {
        return CompradorResponseUtil.comprador(
                MetodoController.POR_ID, compradorService.porId(id), TipoOperacaoController.CONSULTA, compradorLink);
    }

    @PostMapping(path = "/cadastra")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> cadastra(@RequestBody CompradorINDTO requisicao) {
        return CompradorResponseUtil.comprador(
                MetodoController.CADASTRO,
                compradorService.cadastra(requisicao),
                TipoOperacaoController.CRIACAO,
                compradorLink);
    }

    @PutMapping(path = "/altera_nome/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> alteraNome(
            @RequestBody CompradorINDTO requisicao, @PathVariable String id) {
        return CompradorResponseUtil.comprador(
                MetodoController.ALTERACAO_NOME,
                compradorService.atualizaNome(compradorService.porId(id), requisicao),
                TipoOperacaoController.ALTERACAO,
                compradorLink);
    }

    @PatchMapping(path = "/altera_saldo_debito/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> atualizaSaldoDebito(
            @PathVariable String id, @RequestParam("valor") String valor) {
        BigDecimal valorBigDecimal = null;
        valorBigDecimal = NumberUtils.tentarFloat(valor);
        return CompradorResponseUtil.comprador(
                MetodoController.ALTERACAO_VALOR_DEBITO,
                compradorService.atualizaSaldoDebito(compradorService.porId(id), valorBigDecimal),
                TipoOperacaoController.ALTERACAO,
                compradorLink);
    }

    @PatchMapping(path = "/altera_saldo_va/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> atualizaSaldoValeAlimentacao(
            @PathVariable String id, @RequestParam("valor") String valor) {
        BigDecimal valorBigDecimal = null;
        valorBigDecimal = NumberUtils.tentarFloat(valor);
        return CompradorResponseUtil.comprador(
                MetodoController.ALTERACAO_VALOR_VALE_ALIMENTACAO,
                compradorService.atualizaSaldoValeAlimentacao(compradorService.porId(id), valorBigDecimal),
                TipoOperacaoController.ALTERACAO,
                compradorLink);
    }

    @PatchMapping(path = "/atualiza_total_compras/{id}")
    public ResponseEntity<EntityModel<CompradorOUTDTO>> atualizaTotalCompras(
            @PathVariable String id, @RequestParam("valor") String valor) {
        int valorInt = NumberUtils.tentarInt(valor);
        return CompradorResponseUtil.comprador(
                MetodoController.ALTERACAO_TOTAL_COMPRAS,
                compradorService.atualizaTotalCompras(compradorService.porId(id), valorInt),
                TipoOperacaoController.ALTERACAO,
                compradorLink);
    }

    @DeleteMapping(path = "/apaga/{id}")
    public ResponseEntity<Void> apaga(@PathVariable String id) {
        compradorService.apaga(compradorService.porId(id));
        return ResponseEntity.noContent().build();
    }
}
