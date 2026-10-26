package com.cloudnative.eventos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompraRequest {

    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe comprar al menos 1 entrada")
    private Integer cantidad;
}