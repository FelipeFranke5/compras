package dev.franke.felipe.compras.compras.api.service;

import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.exception.*;
import dev.franke.felipe.compras.compras.api.mapper.CompradorMapper;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import dev.franke.felipe.compras.compras.api.repository.CompradorRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompradorService {

    private static final CompradorMapper MAPPER = CompradorMapper.INSTANCIA;

    private final CompradorRepository compradorRepository;

    private static void validaCompradorERequisicao(Comprador comprador, BigDecimal valor) {
        if (comprador == null) throw new CompradorObrigatorioException("O comprador e obrigatorio");
        if (valor == null) throw new ValorProdutoObrigatorioException("O valor e obrigatorio");
    }

    public List<Comprador> listaCompradores() {
        return compradorRepository.findAll();
    }

    public List<Comprador> listaCompradoresAtivos() {
        return compradorRepository.compradoresAtivos();
    }

    public List<Comprador> listaCompradoresNegativados() {
        return compradorRepository.compradoresNegativados();
    }

    public Comprador porId(String id) {
        try {
            return compradorRepository
                    .findById(UUID.fromString(id))
                    .orElseThrow(
                            () -> new CompradorNaoEncontradoException("Comprador com id " + id + " nao encontrado"));
        } catch (IllegalArgumentException idInvalidoException) {
            throw new IdCompradorInvalidoException(idInvalidoException.getMessage());
        }
    }

    public Comprador cadastra(CompradorINDTO requisicao) {
        if (requisicao == null) throw new CompradorINObrigatorioException("A requisicao e obrigatoria");
        requisicao.validaTudo();
        var comprador = MAPPER.compradorINDTOParaComprador(requisicao);
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaNome(Comprador comprador, CompradorINDTO requisicao) {
        if (comprador == null) throw new CompradorObrigatorioException("O comprador e obrigatorio");
        if (requisicao == null) throw new CompradorINObrigatorioException("A requisicao e obrigatoria");
        requisicao.validaTudo();
        comprador.setNome(requisicao.getNome());
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaSaldoDebito(Comprador comprador, BigDecimal valor) {
        validaCompradorERequisicao(comprador, valor);
        comprador.setSaldoDebito(valor);
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaSaldoValeAlimentacao(Comprador comprador, BigDecimal valor) {
        validaCompradorERequisicao(comprador, valor);
        comprador.setSaldoValeAlimentacao(valor);
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaTotalCompras(Comprador comprador, int valor) {
        if (comprador == null) throw new CompradorObrigatorioException("O comprador e obrigatorio");
        if (valor <= 0 || valor < comprador.getTotalCompras()) {
            throw new ValorCompraInvalidoException("Valor invalido: " + valor);
        }
        comprador.setTotalCompras(valor);
        return compradorRepository.save(comprador);
    }

    public void apaga(Comprador comprador) {
        if (comprador == null) throw new CompradorObrigatorioException("O comprador e obrigatorio");
        compradorRepository.delete(comprador);
    }
}
