package dev.franke.felipe.compras.compras.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.exception.*;
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

    @Mock
    private CompradorRepository repository;

    @InjectMocks
    private CompradorService service;

    void validaNuloOptionalComprador(Object obj) {
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

    void validaNuloListas(Object obj) {
        assertNotNull(obj);
        assertInstanceOf(List.class, obj);
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
    void testeQuandoExistem2Registros_ListaCompradoresNegativadosChamado_RetornaListaCom2Registros() {
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
    void testeQuandoIdValido_PorIdChamado_RetornaComprador() {
        var comprador1 = new Comprador("Felipe");
        comprador1.setId(UUID.randomUUID());
        when(repository.findById(comprador1.getId())).thenReturn(Optional.of(comprador1));
        var resultado = service.porId(comprador1.getId().toString());
        validaNuloOptionalComprador(resultado);
    }

    @Test
    @DisplayName("Teste - Metodo porId - Cenario Triste - ID nao encontrado")
    void testeQuandoIdNaoEncontrado_PorIdChamado_LancaCompradorNaoEncontradoException() {
        var id = UUID.randomUUID();
        when(repository.findById(id))
                .thenThrow(new CompradorNaoEncontradoException("Comprador com id " + id + " nao encontrado"));
        assertThrowsExactly(CompradorNaoEncontradoException.class, () -> service.porId(id.toString()));
    }

    @Test
    @DisplayName("Teste - Metodo porId - Cenario Triste - ID invalido")
    void testeQuandoIdInvalido_PorIdChamado_LancaIdCompradorInvalidoException() {
        var id = "invalido";
        assertThrowsExactly(IdCompradorInvalidoException.class, () -> service.porId(id));
    }

    @Test
    @DisplayName("Teste - Metodo cadastra - Cenario Feliz - Request valida")
    void testeQuandoRequestValida_CadastraChamado_RetornaComprador() {
        var comprador = new Comprador("Felipe");
        var compradorINDTO = new CompradorINDTO();
        compradorINDTO.setNome(comprador.getNome());
        when(repository.save(comprador)).thenReturn(comprador);
        var resultado = service.cadastra(compradorINDTO);
        validaNuloOptionalComprador(resultado);
        verify(repository).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo cadastra - Cenario Triste - CompradorINDTO nulo")
    void testeQuandoCompradorINDTONulo_CadastraChamado_LancaCompradorINObrigatorioException() {
        assertThrowsExactly(CompradorINObrigatorioException.class, () -> service.cadastra(null));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo cadastra - Cenario Triste - Nome nulo")
    void testeQuandoNomeNulo_CadastraChamado_LancaNomeCompradorObrigatorioException() {
        var compradorINDTO = new CompradorINDTO();
        assertThrowsExactly(NomeCompradorObrigatorioException.class, () -> service.cadastra(compradorINDTO));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo cadastra - Cenario Triste - Nome vazio")
    void testeQuandoNomeVazio_CadastraChamado_LancaNomeCompradorObrigatorioException() {
        var compradorINDTO = new CompradorINDTO();
        compradorINDTO.setNome("");
        assertThrowsExactly(NomeCompradorObrigatorioException.class, () -> service.cadastra(compradorINDTO));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo cadastra - Cenario Triste - Nome com menos de 5 caracteres")
    void testeQuandoNomeContem3Caracteres_CadastraChamado_LancaTamanhoNomeCompradorInvalidoException() {
        var compradorINDTO = new CompradorINDTO();
        compradorINDTO.setNome("abc");
        assertThrowsExactly(TamanhoNomeCompradorInvalidoException.class, () -> service.cadastra(compradorINDTO));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo cadastra - Cenario Triste - Nome com mais de 30 caracteres")
    void testeQuandoNomeContem31Caracteres_CadastraChamado_LancaTamanhoNomeCompradorInvalidoException() {
        var compradorINDTO = new CompradorINDTO();
        var inicioString = "abc";
        var stringInvalida =
                inicioString.repeat((31 / inicioString.length()) + 1).substring(0, 31);
        compradorINDTO.setNome(stringInvalida);
        assertThrowsExactly(TamanhoNomeCompradorInvalidoException.class, () -> service.cadastra(compradorINDTO));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Feliz - Nome valido")
    void testeQuandoRequestValida_AtualizaNomeChamado_RetornaComprador() {
        var compradorAnterior = new Comprador("Felipe");
        var compradorNovo = new Comprador("Lucas");
        var requisicaoMudanca = new CompradorINDTO();
        requisicaoMudanca.setNome("Lucas");
        when(repository.save(any(Comprador.class))).thenReturn(compradorNovo);
        var resultado = service.atualizaNome(compradorAnterior, requisicaoMudanca);
        validaNuloOptionalComprador(resultado);
        assertEquals(compradorNovo.getNome(), resultado.getNome());
        verify(repository).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Triste - CompradorINDTO nulo")
    void testeQuandoCompradorINDTONulo_AtualizaNomeChamado_LancaCompradorINObrigatorioException() {
        var compradorAnterior = new Comprador("Felipe");
        assertThrowsExactly(CompradorINObrigatorioException.class, () -> service.atualizaNome(compradorAnterior, null));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Triste - Comprador nulo")
    void testeQuandoCompradorNulo_AtualizaNomeChamado_LancaCompradorObrigatorioException() {
        assertThrowsExactly(CompradorObrigatorioException.class, () -> service.atualizaNome(null, null));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Triste - Nome nulo")
    void testeQuandoNomeNulo_AtualizaNomeChamado_LancaNomeCompradorObrigatorioException() {
        var compradorAnterior = new Comprador("Felipe");
        var requisicaoMudanca = new CompradorINDTO();
        assertThrowsExactly(
                NomeCompradorObrigatorioException.class,
                () -> service.atualizaNome(compradorAnterior, requisicaoMudanca));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Triste - Nome vazio")
    void testeQuandoNomeVazio_AtualizaNomeChamado_LancaNomeCompradorObrigatorioException() {
        var compradorAnterior = new Comprador("Felipe");
        var requisicaoMudanca = new CompradorINDTO();
        requisicaoMudanca.setNome("");
        assertThrowsExactly(
                NomeCompradorObrigatorioException.class,
                () -> service.atualizaNome(compradorAnterior, requisicaoMudanca));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Triste - Nome com menos de 5 caracteres")
    void testeQuandoNomeContem3Caracteres_AtualizaNomeChamado_LancaTamanhoNomeCompradorInvalidoException() {
        var compradorAnterior = new Comprador("Felipe");
        var requisicaoMudanca = new CompradorINDTO();
        requisicaoMudanca.setNome("abc");
        assertThrowsExactly(
                TamanhoNomeCompradorInvalidoException.class,
                () -> service.atualizaNome(compradorAnterior, requisicaoMudanca));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaNome - Cenario Triste - Nome com mais de 30 caracteres")
    void testeQuandoNomeContem31Caracteres_AtualizaNomeChamado_LancaTamanhoNomeCompradorInvalidoException() {
        var compradorAnterior = new Comprador("Felipe");
        var requisicaoMudanca = new CompradorINDTO();
        var inicioString = "abc";
        var stringInvalida =
                inicioString.repeat((31 / inicioString.length()) + 1).substring(0, 31);
        requisicaoMudanca.setNome(stringInvalida);
        assertThrowsExactly(
                TamanhoNomeCompradorInvalidoException.class,
                () -> service.atualizaNome(compradorAnterior, requisicaoMudanca));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaSaldoDebito - Cenario Feliz - Saldo valido")
    void testeQuandoSaldoDebitoValido_AtualizaSaldoDebito_RetornaComprador() {
        var comprador = new Comprador("Felipe");
        var valor = new BigDecimal(100);
        when(repository.save(any(Comprador.class))).thenReturn(comprador);
        var resultado = service.atualizaSaldoDebito(comprador, valor);
        assertEquals(valor.intValue(), resultado.getSaldoDebito().intValue());
        verify(repository).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaSaldoDebito - Cenario Triste - Comprador nulo")
    void testeQuandoCompradorNulo_AtualizaSaldoDebito_LancaCompradorObrigatorioException() {
        var valor = new BigDecimal(100);
        assertThrowsExactly(CompradorObrigatorioException.class, () -> service.atualizaSaldoDebito(null, valor));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaSaldoDebito - Cenario Triste - Valor nulo")
    void testeQuandoValorNulo_AtualizaSaldoDebito_LancaValorProdutoObrigatorioException() {
        var comprador = new Comprador("Felipe");
        assertThrowsExactly(ValorProdutoObrigatorioException.class, () -> service.atualizaSaldoDebito(comprador, null));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaSaldoValeAlimentacao - Cenario Feliz - Saldo valido")
    void testeQuandoSaldoDebitoValido_AtualizaSaldoValeAlimentacao_RetornaComprador() {
        var comprador = new Comprador("Felipe");
        var valor = new BigDecimal(100);
        when(repository.save(any(Comprador.class))).thenReturn(comprador);
        var resultado = service.atualizaSaldoValeAlimentacao(comprador, valor);
        assertEquals(valor.intValue(), resultado.getSaldoValeAlimentacao().intValue());
        verify(repository).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaSaldoValeAlimentacao - Cenario Triste - Comprador nulo")
    void testeQuandoCompradorNulo_AtualizaSaldoValeAlimentacao_LancaCompradorObrigatorioException() {
        var valor = new BigDecimal(100);
        assertThrowsExactly(
                CompradorObrigatorioException.class, () -> service.atualizaSaldoValeAlimentacao(null, valor));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaSaldoValeAlimentacao - Cenario Triste - Valor nulo")
    void testeQuandoValorNulo_AtualizaSaldoValeAlimentacao_LancaValorProdutoObrigatorioException() {
        var comprador = new Comprador("Felipe");
        assertThrowsExactly(ValorProdutoObrigatorioException.class, () -> service.atualizaSaldoDebito(comprador, null));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaTotalCompras - Cenario Feliz - Total valido")
    void testeQuandoTotalComprasValido_AtualizaTotalCompras_RetornaComprador() {
        var comprador = new Comprador("Felipe");
        var valor = 10;
        when(repository.save(any(Comprador.class))).thenReturn(comprador);
        var resultado = service.atualizaTotalCompras(comprador, valor);
        assertEquals(valor, resultado.getTotalCompras());
        verify(repository).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo atualizaTotalCompras - Cenario Triste - Comprador nulo")
    void testeQuandoCompradorNulo_AtualizaTotalCompras_LancaCompradorObrigatorioException() {
        var valor = 10;
        assertThrowsExactly(CompradorObrigatorioException.class, () -> service.atualizaTotalCompras(null, valor));
        verify(repository, never()).save(any(Comprador.class));
    }

    @Test
    @DisplayName("Teste - Metodo apaga - Cenario Feliz - Comprador encontrado")
    void testeQuandoCompradorEncontrado_ApagaChamado_CompradorApagado() {
        var comprador = new Comprador("Felipe");
        service.apaga(comprador);
        verify(repository).delete(comprador);
    }

    @Test
    @DisplayName("Teste - Metodo apaga - Cenario Triste - Comprador nulo")
    void testeQuandoCompradorNulo_ApagaChamado_LancaCompradorObrigatorioException() {
        assertThrowsExactly(CompradorObrigatorioException.class, () -> service.apaga(null));
        verify(repository, never()).delete(any(Comprador.class));
    }
}
