package dev.franke.felipe.compras.compras.api.controller;

import dev.franke.felipe.compras.compras.api.dto.out.ProdutoOUTDTO;
import dev.franke.felipe.compras.compras.api.mapper.ProdutoMapper;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/produto")
public class ProdutoController {

    public static final ProdutoMapper MAPPER = ProdutoMapper.INSTANCIA;

    private final ProdutoService produtoService;

    @GetMapping("/lista_padrao")
    public ResponseEntity<List<ProdutoOUTDTO>> listaProdutos() {
        var lista = this.produtoService.listaTodosProdutos()
                .stream()
                .map(MAPPER::produtoParaProdutoOUTDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/lista_ordenada")
    public ResponseEntity<List<ProdutoOUTDTO>> listaOrdenada() {
        var lista = this.produtoService.listaTodosProdutosOrdenada()
                .stream()
                .map(MAPPER::produtoParaProdutoOUTDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoOUTDTO> produtoPorId(@PathVariable Long id) {
        var produto = this.produtoService.produtoPorId(id);
        return ResponseEntity.ok(MAPPER.produtoParaProdutoOUTDTO(produto));
    }

    // WIP: Outras funções do Service
    // TODO: Outras funções do Service

}
