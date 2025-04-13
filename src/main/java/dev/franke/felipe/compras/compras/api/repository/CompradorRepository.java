package dev.franke.felipe.compras.compras.api.repository;

import dev.franke.felipe.compras.compras.api.model.Comprador;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, UUID> {
    @Query("SELECT c FROM Comprador c WHERE c.totalCompras > 0")
    List<Comprador> compradoresAtivos();

    @Query("SELECT c FROM Comprador c WHERE c.saldoTotal < 0")
    List<Comprador> compradoresNegativados();
}
