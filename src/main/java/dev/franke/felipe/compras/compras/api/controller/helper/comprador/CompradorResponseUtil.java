package dev.franke.felipe.compras.compras.api.controller.helper.comprador;

import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.links.CompradorLink;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CompradorResponseUtil {

    public static ResponseEntity<EntityModel<CompradorOUTDTO>> comprador(
            MetodoController metodoController,
            Comprador comprador,
            TipoOperacaoController tipoOperacaoController,
            CompradorLink compradorLink) {
        return switch (tipoOperacaoController) {
            case ALTERACAO -> ResponseEntity.ok().body(compradorLink.preparaComprador(comprador, metodoController));
            case CONSULTA -> ResponseEntity.ok().body(compradorLink.preparaComprador(comprador, metodoController));
            case CRIACAO -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(compradorLink.preparaComprador(comprador, metodoController));
            default -> ResponseEntity.ok().body(compradorLink.preparaComprador(comprador, metodoController));
        };
    }
}
