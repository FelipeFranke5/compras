package dev.franke.felipe.compras.compras.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.franke.felipe.compras.compras.api.model.Comprador;
import java.util.List;

public class ValidadorTestes {

    public void validaNuloOptionalComprador(Object obj) {
        assertNotNull(obj);
        assertInstanceOf(Comprador.class, obj);
        var comprador = (Comprador) obj;
        assertNotNull(comprador.getNome());
        assertNotNull(comprador.getSaldoDebito());
        assertNotNull(comprador.getSaldoValeAlimentacao());
        assertNotNull(comprador.getSaldoTotal());
        assertEquals(
                comprador.getSaldoTotal().intValue(),
                comprador.getSaldoDebito().intValue()
                        + comprador.getSaldoValeAlimentacao().intValue());
    }

    public void validaNuloListas(Object obj) {
        assertNotNull(obj);
        assertInstanceOf(List.class, obj);
    }

    public void validaNuloListasVazias(Object obj) {
        validaNuloListas(obj);
        assertTrue(((List<?>) obj).isEmpty());
    }

    public void validaNuloListasTamanho2(Object obj) {
        validaNuloListas(obj);
        assertFalse(((List<?>) obj).isEmpty());
        assertEquals(2, ((List<?>) obj).size());
    }
}
