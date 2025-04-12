package dev.franke.felipe.compras.compras.api.dto.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "produtos")
public class ProdutoOUTDTO {

    private Long idProduto;
    private String nomeProduto;
    private BigDecimal precoProduto;
    private LocalDateTime criadoEm;
    private LocalDateTime modificadoEm;
}
