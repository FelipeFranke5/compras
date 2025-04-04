package dev.franke.felipe.compras.compras.api.service;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.exception.ProdutoNaoEncontradoException;
import dev.franke.felipe.compras.compras.api.mapper.ProdutoMapper;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto> listaTodosProdutos() {
        return this.produtoRepository.findAll();
    }

    public List<Produto> listaTodosProdutosOrdenada() {
        return this.produtoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public Produto produtoPorId(Long id) {
        return this.produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id.toString()));
    }

    public Produto cadastraProduto(ProdutoINDTO requisicao) {
        requisicao.validaTudo();
        Produto produto = ProdutoMapper.INSTANCIA.produtoINDTOParaProduto(requisicao);
        return this.produtoRepository.save(produto);
    }

    public Produto alteraProduto(Produto produto, ProdutoINDTO requisicao) {
        requisicao.validaTudo();
        produto.setNome(requisicao.getNomeProduto());
        produto.setPreco(requisicao.getPrecoProduto());
        return this.produtoRepository.save(produto);
    }

    public void removeProduto(Produto produto) {
        this.produtoRepository.delete(produto);
    }

}
