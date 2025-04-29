package dev.franke.felipe.compras.compras.api.controller.helper.comprador;

import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.links.CompradorLink;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

public class ListaUtil {

    public static ResponseEntity<CollectionModel<CompradorOUTDTO>> lista(
            MetodoController metodoController, List<Comprador> compradores, CompradorLink compradorLink) {
        return ResponseEntity.ok().body(compradorLink.preparaLista(compradores, metodoController));
    }
}
