package dev.franke.felipe.compras.compras.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.franke.felipe.compras.compras.api.model.Comprador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
class CompradorRepositoryTest {

    @Autowired
    private CompradorRepository compradorRepository;

    private Comprador comprador1;

    void criarComprador2() {
        var comprador = new Comprador("Amanda", new BigDecimal(16000), new BigDecimal(3500));
        compradorRepository.save(comprador);
    }

    @BeforeEach
    void setUp() {
        var comprador = new Comprador("Felipe", new BigDecimal(6000), new BigDecimal(3000));
        comprador1 = compradorRepository.save(comprador);
    }

    @Test
    @DisplayName("Teste - Metodo save()")
    void testeDadoComprador_QuandoSaveChamado_DeveSalvar() {
        var saldoPrevisto = 9000;
        assertNotNull(comprador1);
        assertNotNull(comprador1.getId());
        assertNotNull(comprador1.getSaldoTotal());
        assertNotNull(comprador1.getSaldoDebito());
        assertNotNull(comprador1.getSaldoValeAlimentacao());
        assertEquals(saldoPrevisto, comprador1.getSaldoTotal().intValue());
    }

    @Test
    @DisplayName("Teste - Metodo findAll()")
    void teste_QuandoFindAllChamado_RetornaLista() {
        criarComprador2();
        var compradores = compradorRepository.findAll();
        assertNotNull(compradores);
        assertFalse(compradores.isEmpty());
        assertEquals(2, compradores.size());
    }

    @Test
    @DisplayName("Teste - Metodo findById()")
    void teste_QuandoFindByIdChamado_RetornaComprador() {
        var compradorEncontrado = compradorRepository.findById(comprador1.getId());
        assertNotNull(compradorEncontrado);
        assertTrue(compradorEncontrado.isPresent());
        assertNotNull(compradorEncontrado.get().getSaldoTotal());
    }

    @Test
    @DisplayName("Teste - Metodo delete()")
    void teste_QuandoDeleteChamado_DeveApagar() {
        compradorRepository.delete(comprador1);
        var compradorOptional = compradorRepository.findById(comprador1.getId());
        assertTrue(compradorOptional.isEmpty());
    }

    @Test
    @DisplayName("Teste - Metodo compradoresAtivos() - 0 compradores")
    void testeDadoNenhumCompradorAtivo_QuandoCompradoresAtivos_RetornaListaVazia() {
        var compradores = compradorRepository.compradoresAtivos();
        assertNotNull(compradores);
        assertTrue(compradores.isEmpty());
    }

    @Test
    @DisplayName("Teste - Metodo compradoresAtivos() - 2 compradores")
    void testeDadosCompradoresAtivos_QuandoCompradoresAtivos_RetornaListaDeCompradoresAtivos() {
        var comprador3 = new Comprador("Lucas", new BigDecimal(100), new BigDecimal(100));
        var comprador4 = new Comprador("Rafael", new BigDecimal(100), new BigDecimal(100));
        compradorRepository.saveAll(List.of(comprador3, comprador4));
        comprador3.setTotalCompras(1);
        comprador4.setTotalCompras(2);
        compradorRepository.saveAll(List.of(comprador3, comprador4));
        var compradores = compradorRepository.compradoresAtivos();
        assertNotNull(compradores);
        assertEquals(2, compradores.size());
    }

    @Test
    @DisplayName("Teste - Metodo compradoresNegativados() - 0 compradores")
    void testeDadoNenhumCompradorNegativado_QuandoCompradoresNegativados_RetornaListaVazia() {
        var compradores = compradorRepository.compradoresNegativados();
        assertNotNull(compradores);
        assertTrue(compradores.isEmpty());
    }

    @Test
    @DisplayName("Teste - Metodo compradoresNegativados() - 2 compradores")
    void testeDadosCompradoresNegativados_QuandoCompradoresNegativados_RetornaListaDeCompradoresNegativados() {
        var comprador3 = new Comprador("Lucas", new BigDecimal(100), new BigDecimal(100));
        var comprador4 = new Comprador("Rafael", new BigDecimal(100), new BigDecimal(100));
        compradorRepository.saveAll(List.of(comprador3, comprador4));
        comprador3.setSaldoDebito(new BigDecimal(-91000));
        comprador4.setSaldoDebito(new BigDecimal(-91000));
        compradorRepository.saveAll(List.of(comprador3, comprador4));
        var compradores = compradorRepository.compradoresNegativados();
        assertNotNull(compradores);
        assertEquals(2, compradores.size());
    }
}
