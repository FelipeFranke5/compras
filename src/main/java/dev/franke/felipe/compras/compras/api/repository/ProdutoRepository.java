package dev.franke.felipe.compras.compras.api.repository;

import dev.franke.felipe.compras.compras.api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findAllByOrderByDataCriacaoDesc();
    Optional<Produto> findByNome(String nome);
    boolean existsByNome(String nome);

    @Query("SELECT p FROM Produto p WHERE p.preco <= :precoMax")
    List<Produto> precoAbaixoDe(@Param("precoMax") BigDecimal precoMax);

    @Query("SELECT p FROM Produto p WHERE p.preco >= :precoMax")
    List<Produto> precoAcimaDe(@Param("precoMax") BigDecimal precoMax);
}
