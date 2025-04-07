package dev.franke.felipe.compras.compras.api.controller;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.ProdutoOUTDTO;
import dev.franke.felipe.compras.compras.api.mapper.ProdutoMapper;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.service.ProdutoService;
import dev.franke.felipe.compras.compras.api.service.lista_produtos.ResultadoSomaProdutos;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/produto")
@CrossOrigin
public class ProdutoController {

    public static final ProdutoMapper MAPPER = ProdutoMapper.INSTANCIA;
    public static final Logger LOGGER = LogManager.getLogger();

    private final ProdutoService produtoService;

    @GetMapping("/soma_preco_produtos")
    public ResponseEntity<EntityModel<ResultadoSomaProdutos>> somaPrecos(@RequestBody List<Object> ids) {
        LOGGER.info("Requisicao para soma total de produtos recebida");
        var resultado = this.produtoService.calculaTotalProdutos(ids);
        var modelo = EntityModel.of(resultado);
        List<Link> links = this.linksSemId("somaPrecos", ids);
        modelo.add(links);
        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/lista_padrao")
    public ResponseEntity<CollectionModel<ProdutoOUTDTO>> listaProdutos() {
        LOGGER.info("Requisicao para listagem padrao recebida");
        return this.obtemLista(this.produtoService.listaTodosProdutos(), "listaProdutos");
    }

    @GetMapping("/lista_ordenada")
    public ResponseEntity<CollectionModel<ProdutoOUTDTO>> listaOrdenada() {
        LOGGER.info("Requisicao para listagem ordenada recebida");
        return this.obtemLista(this.produtoService.listaTodosProdutosOrdenada(), "listaOrdenada");
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<ProdutoOUTDTO>> produtoPorId(@PathVariable Long id) {
        LOGGER.info("Requisicao para pesquisar produto pelo ID {} recebida", id);
        LOGGER.info("Buscando um produto com o ID {}", id);
        var produto = this.produtoService.produtoPorId(id);
        LOGGER.info("Produto com id {} resgatado", id);
        var modelo = EntityModel.of(MAPPER.produtoParaProdutoOUTDTO(produto));
        List<Link> links = this.linksComId(id, "produtoPorId", null);
        modelo.add(links);
        return ResponseEntity.ok(modelo);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<EntityModel<ProdutoOUTDTO>> salvaProduto(@RequestBody ProdutoINDTO requisicao) {
        LOGGER.info("Requisicao para cadastrar produto recebida");
        LOGGER.info("Tentando armazenar o produto");
        var produtoSalvo = this.produtoService.cadastraProduto(requisicao);
        LOGGER.info("Produto armazenado com sucesso");
        var modelo = EntityModel.of(MAPPER.produtoParaProdutoOUTDTO(produtoSalvo));
        List<Link> links = this.linksComId(Objects.requireNonNull(modelo
                .getContent()).getIdProduto(), "salvaProduto", requisicao);
        modelo.add(links);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/alteracao/{id}")
    public ResponseEntity<EntityModel<ProdutoOUTDTO>> alteraProduto(@PathVariable Long id, @RequestBody ProdutoINDTO requisicao) {
        LOGGER.info("Requisicao para alterar o produto com o id {} recebida", id);
        var produto = this.produtoService.produtoPorId(id);
        LOGGER.info("Produto com o id {} resgatado. Iniciando alteracoes", id);
        var produtoSalvo = this.produtoService.alteraProduto(produto, requisicao);
        LOGGER.info("Alteracoes realizadas com sucesso");
        var modelo = EntityModel.of(MAPPER.produtoParaProdutoOUTDTO(produtoSalvo));
        List<Link> links = this.linksComId(id, "alteraProduto", requisicao);
        modelo.add(links);
        return ResponseEntity.ok(modelo);
    }

    @DeleteMapping("/delecao/{id}")
    public ResponseEntity<Void> deletaProduto(@PathVariable Long id) {
        LOGGER.info("Requisicao para deletar produto com id {} recebida", id);
        LOGGER.info("Tentando obter o produto utilizando id {}", id);
        var produto = this.produtoService.produtoPorId(id);
        LOGGER.info("Produto com id {} resgatado com sucesso", id);
        LOGGER.info("Iniciando delecao do produto com id {}", id);
        this.produtoService.removeProduto(produto);
        LOGGER.info("Delecao do produto com id {} efetuada com sucesso", id);
        return ResponseEntity.noContent().build();
    }

    private ProdutoINDTO obtemRequisicaoExemplo() {
        var requisicaoExemplo = new ProdutoINDTO();
        requisicaoExemplo.setNomeProduto("Nome Produto");
        requisicaoExemplo.setPrecoProduto(new BigDecimal(100));
        return requisicaoExemplo;
    }

    private List<Link> linksSemId(String metodo, List<Object> ids) {
        var requisicaoExemplo = this.obtemRequisicaoExemplo();
        var id = ThreadLocalRandom.current().nextLong(0, 20);
        return List.of(
                this.selfLink(metodo, id, ids, null),
                linkTo(methodOn(ProdutoController.class).listaOrdenada()).withRel("lista-ordenada"),
                linkTo(methodOn(ProdutoController.class).produtoPorId(id)).withRel("produto-por-id"),
                linkTo(methodOn(ProdutoController.class).salvaProduto(requisicaoExemplo)).withRel("salva-produto"),
                linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicaoExemplo)).withRel("altera-produto"),
                linkTo(methodOn(ProdutoController.class).deletaProduto(id)).withRel("deleta-produto")
        );
    }

    private List<Link> linksComId(Long id, String metodo, ProdutoINDTO requisicao) {
        var requisicaoExemplo = this.obtemRequisicaoExemplo();
        return List.of(
                this.selfLink(metodo, id, null, requisicao),
                linkTo(methodOn(ProdutoController.class).listaOrdenada()).withRel("lista-ordenada"),
                linkTo(methodOn(ProdutoController.class).produtoPorId(id)).withRel("produto-por-id"),
                linkTo(methodOn(ProdutoController.class).salvaProduto(requisicaoExemplo)).withRel("salva-produto"),
                linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicaoExemplo)).withRel("altera-produto"),
                linkTo(methodOn(ProdutoController.class).deletaProduto(id)).withRel("deleta-produto")
        );
    }

    private Link selfLink(String metodo, Long id, List<Object> ids, ProdutoINDTO requisicao) {
        return switch (metodo) {
            case "somaPrecos" -> linkTo(methodOn(ProdutoController.class).somaPrecos(ids)).withSelfRel();
            case "listaOrdenada" -> linkTo(methodOn(ProdutoController.class).listaOrdenada()).withSelfRel();
            case "produtoPorId" -> linkTo(methodOn(ProdutoController.class).produtoPorId(id)).withSelfRel();
            case "salvaProduto" -> linkTo(methodOn(ProdutoController.class).salvaProduto(requisicao)).withSelfRel();
            case "alteraProduto" ->
                    linkTo(methodOn(ProdutoController.class).alteraProduto(id, requisicao)).withSelfRel();
            case "deletaProduto" -> linkTo(methodOn(ProdutoController.class).deletaProduto(id)).withSelfRel();
            default -> linkTo(methodOn(ProdutoController.class).listaProdutos()).withSelfRel();
        };
    }

    private ResponseEntity<CollectionModel<ProdutoOUTDTO>> obtemLista(List<Produto> listaProduto, String metodo) {
        var lista = listaProduto
                .stream()
                .map(MAPPER::produtoParaProdutoOUTDTO)
                .toList();
        var modelos = CollectionModel.of(lista).withFallbackType(ProdutoOUTDTO.class);
        modelos.add(this.linksSemId(metodo, null));
        return ResponseEntity.ok(modelos);
    }

}
