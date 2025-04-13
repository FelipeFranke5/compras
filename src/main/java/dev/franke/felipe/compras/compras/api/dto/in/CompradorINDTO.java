package dev.franke.felipe.compras.compras.api.dto.in;

import dev.franke.felipe.compras.compras.api.exception.NomeCompradorObrigatorioException;
import dev.franke.felipe.compras.compras.api.exception.TamanhoNomeCompradorInvalidoException;
import lombok.Data;

@Data
public class CompradorINDTO {

    private String nome;

    private void validaNome() {
        if (this.getNome() == null || this.getNome().isBlank()) {
            throw new NomeCompradorObrigatorioException("O nome do comprador e obrigatorio");
        }
        if (this.getNome().length() < 5 || this.getNome().length() > 30) {
            throw new TamanhoNomeCompradorInvalidoException(
                    "O tamanho do nome do comprador deve ser entre 5 e 30 caracteres");
        }
    }

    public void validaTudo() {
        this.validaNome();
    }
}
