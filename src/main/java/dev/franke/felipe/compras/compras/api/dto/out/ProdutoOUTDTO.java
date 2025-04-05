package dev.franke.felipe.compras.compras.api.dto.out;

import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Relation(collectionRelation = "produtos")
public class ProdutoOUTDTO {

    private Long idProduto;
    private String nomeProduto;
    private BigDecimal precoProduto;
    private LocalDateTime criadoEm;
    private LocalDateTime modificadoEm;

}
