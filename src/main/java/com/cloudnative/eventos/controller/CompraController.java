package com.cloudnative.eventos.controller;

import com.cloudnative.eventos.dto.CompraRequest;
import com.cloudnative.eventos.dto.CompraResponse;
import com.cloudnative.eventos.model.Compra;
import com.cloudnative.eventos.model.Evento;
import com.cloudnative.eventos.repository.CompraRepository;
import com.cloudnative.eventos.repository.EventoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;

    // POST /compras → realiza una compra de entradas
    @PostMapping
    public ResponseEntity<CompraResponse> comprarEntradas(@Valid @RequestBody CompraRequest request) {
        // 1. Buscar el evento
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Evento no encontrado con id: " + request.getEventoId()));

        // 2. Calcular totales
        BigDecimal precioUnitario = evento.getPrecio();
        BigDecimal precioTotal = precioUnitario.multiply(BigDecimal.valueOf(request.getCantidad()));

        // 3. Crear y guardar la compra
        Compra compra = new Compra();
        compra.setEvento(evento);
        compra.setCantidad(request.getCantidad());
        compra.setPrecioUnitario(precioUnitario);
        compra.setPrecioTotal(precioTotal);
        compra.setFechaCompra(LocalDateTime.now());

        Compra guardada = compraRepository.save(compra);

        // 4. Devolver el resumen
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(guardada));
    }

    private CompraResponse toResponse(Compra compra) {
        CompraResponse response = new CompraResponse();
        response.setId(compra.getId());
        response.setNombreEvento(compra.getEvento().getNombre());
        response.setFechaEvento(compra.getEvento().getFecha());
        response.setCantidad(compra.getCantidad());
        response.setPrecioUnitario(compra.getPrecioUnitario());
        response.setPrecioTotal(compra.getPrecioTotal());
        response.setFechaCompra(compra.getFechaCompra());
        return response;
    }
}