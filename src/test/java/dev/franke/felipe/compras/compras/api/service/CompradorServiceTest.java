package dev.franke.felipe.compras.compras.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.exception.CompradorNaoEncontradoException;
import dev.franke.felipe.compras.compras.api.exception.IdCompradorInvalidoException;
import dev.franke.felipe.compras.compras.api.mapper.CompradorMapper;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import dev.franke.felipe.compras.compras.api.repository.CompradorRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompradorServiceTest {

    private static final CompradorMapper MAPPER = CompradorMapper.INSTANCIA;

    @Mock
    private CompradorRepository repository;

    @InjectMocks
    private CompradorService service;

    void validaNuloOptionalComprador(Object obj) {
        assertNotNull(obj);
        assertTrue(obj instanceof Comprador);
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

    void validaNuloListas(Object obj) {
        assertNotNull(obj);
        assertTrue(obj instanceof List);
    }

    void validaNuloListasVazias(Object obj) {
        validaNuloListas(obj);
        assertTrue(((List<?>) obj).isEmpty());
    }

    void validaNuloListasTamanho2(Object obj) {
        validaNuloListas(obj);
        assertFalse(((List<?>) obj).isEmpty());
        assertEquals(2, ((List<?>) obj).size());
    }

    @Test
    @DisplayName("Teste - Metodo listaCompradores - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ListaCompradoresChamado_RetornaListaVazia() {
        when(repository.findAll()).thenReturn(List.of());
        var resultado = service.listaCompradores();
        validaNuloListasVazias(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo listaCompradores - Lista com 2 registros")
    void testeQuandoExistem2Registros_ListaCompradoresChamado_RetornaListaCom2Registros() {
        var comprador1 = new Comprador("Felipe");
        var comprador2 = new Comprador("Lucas");
        when(repository.findAll()).thenReturn(List.of(comprador1, comprador2));
        var resultado = service.listaCompradores();
        validaNuloListasTamanho2(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo listaCompradoresAtivos - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ListaCompradoresAtivosChamado_RetornaListaVazia() {
        when(repository.compradoresAtivos()).thenReturn(List.of());
        var resultado = service.listaCompradoresAtivos();
        validaNuloListasVazias(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo listaCompradoresAtivos - Lista com 2 registros")
    void testeQuandoExistem2Registros_ListaCompradoresAtivosChamado_RetornaListaCom2Registros() {
        var comprador1 = new Comprador("Felipe");
        comprador1.setTotalCompras(comprador1.getTotalCompras() + 1);
        var comprador2 = new Comprador("Lucas");
        comprador2.setTotalCompras(comprador2.getTotalCompras() + 1);
        when(repository.compradoresAtivos()).thenReturn(List.of(comprador1, comprador2));
        var resultado = service.listaCompradoresAtivos();
        validaNuloListasTamanho2(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo listaCompradoresNegativados - Lista Vazia")
    void testeQuandoNaoExistemRegistros_ListaCompradoresNegativadosChamado_RetornaListaVazia() {
        when(repository.compradoresNegativados()).thenReturn(List.of());
        var resultado = service.listaCompradoresNegativados();
        validaNuloListasVazias(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo listaCompradoresNegativados - Lista com 2 registros")
    void testeQuandoExistem2Registros_ListaCOmpradoresNegativadosChamado_RetornaListaCom2Registros() {
        var comprador1 = new Comprador("Felipe");
        comprador1.setSaldoDebito(new BigDecimal(-3000));
        var comprador2 = new Comprador("Lucas");
        comprador2.setSaldoDebito(new BigDecimal(-3000));
        when(repository.compradoresNegativados()).thenReturn(List.of(comprador1, comprador2));
        var resultado = service.listaCompradoresNegativados();
        validaNuloListasTamanho2(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo porId - Cenario Feliz - ID encontrado")
    void testeQuandoIdValido_PorId_RetornaComprador() {
        var comprador1 = new Comprador("Felipe");
        comprador1.setId(UUID.randomUUID());
        when(repository.findById(comprador1.getId())).thenReturn(Optional.of(comprador1));
        var resultado = service.porId(comprador1.getId().toString());
        validaNuloOptionalComprador(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo porId - Cenario Triste - ID nao encontrado")
    void testeQuandoIdNaoEncontrado_PorId_LancaCompradorNaoEncontradoException() {
        var id = UUID.randomUUID();
        when(repository.findById(id))
                .thenThrow(new CompradorNaoEncontradoException("Comprador com id " + id + " nao encontrado"));
        assertThrowsExactly(CompradorNaoEncontradoException.class, () -> service.porId(id.toString()));
    }

    @Test
    @DisplayName("Teste - Metodo porId - Cenario Triste - ID invalido")
    void testeQuandoIdInvalido_PorId_LancaIdCompradorInvalidoException() {
        var id = "invalido";
        assertThrowsExactly(IdCompradorInvalidoException.class, () -> service.porId(id));
    }
}
