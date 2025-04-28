package dev.franke.felipe.compras.compras.api.controller;

import dev.franke.felipe.compras.compras.api.controller.helper.comprador.MetodoController;
import dev.franke.felipe.compras.compras.api.controller.helper.comprador.TipoOperacaoController;
import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.links.CompradorLink;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import dev.franke.felipe.compras.compras.api.service.CompradorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comprador")
@CrossOrigin
@RequiredArgsConstructor
public class CompradorController {

    private final CompradorService compradorService;
    private final CompradorLink compradorLink;

    private ResponseEntity<CollectionModel<CompradorOUTDTO>> lista(
            MetodoController metodoController, List<Comprador> compradores) {
        return ResponseEntity.ok().body(compradorLink.preparaLista(compradores, metodoController));
    }

    private ResponseEntity<EntityModel<CompradorOUTDTO>> comprador(
            MetodoController metodoController, Comprador comprador, TipoOperacaoController tipoOperacaoController) {
        return switch (tipoOperacaoController) {
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
}
