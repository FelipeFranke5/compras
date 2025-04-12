package dev.franke.felipe.compras.compras.api.dto.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "compradores")
public class CompradorOUTDTO {

    private UUID id;
    private String nome;
    private BigDecimal saldoTotal;
    private BigDecimal saldoDebito;
    private BigDecimal saldoValeAlimentacao;
    private Integer totalCompras;
    private LocalDateTime dataCriacao;

}
