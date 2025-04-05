package dev.franke.felipe.compras.compras.api.controller;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.ProdutoOUTDTO;
import dev.franke.felipe.compras.compras.api.mapper.ProdutoMapper;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cadastro")
    public ResponseEntity<ProdutoOUTDTO> salvaProduto(@RequestBody ProdutoINDTO requisicao) {
        var produtoSalvo = this.produtoService.cadastraProduto(requisicao);
        return ResponseEntity.status(HttpStatus.CREATED).body(MAPPER.produtoParaProdutoOUTDTO(produtoSalvo));
    }

    @PutMapping("/alteracao/{id}")
    public ResponseEntity<ProdutoOUTDTO> alteraProduto(@PathVariable Long id, @RequestBody ProdutoINDTO requisicao) {
        var produto = this.produtoService.produtoPorId(id);
        var produtoSalvo = this.produtoService.alteraProduto(produto, requisicao);
        return ResponseEntity.ok(MAPPER.produtoParaProdutoOUTDTO(produtoSalvo));
    }

    @DeleteMapping("/delecao/{id}")
    public ResponseEntity<Void> deletaProduto(@PathVariable Long id) {
        var produto = this.produtoService.produtoPorId(id);
        this.produtoService.removeProduto(produto);
        return ResponseEntity.noContent().build();
    }

}
