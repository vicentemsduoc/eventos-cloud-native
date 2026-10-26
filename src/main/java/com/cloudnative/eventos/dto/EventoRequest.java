package com.cloudnative.eventos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
}