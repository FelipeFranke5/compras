package dev.franke.felipe.compras.compras.api.service.helper.comprador;

import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.exception.CompradorINObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.CompradorObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.ValorCompraInvalidoException;
import dev.franke.felipe.compras.compras.api.exception.ValorProdutoObrigatorioException;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import java.math.BigDecimal;

public class ValidadorComprador {

    public static void validaCompradorERequisicao(Comprador comprador, BigDecimal valor) {
        if (comprador == null) throw new CompradorObrigatorioException("O comprador e obrigatorio");
        if (valor == null) throw new ValorProdutoObrigatorioException("O valor e obrigatorio");
    }

    public static void validaRequisicao(CompradorINDTO requisicao) {
        if (requisicao == null) throw new CompradorINObrigatorioException("A requisicao e obrigatoria");
    }

    public static void validaComprador(Comprador comprador) {
        if (comprador == null) throw new CompradorObrigatorioException("O comprador e obrigatorio");
    }

    public static void validaValorCompras(Comprador comprador, int valor) {
        if (valor <= 0 || valor < comprador.getTotalCompras()) {
            throw new ValorCompraInvalidoException("Valor invalido: " + valor);
        }
    }
}
