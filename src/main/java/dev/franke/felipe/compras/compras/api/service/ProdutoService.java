package dev.franke.felipe.compras.compras.api.service;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.exception.ListaProdutosInvalidaException;
import dev.franke.felipe.compras.compras.api.exception.NomeProdutoJaCadastradoException;
import dev.franke.felipe.compras.compras.api.exception.ProdutoNaoEncontradoException;
import dev.franke.felipe.compras.compras.api.exception.ProdutoRequisicaoInvalidaException;
import dev.franke.felipe.compras.compras.api.exception.QueryPrecoInvalidoException;
import dev.franke.felipe.compras.compras.api.mapper.ProdutoMapper;
import dev.franke.felipe.compras.compras.api.model.Produto;
import dev.franke.felipe.compras.compras.api.repository.ProdutoRepository;
import dev.franke.felipe.compras.compras.api.service.lista_produtos.ResultadoSomaProdutos;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    public static final ProdutoMapper MAPPER = ProdutoMapper.INSTANCIA;
    public static final Logger LOGGER = LogManager.getLogger();

    private final ProdutoRepository produtoRepository;

    public static void validaRequisicaoNula(ProdutoINDTO requisicao) {
        if (requisicao == null) {
            LOGGER.warn("Requisicao nula");
            throw new ProdutoRequisicaoInvalidaException("Requisicao nao pode ser nula");
        }
    }

    public static void validaProdutoNulo(Produto produto) {
        if (produto == null) {
            LOGGER.warn("Produto nulo");
            throw new ProdutoRequisicaoInvalidaException("Produto nao pode ser nulo");
        }
    }

    public static String idInvalidoOuTextoPadrao(Object idInvalido) {
        if (idInvalido instanceof String idInvalidoString) {
            return idInvalidoString.length() < 3 ? idInvalidoString : "texto_invalido";
        }
        return "objeto_invalido";
    }

    public static BigDecimal decimal(int precoInt) {
        LOGGER.info("Criando BigDecimal com valor {}", precoInt);
        var precoDecimal = new BigDecimal(precoInt);
        LOGGER.info("BigDecimal criado");
        LOGGER.info("Retornando lista");
        return precoDecimal;
    }

    public static int getPrecoInt(String precoString) {
        int precoInt;
        LOGGER.info("Realizando parsing do valor informado");

        try {
            precoInt = Integer.parseInt(precoString);
        } catch (NumberFormatException formato) {
            LOGGER.info("Parsing nao realizado. O valor informado nao e numerico");
            throw new QueryPrecoInvalidoException("O preco deve ser numerico");
        }

        LOGGER.info("Parsing realizado com sucesso");
        return precoInt;
    }

    public List<Produto> listaTodosProdutos() {
        LOGGER.info("Iniciando busca de todos os produtos");
        LOGGER.info("Busca ordenada: {}", false);
        return this.produtoRepository.findAll();
    }

    public List<Produto> listaTodosProdutosOrdenada() {
        LOGGER.info("Iniciando busca de todos os produtos");
        LOGGER.info("Busca ordenada: {}", true);
        return this.produtoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public Produto produtoPorId(Long id) {
        LOGGER.info("Iniciando busca de um produto por Id");
        LOGGER.info("Id informado: {}", id);
        return this.produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id.toString()));
    }

    public Produto produtoPorNome(String nome) {
        LOGGER.info("Iniciando busca de um produto por nome");
        LOGGER.info("Nome informado: {}", nome);
        return this.produtoRepository.findByNome(nome).orElseThrow(() -> new ProdutoNaoEncontradoException(nome));
    }

    public boolean produtoExistePorNome(String nome) {
        LOGGER.info("Iniciando busca para identificar se o produto existe pelo nome");
        LOGGER.info("Nome informado: {}", nome);
        return this.produtoRepository.existsByNome(nome);
    }

    public List<Produto> produtoPorPreco(Object preco, boolean precoAbaixo) {
        LOGGER.info("Iniciando busca de produtos por preco");
        LOGGER.info("Preco informado: {}", preco);
        LOGGER.info("Flag de Preco abaixo definida para: {}", precoAbaixo);

        if (!(preco instanceof String precoString)) throw new QueryPrecoInvalidoException("Preco invalido");
        int precoInt = getPrecoInt(precoString);
        if (precoInt <= 0) throw new QueryPrecoInvalidoException("O preco nao pode ser zero ou negativo");

        var precoDecimal = decimal(precoInt);
        return precoAbaixo
                ? this.produtoRepository.precoAbaixoDe(precoDecimal)
                : this.produtoRepository.precoAcimaDe(precoDecimal);
    }

    public Produto cadastraProduto(ProdutoINDTO requisicao) {
        LOGGER.info("Iniciando cadastro de produto");
        validaRequisicaoNula(requisicao);
        requisicao.validaTudo();
        LOGGER.info("Verificando se o nome ja esta cadastrado");
        this.validaNomeProdutoJaExiste(requisicao.getNomeProduto());
        Produto produto = MAPPER.produtoINDTOParaProduto(requisicao);

        var preco = produto.getPreco();
        produto.setPreco(preco);
        return this.produtoRepository.save(produto);
    }

    public ResultadoSomaProdutos calculaTotalProdutos(List<Object> ids) {
        LOGGER.info("Iniciando funcao para somar os precos de uma lista de produtos");
        this.validaListaIdsEstaVazia(ids);

        var total = new AtomicInteger();
        var idsConsiderados = new ArrayList<Long>();
        var idsDesconsiderados = new ArrayList<>();
        var idsEncontrados = new ArrayList<Long>();
        var idsNaoEncontrados = new ArrayList<Long>();
        var nomesProdutos = new ArrayList<String>();

        ids.forEach(id -> {
            if (id instanceof Number idNumero) {
                Long idLong = idNumero.longValue();
                idsConsiderados.add(idLong);

                try {
                    var produto = this.produtoPorId(idLong);
                    var precoProduto = produto.getPreco();
                    total.addAndGet(precoProduto.intValue());
                    idsEncontrados.add(idLong);
                    var descricaoProduto =
                            produto.getNome() + " - " + produto.getPreco().intValue();
                    nomesProdutos.add(descricaoProduto);
                } catch (ProdutoNaoEncontradoException excecao) {
                    idsNaoEncontrados.add(idLong);
                }

            } else {
                idsDesconsiderados.add(idInvalidoOuTextoPadrao(id));
            }
        });

        var soma = new BigDecimal(total.get());
        return new ResultadoSomaProdutos(
                soma, nomesProdutos, idsConsiderados, idsDesconsiderados, idsEncontrados, idsNaoEncontrados);
    }

    @Transactional
    public Produto alteraProduto(Produto produto, ProdutoINDTO requisicao) {
        LOGGER.info("Iniciando metodo para alteracao de um produto");
        validaRequisicaoNula(requisicao);
        validaProdutoNulo(produto);
        requisicao.validaTudo();
        this.validaNomeProdutoJaExiste(requisicao.getNomeProduto());
        produto.setNome(requisicao.getNomeProduto());
        produto.setPreco(requisicao.getPrecoProduto());
        return this.produtoRepository.save(produto);
    }

    @Transactional
    public void removeProduto(Produto produto) {
        LOGGER.info("Iniciando metodo para remover um produto");
        validaProdutoNulo(produto);
        this.produtoRepository.delete(produto);
    }

    private void validaListaIdsEstaVazia(List<Object> ids) {
        LOGGER.info("Verificando se a lista de objetos esta vazia para soma de produtos");
        if (ids == null || ids.isEmpty()) {
            LOGGER.warn("Lista de objetos esta vazia e nenhum calculo sera realizado");
            throw new ListaProdutosInvalidaException("A lista esta vazia");
        }
    }

    private void validaNomeProdutoJaExiste(String nome) {
        LOGGER.info("Verificando no banco se existe um produto com este nome");

        if (this.produtoRepository.existsByNome(nome)) {
            LOGGER.info("Produto com nome ({}) ja cadastrado no banco", nome);
            throw new NomeProdutoJaCadastradoException(nome);
        }

        LOGGER.info("Validacao do banco realizada com sucesso");
    }
}
