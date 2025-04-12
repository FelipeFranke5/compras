package dev.franke.felipe.compras.compras.api.repository;

import dev.franke.felipe.compras.compras.api.model.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, UUID> {
    @Query("SELECT c FROM Comprador c WHERE c.totalCompras > 0")
    List<Comprador> compradoresAtivos();

    @Query("SELECT c FROM Comprador c WHERE c.saldoTotal < 0")
    List<Comprador> compradoresNegativados();
}
