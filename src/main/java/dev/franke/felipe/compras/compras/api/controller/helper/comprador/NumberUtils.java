package dev.franke.felipe.compras.compras.api.controller.helper.comprador;

import dev.franke.felipe.compras.compras.api.exception.ValorProdutoInvalidoException;
import java.math.BigDecimal;

public class NumberUtils {

    public static int tentarInt(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException formataNumeroException) {
            throw new ValorProdutoInvalidoException("O valor nao e valido");
        }
    }

    public static BigDecimal tentarFloat(String valor) {
        try {
            return BigDecimal.valueOf(Float.parseFloat(valor));
        } catch (NumberFormatException formataNumeroException) {
            return tentarLong(valor);
        }
    }

    public static BigDecimal tentarLong(String valor) {
        try {
            return BigDecimal.valueOf(Long.parseLong(valor));
        } catch (NumberFormatException formataNumeroException) {
            throw new ValorProdutoInvalidoException("O valor nao e valido");
        }
    }
}
