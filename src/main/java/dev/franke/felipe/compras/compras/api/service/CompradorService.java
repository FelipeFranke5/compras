package dev.franke.felipe.compras.compras.api.service;

import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.exception.*;
import dev.franke.felipe.compras.compras.api.mapper.CompradorMapper;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import dev.franke.felipe.compras.compras.api.repository.CompradorRepository;
import dev.franke.felipe.compras.compras.api.service.helper.comprador.ValidadorComprador;
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
        ValidadorComprador.validaRequisicao(requisicao);
        requisicao.validaTudo();
        var comprador = MAPPER.compradorINDTOParaComprador(requisicao);
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaNome(Comprador comprador, CompradorINDTO requisicao) {
        ValidadorComprador.validaComprador(comprador);
        ValidadorComprador.validaRequisicao(requisicao);
        requisicao.validaTudo();
        comprador.setNome(requisicao.getNome());
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaSaldoDebito(Comprador comprador, BigDecimal valor) {
        ValidadorComprador.validaCompradorERequisicao(comprador, valor);
        comprador.setSaldoDebito(valor);
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaSaldoValeAlimentacao(Comprador comprador, BigDecimal valor) {
        ValidadorComprador.validaCompradorERequisicao(comprador, valor);
        comprador.setSaldoValeAlimentacao(valor);
        return compradorRepository.save(comprador);
    }

    public Comprador atualizaTotalCompras(Comprador comprador, int valor) {
        ValidadorComprador.validaComprador(comprador);
        ValidadorComprador.validaValorCompras(comprador, valor);
        comprador.setTotalCompras(valor);
        return compradorRepository.save(comprador);
    }

    public void apaga(Comprador comprador) {
        ValidadorComprador.validaComprador(comprador);
        compradorRepository.delete(comprador);
    }
}
