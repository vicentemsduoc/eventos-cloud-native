package com.cloudnative.eventos.controller;

import com.cloudnative.eventos.dto.CompraRequest;
import com.cloudnative.eventos.dto.CompraResponse;
import com.cloudnative.eventos.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<CompraResponse> comprarEntradas(@Valid @RequestBody CompraRequest request) {
        CompraResponse response = compraService.realizarCompra(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(compraService.obtenerEstadisticas());
    }
}