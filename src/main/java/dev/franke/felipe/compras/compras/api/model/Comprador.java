package dev.franke.felipe.compras.compras.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "comprador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comprador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "saldo_total")
    private BigDecimal saldoTotal = new BigDecimal(0);

    @Column(name = "saldo_debito")
    private BigDecimal saldoDebito = new BigDecimal(0);

    @Column(name = "saldo_vale_alimentacao")
    private BigDecimal saldoValeAlimentacao = new BigDecimal(0);

    @Column(name = "total_compras")
    private Integer totalCompras = 0;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    public Comprador(String nome) {
        this.nome = nome;
    }

    public Comprador(String nome, BigDecimal saldoDebito, BigDecimal saldoVale) {
        this.nome = nome;
        this.saldoDebito = saldoDebito;
        this.saldoValeAlimentacao = saldoVale;
    }

    @PrePersist
    @PreUpdate
    public void defineSaldo() {
        var saldo = new BigDecimal(this.getSaldoDebito().intValue()
                + this.getSaldoValeAlimentacao().intValue());
        this.setSaldoTotal(saldo);
    }
}
