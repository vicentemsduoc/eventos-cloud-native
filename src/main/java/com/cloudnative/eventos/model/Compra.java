package com.cloudnative.eventos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El evento es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe comprar al menos 1 entrada")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(nullable = false)
    private LocalDateTime fechaCompra = LocalDateTime.now();
}