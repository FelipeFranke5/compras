package dev.franke.felipe.compras.compras.api.dto.out;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProdutoOUTDTO {

    private Long idProduto;
    private String nomeProduto;
    private BigDecimal precoProduto;
    private LocalDateTime criadoEm;
    private LocalDateTime modificadoEm;

}
