package dev.franke.felipe.compras.compras.api.service;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.exception.ProdutoNaoEncontradoException;
import dev.franke.felipe.compras.compras.api.mapper.ProdutoMapper;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    public static final ProdutoMapper MAPPER = ProdutoMapper.INSTANCIA;
    public static final Logger LOGGER = LogManager.getLogger();

    private final ProdutoRepository produtoRepository;

    public List<Produto> listaTodosProdutos() {
        LOGGER.info("Service - Listando todos os produtos");
        LOGGER.info("Service - Total de registros: {}", this.produtoRepository.count());
        return this.produtoRepository.findAll();
    }

    public List<Produto> listaTodosProdutosOrdenada() {
        LOGGER.info("Service - Listando todos os produtos de forma ordenada");
        LOGGER.info("Service - Contagem: {}", this.produtoRepository.count());
        return this.produtoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public Produto produtoPorId(Long id) {
        LOGGER.info("Service - Busca por Id iniciada");
        return this.produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id.toString()));
    }

    public Produto cadastraProduto(ProdutoINDTO requisicao) {
        LOGGER.info("Service - Cadastro de Produto iniciado");
        LOGGER.info("Service - Validando requisicao");
        requisicao.validaTudo();
        LOGGER.info("Service - A requisicao e valida");
        Produto produto = MAPPER.produtoINDTOParaProduto(requisicao);
        return this.produtoRepository.save(produto);
    }

    @Transactional
    public Produto alteraProduto(Produto produto, ProdutoINDTO requisicao) {
        LOGGER.info("Service - Alteracao de Produto iniciado");
        LOGGER.info("Service - Validando requisicao de alteracao");
        requisicao.validaTudo();
        LOGGER.info("Service - Requisicao valida");
        produto.setNome(requisicao.getNomeProduto());
        produto.setPreco(requisicao.getPrecoProduto());
        return this.produtoRepository.save(produto);
    }

    @Transactional
    public void removeProduto(Produto produto) {
        LOGGER.info("Service - Delecao de Produto iniciado");
        this.produtoRepository.delete(produto);
    }

}
