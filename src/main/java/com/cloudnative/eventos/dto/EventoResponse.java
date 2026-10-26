package com.cloudnative.eventos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventoResponse {

    private Long id;
    private String nombre;
    private LocalDateTime fecha;
    private BigDecimal precio;
}