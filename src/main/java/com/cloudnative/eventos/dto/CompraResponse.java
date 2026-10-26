package com.cloudnative.eventos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CompraResponse {

    private Long id;
    private String nombreEvento;
    private LocalDateTime fechaEvento;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;
    private LocalDateTime fechaCompra;
}